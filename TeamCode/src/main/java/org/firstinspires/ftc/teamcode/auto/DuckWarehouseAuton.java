package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeleOpLeagues;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Duck Murder 2: Electric Boogaloo
 */
@Config
public abstract class DuckWarehouseAuton extends BaseAutonomousOpMode {
    @Override
    public void run() {
        double colorMultiplier = getColorMultiplier();
        ShippingHubLevel level = performAnalysis();

        capstone.setPosition(CapstoneMechanism2.getIdle());
        telemetry.addData("Position", level);
        telemetry.update();

        TrajectorySequence sequence = duckDeliverySequence(level)
                .lineTo(new Vector2d(-6, -52 * colorMultiplier))
                .turn(Math.toRadians(-90) * colorMultiplier)
                .addTemporalMarker(() -> capstone.setPosition(CapstoneMechanism2.getIdle()))
                .lineTo(new Vector2d(12, -48 * colorMultiplier))
                .lineTo(new Vector2d(12, -72 * colorMultiplier))
                .forward(24)
                .waitSeconds(0.5)
                .addTemporalMarker(() -> claw.grab())
                .addTemporalMarker(() -> retraction.retract())
                .waitSeconds(0.5)
                .build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }
}
