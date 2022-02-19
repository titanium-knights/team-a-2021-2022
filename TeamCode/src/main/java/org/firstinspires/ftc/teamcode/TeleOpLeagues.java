package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.odometry.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.TubeIntake;

@TeleOp(name = "TeleOp Leagues", group = "TeleOp")
public class TeleOpLeagues extends OpMode {
    MecanumDrive drive;
    TubeIntake intake;
    OdometryMecanumDrive odoDrive;

    @Override
    public void init() {
        intake = new TubeIntake(hardwareMap);
        drive = new MecanumDrive(hardwareMap);
        odoDrive = new OdometryMecanumDrive(hardwareMap);
    }

    @Override
    public void loop() {
        drive.teleOpRobotCentric(gamepad1,0.5);
        if(gamepad1.right_trigger>0.3) {
            intake.setPower(gamepad1.right_trigger);
        } else if(gamepad1.left_trigger>0.3) {
            intake.setPower(-gamepad1.left_trigger);
        } else {
            intake.stop();
        }

        odoDrive.update();

        Pose2d poseEstimate = odoDrive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.update();
    }
}
