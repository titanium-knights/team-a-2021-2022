package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.*;

@TeleOp(name = "TeleOpMode", group = "Tele-Op")
public class TeleOpMode extends OpMode {
    MecanumDrive drive;
    Carousel carousel;
    Slide slides;
    TubeIntake intake;
    Carriage carriage;
    IMU imu;
    Speed speed;
    PushButton btB,btYSlowMode;
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
        btB = new PushButton(() -> gamepad1.b);
        btYSlowMode = new PushButton(() -> gamepad1.y);
    }

    @Override
    public void loop() {
        double speedVal = speed==Speed.FAST ? 0.75 : 0.3;
        drive.teleOpRobotCentric(gamepad1,speedVal);
        if(gamepad1.x){
            intake.reverse();
        }
        else if(btB.isPressed()){
            intake.spin();
        }
        else{
            intake.stop();
        }


        if(btYSlowMode.isPressed()){
            speed = Speed.SLOW;
        }
        else{
            speed = Speed.FAST;
        }

        if(gamepad1.left_trigger>0.1){
            double pwr = slides.getSafePower(-gamepad1.left_trigger);
            slides.setPower(pwr);
        }
        else{
            slides.stop();
        }

        if(gamepad1.right_trigger>0.1){
            double pwr = slides.getSafePower(gamepad1.right_trigger);
            slides.setPower(pwr);
        }
        else{
            slides.stop();
        }
        if(gamepad1.right_bumper){
            carriage.dump();
        }
        else if(gamepad1.left_bumper){
            carriage.idle();
        }

        btB.update();
        btYSlowMode.update();
    }
}
