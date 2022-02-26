package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.*;

@TeleOp(name = "Reversed Controls", group = "Tele-Op")
@Disabled
public class ReverseIntakeAndTriggerControls extends OpMode {
    Slide slides;
    TubeIntake intake;
    @Override
    public void init() {
        slides = new Slide(hardwareMap);
        slides.stopAndResetEncoder();
        intake = new TubeIntake(hardwareMap);
    }

    public static int SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = (Slide.getMinPosition() + Slide.getMaxPosition()) / 3;

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
