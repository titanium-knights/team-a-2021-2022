package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeleOpLeagues;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
public abstract class DuckSpareAuton extends BaseAutonomousOpMode {
    @Override
    protected boolean grabsOnInit() {
        return true;
    }

    @Override
    public void run() {
        double colorMultiplier = getColorMultiplier();
        ShippingHubLevel level = performAnalysis();

        telemetry.addData("position",level);
        telemetry.update();

        TrajectorySequence sequence = spareDuckSequence(level)
                .lineTo(new Vector2d(-6, -46 * colorMultiplier))
                .turn(Math.toRadians(-90) * colorMultiplier)
                .addTemporalMarker(() -> capstone.setPosition(CapstoneMechanism2.getIdle()))
                .lineTo(new Vector2d(15, -46 * colorMultiplier))
                .waitSeconds(10)
                .lineTo(new Vector2d(12, -72 * colorMultiplier))
                .forward(24)
                .waitSeconds(0.5)
                .addTemporalMarker(() -> retraction.retract()) // DO NOT move the robot after this line
                .waitSeconds(0.5)
                .build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }
}
