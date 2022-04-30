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
@Autonomous(name="Optimized MTI Liney + Spliney")
public class MTIAutonCycle5 extends LinearOpMode {
    TrajectorySequence a;
    protected Carousel carousel;
    protected OdometryMecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected CarriageDC carriage;
    protected TapeMeasureMechanism tape;
    protected OdometryRetraction retraction;
    public static double DUMP_TIME = 0.25;
    public static int CYCLES = 3;
    public static double UNINTAKE_DELAY = -0.75;
    public static double WAREHOUSE_X = 44;
    public static double WAREHOUSE_Y = 65;
    public static double BLUE_HUB_X = -11;
    //    public static double BLUE_HUB_Y = 53;
    public static double BLUE_HUB_Y = 50;
    public static double dumpWaitTime = .8;

    public static double BLUE_HUB_HEADING = 90;
    public static double TAPE_PITCH = 0.4;
    public static Pose2d rightOfBlueHub;
    public static double cycle_y_offset=5;
    public static double cycle_x_offset=0;
    public static double cycle_heading_offset = -8;
    public static Pose2d rightOfBlueHubCycle1;
    public static Pose2d rightOfBlueHubCycle2;
    public static final Pose2d startPose = new Pose2d(-12, 63,Math.toRadians(90));

    public static Pose2d rightOfBlueCycle1;
    public static double rightOfBlueCycle1_X = 0;
    public static double rightOfBlueCycle1_Y = 47;
    public static double rightOfBlueCycle1_HEADING = 45;

    public static Pose2d rightOfBlueCycle2;
    public static double rightOfBlueCycle2_X= 0;
    public static double rightOfBlueCycle2_Y = 47;
    public static double rightOfBlueCycle2_HEADING = 45;

    public static double intermediate_x_offset = 0;
    public static double intermediate_y_offset = 1.2;
    public static double intermediate_x = 12;

    public static Pose2d blueWarehouseIntermediate;
    public static Pose2d blueWarehouse;
    public static Pose2d pB1,pB2,pD2,pD3;
    public static double WAREHOUSE_X_OFFSET_CYCLE_2 = 3 ;
    private MecanumDrive drive2;
    public static double WAREHOUSE_Y_OFFSET_CYCLE_2 = 3;
    Pose2d currentPose = startPose;
    Integer slidePos = 0;

    Pose2d pA = startPose;
    Pose2d pB = rightOfBlueHub;
    Pose2d pC1 = blueWarehouseIntermediate;
    Pose2d pC2;
    Pose2d pD = blueWarehouse;
    Pose2d pB3;
//    Pose2d pD;

    public static double dump_hack_bca = -1;
    public static double dump_hack_bca2 = -0.5;
    Pose2d pB_optimized1 = rightOfBlueCycle1;
    Pose2d pB_optimized2 = rightOfBlueCycle2;

