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
    public static int X_POSITION = -61;
    public static int Y_POSITION = -56;

    @Override
    public void run() {
        double colorMultiplier = getColorMultiplier();
        ShippingHubLevel position = performAnalysis();

        double destinationY;
        if (position == ShippingHubLevel.HIGH) {
            destinationY = DuckSpareAuton.HIGH_POS;
        } else if (position == ShippingHubLevel.MID) {
            destinationY = DuckSpareAuton.MID_POS - 2.5;
        } else {
            destinationY = DuckSpareAuton.LOW_POS - 2.5;
        }
        capstone.setPosition(CapstoneMechanism2.getIdle());
        telemetry.addData("Position", position);
        telemetry.update();

        TrajectorySequence sequenceStart = drive.trajectorySequenceBuilder(new Pose2d(-36, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .back(12)
                .lineToLinearHeading(new Pose2d(X_POSITION, Y_POSITION * colorMultiplier, Math.toRadians(180)))
                .addTemporalMarker(() -> carousel.spinReverse(false))
                .waitSeconds(4)
                .addTemporalMarker(carousel::stop)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-40, -56 * colorMultiplier),
                        Math.toRadians(30) * colorMultiplier)
                .splineToSplineHeading(new Pose2d(position == ShippingHubLevel.LOW ? -21.5 : -14.5, destinationY * colorMultiplier, Math.toRadians(90) * colorMultiplier),
                        Math.toRadians(90) * colorMultiplier)
                .build();

        drive.setPoseEstimate(sequenceStart.start());
        drive.followTrajectorySequence(sequenceStart);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);

        do {
            if (position == ShippingHubLevel.HIGH) {
                slide.runToPosition(Slide2.MAX_POSITION);
            } else {
                slide.runToPosition((Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2);
            }
        } while (opModeIsActive() && slide.getPower() > 0.0);

        carriage.dump();
        sleep(2000);
        carriage.idle();
        sleep(2000);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);

        capstone.setPosition(CapstoneMechanism2.getIdle());

        TrajectorySequence sequenceEnd = drive.trajectorySequenceBuilder(sequenceStart.end())
                .lineTo(new Vector2d(-6, -52 * colorMultiplier))
                .turn(Math.toRadians(90) * colorMultiplier)

                .lineTo(new Vector2d(12, -48 * colorMultiplier))
                .lineTo(new Vector2d(12, -72 * colorMultiplier))
                .forward(24)
                .waitSeconds(0.5)
                .build();

        drive.followTrajectorySequence(sequenceEnd);
    }
}
