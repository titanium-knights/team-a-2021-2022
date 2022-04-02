package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.*;

@Autonomous(name = "Murder Cycle Auton")
@Config
public class MurderCycleAuton extends LinearOpMode {
    protected Carousel carousel;
    protected OdometryMecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected CarriageDC carriage;
    protected TapeMeasureMechanism tape;
    protected OdometryRetraction retraction;

    public static int CYCLES = 2;
    public static double WAREHOUSE_Y = 68;
    public static double BLUE_HUB_X = -11;
    public static double BLUE_HUB_Y = 53;
    public static double BLUE_HUB_HEADING = 90;
    public static double TAPE_PITCH = 0.4;
    public static boolean enableTerribleHackSeriouslyWeShouldNotBeDoingThis = false;

    Integer slidePos = null;

    protected void setupDevices() {
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

    Double pendingYChange = null;

    @Override
    public void runOpMode() throws InterruptedException {
        setupDevices();

        Pose2d rightOfBlueHub = new Pose2d(BLUE_HUB_X,BLUE_HUB_Y,Math.toRadians(BLUE_HUB_HEADING));
        Pose2d blueWarehouse = new Pose2d(52,WAREHOUSE_Y,Math.toRadians(0));
        Pose2d blueWarehouseIntermediate = new Pose2d(8,WAREHOUSE_Y,Math.toRadians(0));
        Pose2d carouselPos = new Pose2d(-58, 60,Math.toRadians(180));
        Pose2d startingPosition = new Pose2d(-36,63, Math.toRadians(90));

        TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(startingPosition)
                .setReversed(true)
                .back(5)
                .addTemporalMarker(() -> carousel.spinReverse(true))
                .splineToSplineHeading(carouselPos, Math.toRadians(180))
                .waitSeconds(1.9)
                .addTemporalMarker(carousel::stop)
                .setTangent(0)
                .addTemporalMarker(() -> {
                    slidePos = Slide2.MAX_POSITION;
                })
                .lineToSplineHeading(rightOfBlueHub)
                .addTemporalMarker(() -> {
                    carriage.dump();
                })
                .waitSeconds(1.0)
                .addTemporalMarker(() -> {
                    carriage.idle();
                })
                .waitSeconds(0.5)
                .addTemporalMarker(() -> {
                    slidePos = Slide2.MIN_POSITION;
                });

                for(int i = 0; i < CYCLES; i++){
                    builder = builder.setReversed(false)
                            .splineToSplineHeading(blueWarehouseIntermediate, Math.toRadians(0))
                            .addTemporalMarker(() -> {
                                intake.setPower(0.75);
                            })
                            .splineToSplineHeading(blueWarehouse,0)
                            .addTemporalMarker(()->{
                                slidePos = null;
                            })
                            .waitSeconds(0.25)
                            .setReversed(true)
                            .addTemporalMarker(() -> {
                                pendingYChange = 62.0;
                            })
                            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                                pendingYChange = 66.0;
                            })
                            .splineToLinearHeading(blueWarehouseIntermediate,Math.toRadians(180))
                            .addTemporalMarker(() -> {
                                intake.setPower(-1.0);
                                slidePos = Slide2.MAX_POSITION;
                            })
                            .UNSTABLE_addTemporalMarkerOffset(0.5, () -> {
                                intake.stop();
                            })
                            .splineToSplineHeading(rightOfBlueHub, -rightOfBlueHub.getHeading())
                            .UNSTABLE_addTemporalMarkerOffset(0.1, () -> {
                                carriage.dump();
                            })
                            .waitSeconds(1.0)
                            .addTemporalMarker(() -> {
                                carriage.idle();
                            })
                            .waitSeconds(0.2)
                            .addTemporalMarker(() -> {
                                slidePos = Slide2.MIN_POSITION;
                            });
                }

                builder = builder.setReversed(false)
                        .splineToSplineHeading(blueWarehouseIntermediate, Math.toRadians(0))
                        .splineToSplineHeading(blueWarehouse,0)
                        .setReversed(true);
        TrajectorySequence sequence = builder.build();

        waitForStart();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequenceAsync(sequence);

        while (opModeIsActive() && !Thread.currentThread().isInterrupted() && drive.isBusy()) {
            drive.update();
            if (slidePos != null) {
                slide.runToPosition(slidePos, 0.85);
            }
            if (pendingYChange != null && enableTerribleHackSeriouslyWeShouldNotBeDoingThis) {
                Pose2d pose = drive.getPoseEstimate();
                drive.setPoseEstimate(new Pose2d(pose.getX(), pendingYChange, pose.getHeading()));
                pendingYChange = null;
            }
        }




        // retraction.retract();
    }
}
