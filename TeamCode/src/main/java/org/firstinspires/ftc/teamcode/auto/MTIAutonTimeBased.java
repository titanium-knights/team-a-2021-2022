package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.util.*;

import java.util.function.BooleanSupplier;

@Autonomous(name = "MTI Auton (Alt)")
@Config
public class MTIAutonTimeBased extends LinearOpMode {
    protected OdometryMecanumDrive odometryDrive;
    protected MecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected CarriageDC carriage;
    protected TapeMeasureMechanism tape;
    protected OdometryRetraction retraction;
    protected ElapsedTime elapsedTime;

    public static int CYCLES = 4;
    public static double TAPE_PITCH = 0.4;
    public static final Pose2d startPose = new Pose2d(-12, 63, Math.toRadians(90));
    public static double MOVEMENT_FACTOR = 0.025;
    public static double TURN_TIME = 0.43;
    public static double WAREHOUSE_DIST = 41;
    public static double WAREHOUSE_DELAY = 1.5;
    Integer slidePos = null;

    private void updateDevices() {
        odometryDrive.update();
        if (slidePos != null) {
            slide.runToPosition(slidePos, 0.9);
        }
    }

    private void waitSeconds(double seconds) {
        double start = elapsedTime.seconds();
        while (opModeIsActive() && !Thread.currentThread().isInterrupted() && elapsedTime.seconds() - start < seconds) {
            updateDevices();
        }
    }

    private void driveWhile(BooleanSupplier condition, double x, double y, double turn) {
        drive.move(x, y, turn);
        while (opModeIsActive() && !Thread.currentThread().isInterrupted() && condition.getAsBoolean()) {
            updateDevices();
        }
        drive.stop();
    }

    private void dumpAndLower() {
        carriage.dump();
        waitSeconds(0.8);
        carriage.idle();
        waitSeconds(0.2);
        slidePos = Slide2.MIN_POSITION;
    }

    @Override
    public void runOpMode() throws InterruptedException {
        tape = new TapeMeasureMechanism(hardwareMap);
        tape.setPitchPosition(TAPE_PITCH, true);
        odometryDrive = new OdometryMecanumDrive(hardwareMap);
        drive = new MecanumDrive(hardwareMap);
        slide = new Slide2(hardwareMap);
        carriage = new CarriageDC(hardwareMap);
        elapsedTime = new ElapsedTime();
        intake = new TubeIntake(hardwareMap);
        retraction = new OdometryRetraction(hardwareMap);
        retraction.extend();
        waitForStart();
        elapsedTime.reset();
        odometryDrive.setPoseEstimate(startPose);

        // Start moving the slides
        slidePos = Slide2.MAX_POSITION;

        // Go backwards towards the shipping hub, and perform our first dump
        drive.move(0, -1, 0);
        waitSeconds(13 * MOVEMENT_FACTOR);
        drive.stop();
        dumpAndLower();

        // Turn right to enter the shipping hub
        drive.move(0, 0.3, 1.0);
        waitSeconds(0.5);

        // Strafe left to ensure we're next to the wall
        drive.move(-1, 0, 0);
        waitSeconds(0.6);

        // Enter the warehouse
        drive.move(-0.1, 1, 0);
        waitSeconds(18 * MOVEMENT_FACTOR);

        // Begin cycling
        for (int i = 1; i < CYCLES; i++) {
            // Start intaking as we continue to move in
            slidePos = null;
            intake.setPower(1);
            waitSeconds(WAREHOUSE_DIST * MOVEMENT_FACTOR);

            // Stop the drive and wait for a bit
            drive.stop();
            waitSeconds(WAREHOUSE_DELAY);

            // Start moving backwards
            drive.move(-0.1, -1, 0);
            waitSeconds((WAREHOUSE_DIST - 12) * MOVEMENT_FACTOR);

            // Move the intake in reverse
            intake.setPower(-0.5);
            waitSeconds(18 * MOVEMENT_FACTOR);

            // Shut off the intake, start moving the arm to dump, strafe right, then dump
            slidePos = Slide2.MAX_POSITION;
            drive.move(1, 0, 0);
            waitSeconds(21 * MOVEMENT_FACTOR);
            drive.move(0, 0, -1);
            waitSeconds(TURN_TIME);
            intake.stop();
            drive.stop();
            dumpAndLower();
            drive.move(0, 0, 1);
            waitSeconds(TURN_TIME);

            // Head back to the warehouse
            drive.move(-1, 0, 0);
            waitSeconds(21 * MOVEMENT_FACTOR + 0.5);
            drive.move(-0.1, 1, 0);
            waitSeconds(18 * MOVEMENT_FACTOR);
        }

        // Shut off the slide and park
        slidePos = null;
        drive.move(-0.1, 1, 0);
        waitSeconds(36 * MOVEMENT_FACTOR);
        drive.stop();

        retraction.retract();
    }
}
