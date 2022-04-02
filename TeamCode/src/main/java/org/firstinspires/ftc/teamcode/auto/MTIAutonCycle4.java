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
@Autonomous(name="MTI Liney + Spliney")
public class MTIAutonCycle4 extends LinearOpMode {
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

    Pose2d pA = startPose;
    Pose2d pB = rightOfBlueHub;
    Pose2d pC = blueWarehouseIntermediate;
    Pose2d pD = blueWarehouse;

    protected void setupDevices() {
        rightOfBlueHub = new Pose2d(BLUE_HUB_X,BLUE_HUB_Y,Math.toRadians(BLUE_HUB_HEADING));

        rightOfBlueHubCycle1 = new Pose2d(BLUE_HUB_X+cycle_x_offset,BLUE_HUB_Y+cycle_y_offset, Math.toRadians(BLUE_HUB_HEADING));
        blueWarehouse = new Pose2d(48,WAREHOUSE_Y,Math.toRadians(0));
        blueWarehouseIntermediate = new Pose2d(8,WAREHOUSE_Y,Math.toRadians(0));

        pA = startPose;
        pB = rightOfBlueHub;
        pC = blueWarehouseIntermediate;
        pD = blueWarehouse;

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
        builder = drive.trajectorySequenceBuilder(pA)
                .addTemporalMarker(()->{
                    slidePos = Slide2.MAX_POSITION;
                })
                .lineToSplineHeading(pB)
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
                    .lineToSplineHeading(pC)
                    .lineToSplineHeading(pD)
                    .addTemporalMarker(()->{
                        slidePos = null;
                    })
                    .addTemporalMarker(()->{
//                        intake.setPower(-1);
                    })
                    .setReversed(true)
                    .splineToLinearHeading(pC,Math.toRadians(180))
                    .UNSTABLE_addTemporalMarkerOffset(UNINTAKE_DELAY,()->{
                        intake.setPower(-1);
                    })
                    .addTemporalMarker(()->{
                        intake.stop();
                        slidePos = Slide2.MAX_POSITION;
                    })

                    .lineToSplineHeading(pB)
                    .addTemporalMarker(()->{
                        currentPose = drive.getPoseEstimate();
                    })
                    .waitSeconds(0.5)
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
                .lineToSplineHeading(pC)
                .lineToSplineHeading(pD)
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
