package org.firstinspires.ftc.teamcode.odometry.opmode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDriveIMU;
import org.firstinspires.ftc.teamcode.odometry.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.odometry.StandardTrackingWheelLocalizerIMU;

/**
 * This is a simple teleop routine for testing localization. Drive the robot around like a normal
 * teleop routine and make sure the robot's estimated pose matches the robot's actual pose (slight
 * errors are not out of the ordinary, especially with sudden drive motions). The goal of this
 * exercise is to ascertain whether the localizer has been configured properly (note: the pure
 * encoder localizer heading may be significantly off if the track width has not been tuned).
 */
@TeleOp(group = "Test")
public class LocalizationTestIMU extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        OdometryMecanumDriveIMU drive = new OdometryMecanumDriveIMU(hardwareMap);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        waitForStart();

        while (!isStopRequested()) {
            drive.setWeightedDrivePower(
                    new Pose2d(
                            -gamepad1.left_stick_y,
                            -gamepad1.left_stick_x,
                            -gamepad1.right_stick_x
                    )
            );

            drive.update();

            Pose2d poseEstimate = drive.getPoseEstimate();
            telemetry.addData("x", poseEstimate.getX());
            telemetry.addData("y", poseEstimate.getY());
            telemetry.addData("heading", poseEstimate.getHeading());
            telemetry.addData("left pos", ((StandardTrackingWheelLocalizerIMU)drive.getLocalizer()).getWheelPositions().get(0));
            telemetry.addData("right pos", ((StandardTrackingWheelLocalizerIMU)drive.getLocalizer()).getWheelPositions().get(2));
            telemetry.addData("front pos", ((StandardTrackingWheelLocalizerIMU)drive.getLocalizer()).getWheelPositions().get(2));
            telemetry.update();
        }
    }
}