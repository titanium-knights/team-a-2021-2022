package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Murder Duck & StorageUnit")
public class DuckStorageUnitAuton extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        OdometryMecanumDrive drive = new OdometryMecanumDrive(hardwareMap);

        waitForStart();

        TrajectorySequence sequence = drive.trajectorySequenceBuilder(new Pose2d(-36, -66, 0))
                .lineTo(new Vector2d(-56, -66))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90))
                .splineToConstantHeading(new Vector2d(-54, -60), Math.toRadians(90))
                .turn(Math.toRadians(90))
                .forward(36)
                .splineToSplineHeading(new Pose2d(-31, -20, Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(135))
                .splineToSplineHeading(new Pose2d(-60, -35, Math.toRadians(0)), Math.toRadians(-90))
                .build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }

}
