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
    public static double HIGH_POS = -50.5;
    public static double MID_POS = -45;
    public static double LOW_POS = -45.25;
    public static double LOW_HORIZ_POS = -5;

    @Override
    public void run() {
        double colorMultiplier = getColorMultiplier();
        ShippingHubLevel level = performAnalysis();

        telemetry.addData("position",level);
        telemetry.update();
        double destinationY;
        if (level == ShippingHubLevel.HIGH) {
            destinationY = HIGH_POS;
        } else if (level == ShippingHubLevel.MID) {
            destinationY = MID_POS;
        } else {
            destinationY = LOW_POS;
        }
        capstone.setPosition(CapstoneMechanism2.getIdle());
        TrajectorySequence sequenceStart = drive.trajectorySequenceBuilder(new Pose2d(12, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(level == ShippingHubLevel.LOW ? LOW_HORIZ_POS : -11.5, destinationY * colorMultiplier, Math.toRadians(90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();

        drive.setPoseEstimate(sequenceStart.start());
        drive.followTrajectorySequence(sequenceStart);

        do {
            if (level == ShippingHubLevel.HIGH) {
                slide.runToPosition(Slide2.MAX_POSITION);
            } else if (level == ShippingHubLevel.MID) {
                slide.runToPosition((Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2);
            } else {
                slide.runToPosition(760);
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
                .lineTo(new Vector2d(-6, -46 * colorMultiplier))
                .turn(Math.toRadians(-90) * colorMultiplier)

                .lineTo(new Vector2d(15, -46 * colorMultiplier))
                .waitSeconds(10)
                .lineTo(new Vector2d(12, -72 * colorMultiplier))
                .forward(24)
                .waitSeconds(0.5)
                .build();

        drive.followTrajectorySequence(sequenceEnd);
    }
}