    public static double CARRIAGE_DELAY = 0.5;
    protected void setupDevices() {
        drive2 = new MecanumDrive(hardwareMap);
        rightOfBlueHub = new Pose2d(BLUE_HUB_X,BLUE_HUB_Y,Math.toRadians(BLUE_HUB_HEADING));

        rightOfBlueHubCycle1 = new Pose2d(BLUE_HUB_X+cycle_x_offset,BLUE_HUB_Y+cycle_y_offset, Math.toRadians(BLUE_HUB_HEADING));
        rightOfBlueHubCycle2 = new Pose2d(BLUE_HUB_X+cycle_x_offset,BLUE_HUB_Y+cycle_y_offset, Math.toRadians(BLUE_HUB_HEADING)+Math.toRadians(cycle_heading_offset));

        rightOfBlueCycle1 = new Pose2d(rightOfBlueCycle1_X, rightOfBlueCycle1_Y, Math.toRadians(rightOfBlueCycle1_HEADING));
        rightOfBlueCycle2 = new Pose2d(rightOfBlueCycle2_X, rightOfBlueCycle2_Y, Math.toRadians(rightOfBlueCycle2_HEADING));

        blueWarehouse = new Pose2d(WAREHOUSE_X,WAREHOUSE_Y,Math.toRadians(0));
        blueWarehouseIntermediate = new Pose2d(intermediate_x,WAREHOUSE_Y,Math.toRadians(0));

        pA = startPose;
        pB = rightOfBlueHub;
        pB1 = rightOfBlueHubCycle1;
        pB2 = rightOfBlueHubCycle2;
        pC1 = blueWarehouseIntermediate;
        pC2 = new Pose2d(pC1.getX()+intermediate_x_offset,pC1.getY()+intermediate_y_offset, pC1.getHeading());
        pD = blueWarehouse;

        pB_optimized1 = rightOfBlueCycle1;
        pB_optimized2 = rightOfBlueCycle2;

        pD2 = new Pose2d(pD.getX()+WAREHOUSE_X_OFFSET_CYCLE_2, pD.getY()+WAREHOUSE_Y_OFFSET_CYCLE_2, pD.getHeading());
        pD3 = new Pose2d(pD.getX()+(WAREHOUSE_X_OFFSET_CYCLE_2*2), pD.getY()+WAREHOUSE_Y_OFFSET_CYCLE_2, pD.getHeading());
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
                .UNSTABLE_addTemporalMarkerOffset(dump_hack_bca, ()->{
                    carriage.dump();
                })
//                .waitSeconds(dumpWaitTime)
                .addTemporalMarker(()->{
                    carriage.idle();

                })
//                .waitSeconds(DUMP_TIME)
                //may want to add wait seconds here
                .UNSTABLE_addTemporalMarkerOffset(1,()->{
                    slidePos = Slide2.MIN_POSITION;
                });

        for(int i = 0; i < CYCLES; i++){
            builder = builder.setReversed(false);
            if(i==0){
                builder = builder.lineToSplineHeading(pC1);

            }
            else{
                builder = builder.lineToSplineHeading(pC2);
            }

            builder = builder.addTemporalMarker(()->{
                intake.setPower(1);
            });
            if(i==0) {
                builder = builder.lineToSplineHeading(pD);
            }
            else{
                builder = builder.lineToSplineHeading(pD2);
            }
            builder = builder.addTemporalMarker(()->{
                slidePos = null;
            })
                    .addTemporalMarker(()->{
//                        intake.setPower(-1);
                    })
                    .setReversed(true)
                    .splineToLinearHeading(pC1,Math.toRadians(180))
                    .UNSTABLE_addTemporalMarkerOffset(UNINTAKE_DELAY,()->{
                        intake.setPower(-1);
                    })
                    .addTemporalMarker(()->{
                        intake.stop();
                        slidePos = Slide2.MAX_POSITION;
                    });
            if(i==0) {
                builder = builder.lineToSplineHeading(pB_optimized1);
            }
            else{
                builder = builder.lineToSplineHeading(pB_optimized2);
            }
            builder = builder.addTemporalMarker(()->{
                currentPose = drive.getPoseEstimate();
            })
                    .UNSTABLE_addTemporalMarkerOffset(dump_hack_bca2, ()->{
                        carriage.dump();
                    })
                    .UNSTABLE_addTemporalMarkerOffset(dump_hack_bca2+CARRIAGE_DELAY,() -> {
                        carriage.idle();
                    })
//                    .waitSeconds(DUMP_TIME)
                    .UNSTABLE_addTemporalMarkerOffset(1,() -> {
                        slidePos = Slide2.MIN_POSITION;
                    });

        }
        builder = builder.setReversed(false)
                .lineToSplineHeading(new Pose2d(pC1.getX(), pC1.getY()+2, pC1.getHeading()))
//                .lineToSplineHeading(pD2)
                .setReversed(true)
                .addTemporalMarker(()->{
                    intake.setPower(1);
                });
//                .waitSeconds(5);
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
        drive2.move(-0.2,1,0);
        sleep(1000);
        drive2.stop();
    }
}
