package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.*;

@TeleOp(name = "Reversed Controls", group = "Tele-Op")
public class ReverseIntakeAndTriggerControls extends OpMode {
    Slide2 slides;
    TubeIntake intake;
    @Override
    public void init() {
        slides = new Slide2(hardwareMap);
        slides.stopAndResetEncoder();
        intake = new TubeIntake(hardwareMap);
    }

    public static int SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = (Slide2.getMinPosition() + Slide2.getMaxPosition()) / 3;

    @Override
    public void loop() {
        if(gamepad1.right_bumper){
            double pwr = slides.getSafePower(1.0);
            slides.setPower(pwr);
        }
        else if(gamepad1.left_bumper){
            double pwr = slides.getSafePower(-1.0);
            slides.setPower(pwr);
        }
        else{
            slides.stop();
        }

        if(gamepad1.left_trigger>0.1){
            intake.setPower(-gamepad1.left_trigger);
        }
        else if(gamepad1.right_trigger>0.1){
            intake.setPower(gamepad1.right_trigger);
        }
        else{
            intake.stop();
        }


    }
}
