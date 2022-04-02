package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.*;

@Config
@Autonomous(name="MTI Blocky")
public class MTIAutonCycle2 extends LinearOpMode {
    TrajectorySequence a;
    protected Carousel carousel;
    protected OdometryMecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected CarriageDC carriage;
    protected TapeMeasureMechanism tape;
    protected OdometryRetraction retraction;
    public static double DUMP_TIME = 0.1;
    public static int CYCLES = 2;
    public static double UNINTAKE_DELAY = -0.75;
    public static double WAREHOUSE_Y = 68;
    public static double BLUE_HUB_X = -11;
    //    public static double BLUE_HUB_Y = 53;
    public static double BLUE_HUB_Y = 50;

    public static double BLUE_HUB_HEADING = 90;
    public static double TAPE_PITCH = 0.4;
    public static Pose2d rightOfBlueHub = new Pose2d(BLUE_HUB_X,BLUE_HUB_Y,Math.toRadians(BLUE_HUB_HEADING));
    public static double cycle_y_offset=5;
    public static double cycle_x_offset=0;
    public static Pose2d rightOfBlueHubCycle1 = new Pose2d(BLUE_HUB_X+cycle_x_offset,BLUE_HUB_Y+cycle_y_offset, Math.toRadians(BLUE_HUB_HEADING));
    public static final Pose2d startPose = new Pose2d(-12, 63,Math.toRadians(90));

    public static Pose2d blueWarehouseIntermediate = new Pose2d(8,WAREHOUSE_Y,Math.toRadians(0));
    public static Pose2d blueWarehouse = new Pose2d(48,WAREHOUSE_Y,Math.toRadians(0));
    Pose2d currentPose = startPose;
    Integer slidePos = 0;
    protected void setupDevices() {
        rightOfBlueHub = new Pose2d(BLUE_HUB_X,BLUE_HUB_Y,Math.toRadians(BLUE_HUB_HEADING));
        rightOfBlueHubCycle1 = new Pose2d(BLUE_HUB_X+cycle_x_offset,BLUE_HUB_Y+cycle_y_offset, Math.toRadians(BLUE_HUB_HEADING));
        blueWarehouse = new Pose2d(48,WAREHOUSE_Y,Math.toRadians(0));
        blueWarehouseIntermediate = new Pose2d(8,WAREHOUSE_Y,Math.toRadians(0));

        drive = new OdometryMecanumDrive(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        slide = new Slide2(hardwareMap);
        carriage = new CarriageDC(hardwareMap);
        carousel = new Carousel(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        tape = new TapeMeasureMechanism(hardwareMap);
        tape.setPitchPosition(TAPE_PITCH, true);
        retraction = new OdometryRetraction(hardwareMap);
        retraction.extend();
    }
    public void initalizeTrajs(){
        TrajectorySequenceBuilder builder;
        builder = drive.trajectorySequenceBuilder(startPose)
                .addTemporalMarker(()->{
                    slidePos = Slide2.MAX_POSITION;
                })
                .back(13)
                .addTemporalMarker(()->{
                    carriage.dump();
                })
                .waitSeconds(1)
                .addTemporalMarker(()->{
                    carriage.idle();

                })
                .waitSeconds(DUMP_TIME)
                //may want to add wait seconds here
                .addTemporalMarker(()->{
                    slidePos = Slide2.MIN_POSITION;
                });

        for(int i = 0; i < CYCLES; i++){
            builder = builder.setReversed(false)
                    .addTemporalMarker(()->{
                        intake.setPower(1);
                    })
                    .forward(13)
                    .turn(-Math.toRadians(90))
                    .forward(60)
                    .addTemporalMarker(()->{
                        slidePos = null;
                    })
                    .back(20)
                    .addTemporalMarker(()->{
                        intake.setPower(-1);
                    })
                    .back(20)
                    .addTemporalMarker(()->{
                        intake.stop();
                        slidePos = Slide2.MAX_POSITION;
                    })
                    .turn(Math.toRadians(90))
                    .back(13)
                    .addTemporalMarker(()->{
                        carriage.dump();
                    })
                    .waitSeconds(1)
                    .addTemporalMarker(() -> {
                        carriage.idle();
                    })
                    .waitSeconds(DUMP_TIME)
                    .addTemporalMarker(() -> {
                        slidePos = Slide2.MIN_POSITION;
                    });

        }
        builder = builder.setReversed(false)
                .splineToSplineHeading(blueWarehouseIntermediate, Math.toRadians(0))
                .splineToSplineHeading(blueWarehouse,0)
                .setReversed(true);
        a = builder.build();
    }
    @Override
    public void runOpMode() throws InterruptedException {
        setupDevices();
        drive = new OdometryMecanumDrive(hardwareMap);
        initalizeTrajs();
        waitForStart();

        drive.setPoseEstimate(a.start());
        drive.followTrajectorySequenceAsync(a);
        while (opModeIsActive() && !Thread.currentThread().isInterrupted() && drive.isBusy()) {
            drive.update();
            if (slidePos != null) {
                slide.runToPosition(slidePos, 0.85);
            }
        }
    }
}
