package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.*;

@TeleOp(name = "TeleOpMode W/ Preset", group = "Tele-Op")
public class TeleOpModePreset extends OpMode {
    public enum State {
        PRESET_MODE,
        NOT_PRESET_MODE
    }
    public enum PRESETMODE{
        NOT_PRESET_MODE,
        LIFT,
        LOWER
    }
    MecanumDrive drive;
    Carousel carousel;
    Slide slides;
    TubeIntake intake;
    Carriage carriage;
    IMU imu;
    Speed speed;
    ToggleButton btB, btYSlowMode,btAPresetToggle;
    State state = State.NOT_PRESET_MODE;
    public static int SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = (Slide.getMinPosition() + Slide.getMaxPosition()) / 3;

    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        carousel = new Carousel(hardwareMap);
        slides = new Slide(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        carriage = new Carriage(hardwareMap);
        imu = new IMU(hardwareMap);
        imu.initializeIMU();
        speed = Speed.FAST;
        btB = new ToggleButton(() -> gamepad1.b);
        btYSlowMode = new ToggleButton(() -> gamepad1.y);
        btAPresetToggle = new ToggleButton(() -> gamepad1.a);
    }

    @Override
    public void loop() {
        normalTeleOpActivities();
        switch (state) {
            case NOT_PRESET_MODE:
                if(gamepad1.left_trigger>0.1){
                    double pwr = slides.getSafePower(-gamepad1.left_trigger);
                    slides.setPower(pwr);
                }
                else if(gamepad1.right_trigger>0.1){
                    double pwr = slides.getSafePower(gamepad1.right_trigger);
                    slides.setPower(pwr);
                }
                else{
                    slides.stop();
                }
                break;
            case PRESET_MODE:
                if(gamepad1.dpad_up){

                }
                break;
        }
        if(btAPresetToggle.isActive()){
            state = State.PRESET_MODE;
        }
        else{
            state = State.NOT_PRESET_MODE;
        }
        updateButtons();
    }
    public void normalTeleOpActivities(){
        double speedVal = speed==Speed.FAST ? 0.75 : 0.3;
        drive.teleOpRobotCentric(gamepad1,speedVal);
        if(gamepad1.right_bumper){
            intake.spin();
        }
        else if(gamepad1.left_bumper){
            intake.reverse();
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

        if(gamepad1.left_trigger>0.1){
            double pwr = slides.getSafePower(-gamepad1.left_trigger);
            slides.setPower(pwr);
        }
        else if(gamepad1.right_trigger>0.1){
            double pwr = slides.getSafePower(gamepad1.right_trigger);
            slides.setPower(pwr);
        }
        else{
            slides.stop();
        }
        if(gamepad1.b && slides.getCurrentPosition() >= SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD){
            carriage.dump();
        }
        else if(gamepad1.x){
            carriage.idle();
        }

        if(gamepad1.dpad_left){
            carousel.spinReverse(true);
        }
        else if(gamepad1.dpad_right){
            carousel.spin(true);
        }
        else{
            carousel.stop();
        }
        btYSlowMode.update();
        telemetry.addData("SLOW MODE",speed);
        telemetry.update();


    }
    public void updateButtons(){
        btB.update();
        btYSlowMode.update();
        btAPresetToggle.update();
    }
}