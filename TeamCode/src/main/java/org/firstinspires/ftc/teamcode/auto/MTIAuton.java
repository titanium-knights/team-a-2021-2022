package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.*;

public class MTIAuton extends LinearOpMode {
    TrajectorySequence a;
    protected Carousel carousel;
    protected OdometryMecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected Carriage carriage;
    protected TapeMeasureMechanism tape;
    protected OdometryRetraction retraction;

    public static int CYCLES = 2;
    public static double WAREHOUSE_Y_POSE_ESTIMATE = 66.5;
    public static double WAREHOUSE_Y = 68;
    public static double BLUE_HUB_X = -6;
    public static double BLUE_HUB_Y = 52;
    public static double BLUE_HUB_HEADING = 80;
    public static double TAPE_PITCH = 0.4;
    public static Pose2d rightOfBlueHub = new Pose2d(BLUE_HUB_X,BLUE_HUB_Y,Math.toRadians(BLUE_HUB_HEADING));
    public static final Pose2d startPose = new Pose2d(-12, 63,Math.toRadians(90));

    public static Pose2d blueWarehouseIntermediate = new Pose2d(8,WAREHOUSE_Y,Math.toRadians(0));
    public static Pose2d blueWarehouse = new Pose2d(48,WAREHOUSE_Y,Math.toRadians(0));
    Integer slidePos = 0;

    public void initalizeTrajs(){
        TrajectorySequenceBuilder builder;
        builder = drive.trajectorySequenceBuilder(startPose)
                .addTemporalMarker(()->{
                    slidePos = Slide2.MAX_POSITION;
                })
                .lineToSplineHeading(rightOfBlueHub)
                .addTemporalMarker(()->{
                    carriage.dump();
                })
                .waitSeconds(1.5)
                .addTemporalMarker(()->{
                    carriage.idle();

                })
                //may want to add wait seconds here
                .addTemporalMarker(()->{
                    slidePos = Slide2.MIN_POSITION;
                });
        for(int i = 0; i < CYCLES; i++){
            builder = builder.setReversed(false)
                    .addTemporalMarker(()->{
                        intake.setPower(1);
                    })
                    .splineToSplineHeading(blueWarehouseIntermediate,Math.toRadians(0))
                    .splineToSplineHeading(blueWarehouse,Math.toRadians(0))
                    .addTemporalMarker(()->{
                        slidePos = null;
                    })
                    .addTemporalMarker(()->{
                        intake.setPower(-1);
                    })
                    .setReversed(true)
                    .splineToLinearHeading(blueWarehouseIntermediate,Math.toRadians(180))
                    .addTemporalMarker(()->{
                        intake.stop();
                        slidePos = Slide2.MAX_POSITION;
                    })
                    .splineToSplineHeading(rightOfBlueHub,-rightOfBlueHub.getHeading())
                    .waitSeconds(0.5)
                    .addTemporalMarker(()->{
                        carriage.dump();
                    })
                    .waitSeconds(1.5)
                    .addTemporalMarker(() -> {
                        carriage.idle();
                    })
                    .waitSeconds(0.5)
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
