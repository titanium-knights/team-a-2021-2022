package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.TubeIntake;

@TeleOp(name = "TeleOp Leagues", group = "TeleOp")
public class TeleOpLeagues extends OpMode {
    MecanumDrive drive;
    TubeIntake intake;

    @Override
    public void init() {
        intake = new TubeIntake(hardwareMap);
        drive = new MecanumDrive(hardwareMap);
    }

    @Override
    public void loop() {
        drive.teleOpRobotCentric(gamepad1,0.5);
        if(gamepad1.right_trigger>0.3) {
            intake.setPower(gamepad1.right_trigger);
        }
        if(gamepad1.left_trigger>0.3) {
            intake.setPower(-gamepad1.left_trigger);
        }
    }
}
