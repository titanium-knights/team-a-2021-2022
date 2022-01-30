package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.*;

public abstract class TeleOpMode extends OpMode {
    public enum DumpState {
        DUMPING,
        RETURNING_TO_IDLE,
        IDLE
    }

    MecanumDrive drive;
    Carousel carousel;
    MotorInterpolation carouselInterpolation;
    Slide slides;
    TubeIntake intake;
    Carriage carriage;
    MotorInterpolation carriageInterpolation;
    Speed speed;
    ToggleButton slowModeButton;
    PushButton dumpButton;
    DumpState dumpState = DumpState.IDLE;
    public static int SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = 3427;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        carousel = new Carousel(hardwareMap);
        carouselInterpolation = new MotorInterpolation(0, 0.375);
        slides = new Slide(hardwareMap);
        slides.stopAndResetEncoder();
        intake = new TubeIntake(hardwareMap);
        carriage = new Carriage(hardwareMap);
        carriageInterpolation = new MotorInterpolation(carriage.getPosition(), 0.5);
        speed = Speed.FAST;
        slowModeButton = new ToggleButton(() -> gamepad1.dpad_up);
        dumpButton = new PushButton(() -> gamepad1.a);
    }

    public void setSlidePosition(int position) {
        slides.setTargetPosition(position);
        slides.setPower(0.8);
    }

    @Override
    public void loop() {
        normalTeleOpActivities();
        if (gamepad1.right_bumper) {
            slides.setTargetPosition(null);
            double pwr = 0.9;
            slides.setPower(pwr);
        } else if(gamepad1.left_bumper) {
            slides.setTargetPosition(null);
            double pwr = -0.9;
            slides.setPower(pwr);
        } else if (gamepad1.x) { // Lift down
            setSlidePosition(0);
        } else if (gamepad1.y) {
            setSlidePosition(Slide.getMaxPosition() / 2);
        } else if (gamepad1.b) {
            setSlidePosition(Slide.getMaxPosition());
        } else if (slides.getTargetPosition() == null) {
            slides.stop();
        }
        updateButtons();
    }

    abstract void controlDrivetrain(double preferredSpeed);

    public void normalTeleOpActivities(){
        double speedVal = speed==Speed.FAST ? 0.75 : 0.3;
        controlDrivetrain(speedVal);

        if(gamepad1.left_trigger>0.1){
            intake.setPower(-gamepad1.left_trigger);
        }
        else if(gamepad1.right_trigger>0.1){
            intake.setPower(gamepad1.right_trigger);
        }
        else{
            intake.stop();
        }

        if(slowModeButton.isActive()){
            speed = Speed.SLOW;
        }
        else{
            speed = Speed.FAST;
        }

        if(dumpButton.isPressed() && slides.getCurrentPosition() >= SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD) {
            dumpState = DumpState.DUMPING;
        }
        switch (dumpState) {
            case DUMPING:
                carriageInterpolation.setTarget(Carriage.getDumpPosition());
                if (!carriageInterpolation.isBusy()) {
                    dumpState = DumpState.RETURNING_TO_IDLE;
                }
                break;
            case RETURNING_TO_IDLE:
                if (!carriageInterpolation.isBusy()) {
                    setSlidePosition(0);
                    dumpState = DumpState.IDLE;
                }
            case IDLE:
                carriageInterpolation.setTarget(Carriage.getIdlePosition());
        }
        carriage.setPosition(carriageInterpolation.getCurrent());

        if(gamepad1.dpad_right){
            carouselInterpolation.setTarget(0.375);
        }
        else if(gamepad1.dpad_left){
            carouselInterpolation.setTarget(-0.375);
        }
        else{
            carouselInterpolation.setTarget(0);
        }
        carousel.motor.setPower(carouselInterpolation.getCurrent());

        telemetry.addData("SLOW MODE",speed);
        telemetry.update();
    }
    public void updateButtons(){
        slowModeButton.update();
    }
}
