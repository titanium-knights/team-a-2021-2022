package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.util.*;

public abstract class TeleOpMode extends OpMode {
    public enum State {
        PRESET_MODE,
        NOT_PRESET_MODE
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
    State state = State.NOT_PRESET_MODE;
    boolean isDumping = false;
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

    @Override
    public void loop() {
        normalTeleOpActivities();
        switch (state) {
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
                    slides.setPower(0.8);
                }
                else if(gamepad1.left_bumper){
                    slides.setTargetPosition(0);
                    slides.setPower(-0.8);
                }
                break;
        }
        if(btAPresetButton.isPressed()){
            if (state == State.PRESET_MODE) {
                state = State.NOT_PRESET_MODE;
                slides.clearTargetPosition();
                slides.stop();
            } else {
                state = State.PRESET_MODE;
            }
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
            isDumping = true;
        }
        if (isDumping) {
            carriageInterpolation.setTarget(Carriage.getDumpPosition());
            if (!carriageInterpolation.isBusy()) {
                isDumping = false;
            }
        } else {
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
        telemetry.addData("Preset Mode", state == State.PRESET_MODE);
        telemetry.update();


    }
    public void updateButtons(){
        btYSlowMode.update();
        btAPresetButton.update();
    }
}
