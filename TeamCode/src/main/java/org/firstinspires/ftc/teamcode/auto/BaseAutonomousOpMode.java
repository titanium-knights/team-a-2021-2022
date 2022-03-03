package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.util.*;

public abstract class BaseAutonomousOpMode extends LinearOpMode {
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
        setup();
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
    protected OdometryRetraction retraction;
    protected CapstoneVision vision;

    protected void setup() {
        drive = new OdometryMecanumDrive(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        slide = new Slide2(hardwareMap);
        carriage = new Carriage(hardwareMap);
        capstone = new CapstoneMechanism2(hardwareMap);
        claw = new ClawIntake(hardwareMap);
        retraction = new OdometryRetraction(hardwareMap);
        retraction.extend();
        vision = new CapstoneVision(hardwareMap, telemetry);
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
}
