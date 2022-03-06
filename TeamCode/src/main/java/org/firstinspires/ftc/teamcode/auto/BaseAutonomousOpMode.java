package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.*;

@Config public abstract class BaseAutonomousOpMode extends LinearOpMode {
    public enum Color {
        BLUE,
        RED
    }

    public enum ShippingHubLevel {
        LOW,
        MID,
        HIGH
    }

    public abstract Color getColor();

    public final void runOpMode() {
        setupDevices();
        waitForStart();
        run();
        retraction.retract();
    }

    protected OdometryMecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected Carriage carriage;
    protected CapstoneMechanism2 capstone;
    protected ClawIntake claw;
    protected Carousel carousel;
    protected OdometryRetraction retraction;
    protected CapstoneVision vision;

    protected void setupDevices() {
        drive = new OdometryMecanumDrive(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        slide = new Slide2(hardwareMap);
        carriage = new Carriage(hardwareMap);
        capstone = new CapstoneMechanism2(hardwareMap);
        claw = new ClawIntake(hardwareMap);
        carousel = new Carousel(hardwareMap);
        retraction = new OdometryRetraction(hardwareMap);
        retraction.extend();
        vision = new CapstoneVision(hardwareMap, telemetry);

        if (grabsOnInit()) claw.grab();
    }

    protected boolean grabsOnInit() {
        return false;
    }

    protected ShippingHubLevel performAnalysis() {
        vision.pipeline.updateTelemetry = false;
        switch (vision.getPosition()) {
            case 2:
                return ShippingHubLevel.HIGH;
            case 1:
                return ShippingHubLevel.MID;
            default:
                return ShippingHubLevel.LOW;
        }
    }

    protected abstract void run();

    protected final double getColorMultiplier() {
        return getColor() == Color.BLUE ? -1 : 1;
    }

    public static double HIGH_POS = -57;
    public static double MID_POS = -64;
    public static double LOW_POS = -57;

    public static int HIGH_CAPSTONE_POS = -1400;
    public static int MID_CAPSTONE_POS = -1840;
    public static int LOW_CAPSTONE_POS = -2150;

    public static double SPARE_SHIPPING_HUB_X = -20;
    public static double DUCK_SHIPPING_HUB_X = -10;
    public static double SPARE_Y_OFFSET = 8;

    public static double RED_OFFSET = -6;
    public static double RED_OFFSET_Y =2;

    protected void moveCapstoneMechanismForDumping(ShippingHubLevel level) {
        if (level == ShippingHubLevel.HIGH) {
            capstone.setPosition(HIGH_CAPSTONE_POS);
        } else if (level == ShippingHubLevel.MID) {
            capstone.setPosition(MID_CAPSTONE_POS);
        } else {
            capstone.setPosition(LOW_CAPSTONE_POS);
        }
    }

    protected Pose2d poseForDumping(double x, ShippingHubLevel level, double yOffset) {
        double destinationY;
        if (level == ShippingHubLevel.HIGH) {
            destinationY = HIGH_POS;
        } else if (level == ShippingHubLevel.MID) {
            destinationY = MID_POS;
        } else {
            destinationY = LOW_POS;
        }
        double y = ((destinationY + yOffset) * getColorMultiplier());
        return new Pose2d((x - (getColor() == Color.RED ? RED_OFFSET : 0)), (y - (getColor() == Color.RED ? RED_OFFSET_Y : 0)), Math.toRadians(90) * getColorMultiplier());
    };

    protected TrajectorySequenceBuilder spareDuckSequence(ShippingHubLevel level) {
        double colorMultiplier = getColorMultiplier();
        return drive.trajectorySequenceBuilder(new Pose2d(12, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .waitSeconds(0.5)
                .back(24)
                .addTemporalMarker(() -> moveCapstoneMechanismForDumping(level))
                .turn(Math.toRadians(180) * colorMultiplier)
                .setTangent(Math.toRadians(-90) * colorMultiplier)
                .splineToLinearHeading(poseForDumping(SPARE_SHIPPING_HUB_X, level, SPARE_Y_OFFSET), Math.toRadians(90) * colorMultiplier)
                .addTemporalMarker(() -> claw.release())
                .waitSeconds(1);
    }

    protected TrajectorySequenceBuilder duckDeliverySequence(ShippingHubLevel level) {
        double colorMultiplier = getColorMultiplier();
        return drive.trajectorySequenceBuilder(new Pose2d(-36, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .back(12)
                .lineToLinearHeading(new Pose2d(-61, -56 * colorMultiplier, Math.toRadians(180)))
                .addTemporalMarker(() -> carousel.spinReverse(false))
                .waitSeconds(4)
                .addTemporalMarker(carousel::stop)
                .waitSeconds(8)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-40, -56 * colorMultiplier),
                        Math.toRadians(30) * colorMultiplier)
                .addTemporalMarker(() -> moveCapstoneMechanismForDumping(level))
                .splineToConstantHeading(poseForDumping(DUCK_SHIPPING_HUB_X, level, 0).vec(), 0)
                .waitSeconds(1)
                .turn(Math.toRadians(-90) * colorMultiplier)
                .addTemporalMarker(() -> claw.release())
                .waitSeconds(0.5);
    }
}
