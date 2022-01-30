package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.*;

public abstract class TeleOpMode extends OpMode {
    public enum SlideState {
        PRESET_MODE,
        NOT_PRESET_MODE
    }

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
    ToggleButton btYSlowMode;
    PushButton btAPresetButton;
    PushButton btBDumpButton;
    SlideState slideState = SlideState.NOT_PRESET_MODE;
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
        btYSlowMode = new ToggleButton(() -> gamepad1.y);
        btAPresetButton = new PushButton(() -> gamepad1.a);
        btBDumpButton = new PushButton(() -> gamepad1.b);
    }

    public void setSlidePosition(int position) {
        assert slideState == SlideState.PRESET_MODE;
        slides.setTargetPosition(position);
        slides.setPower(0.8);
    }

    @Override
    public void loop() {
        normalTeleOpActivities();
        switch (slideState) {
            case NOT_PRESET_MODE:
                if(gamepad1.right_bumper){
                    double pwr = slides.getSafePower(0.9);
                    slides.setPower(pwr);
                }
                else if(gamepad1.left_bumper){
                    double pwr = slides.getSafePower(-0.9);
                    slides.setPower(pwr);
                }
                else{
                    slides.stop();
                }
                break;
            case PRESET_MODE:
                if(gamepad1.right_bumper){
                    slides.setTargetPosition(Slide.getMaxPosition());
                    setSlidePosition(Slide.getMaxPosition());
                }
                else if(gamepad1.left_bumper){
                    setSlidePosition(0);
                }
                break;
        }
        if(btAPresetButton.isPressed()){
            if (slideState == SlideState.PRESET_MODE) {
                slideState = SlideState.NOT_PRESET_MODE;
                slides.clearTargetPosition();
            } else {
                slideState = SlideState.PRESET_MODE;
            }
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

        if(btYSlowMode.isActive()){
            speed = Speed.SLOW;
        }
        else{
            speed = Speed.FAST;
        }

        if(btBDumpButton.isPressed() && slides.getCurrentPosition() >= SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD) {
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
                    if (slideState == SlideState.PRESET_MODE) setSlidePosition(0);
                    dumpState = DumpState.IDLE;
                }
            case IDLE:
                carriageInterpolation.setTarget(Carriage.getIdlePosition());
        }
        carriage.setPosition(carriageInterpolation.getCurrent());

        if(gamepad1.dpad_left){
            carouselInterpolation.setTarget(0.375);
        }
        else if(gamepad1.dpad_right){
            carouselInterpolation.setTarget(-0.375);
        }
        else{
            carouselInterpolation.setTarget(0);
        }
        carousel.motor.setPower(carouselInterpolation.getCurrent());

        telemetry.addData("SLOW MODE",speed);
        telemetry.addData("Preset Mode", slideState == SlideState.PRESET_MODE);
        telemetry.update();
    }
    public void updateButtons(){
        btYSlowMode.update();
        btAPresetButton.update();
    }
}
