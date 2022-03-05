package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeleOpLeagues;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.util.*;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public abstract class DuckStorageUnitAuton extends BaseAutonomousOpMode {
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
            destinationY = DuckSpareAuton.LOW_POS - 4;
        }
        capstone.setPosition(CapstoneMechanism2.getIdle());
        telemetry.addData("Position", position);
        telemetry.update();

        TrajectorySequence sequenceStart = drive.trajectorySequenceBuilder(new Pose2d(-36, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .back(12)
                .lineToLinearHeading(new Pose2d(DuckWarehouseAuton.X_POSITION, DuckWarehouseAuton.Y_POSITION * colorMultiplier, Math.toRadians(180)))
                .addTemporalMarker(() -> carousel.spinReverse(false))
                .waitSeconds(4)
                .addTemporalMarker(carousel::stop)
                .waitSeconds(10)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-40, -56 * colorMultiplier),
                        Math.toRadians(30) * colorMultiplier)
                .splineToSplineHeading(new Pose2d(position == ShippingHubLevel.LOW ? -20.5 : -14.5, destinationY * colorMultiplier, Math.toRadians(90) * colorMultiplier),
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
                .splineToConstantHeading(new Vector2d(-36, -60 * colorMultiplier), Math.toRadians(180))
                .splineToConstantHeading(new Vector2d(-73, -45 * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .waitSeconds(1)
                .build();

        drive.followTrajectorySequence(sequenceEnd);
    }
}
