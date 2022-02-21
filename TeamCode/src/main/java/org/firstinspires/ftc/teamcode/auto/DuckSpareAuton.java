package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

@Autonomous(name = "Spare Duck & Warehouse")
public class DuckSpareAuton extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        double colorMultiplier = -1; // TODO: Change to -1 for blue
        OdometryMecanumDrive drive = new OdometryMecanumDrive(hardwareMap);

        waitForStart();
        TrajectorySequence sequence = drive.trajectorySequenceBuilder(new Pose2d(12, -66 * colorMultiplier, 0))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(-12, -41 * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)

                .waitSeconds(0.5)
                .forward(3)
                .turn(Math.toRadians(90) * colorMultiplier)

                .lineTo(new Vector2d(15, -44 * colorMultiplier))
                .build();
        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }
}
