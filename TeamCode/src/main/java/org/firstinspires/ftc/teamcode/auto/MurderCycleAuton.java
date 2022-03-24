package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism2;
import org.firstinspires.ftc.teamcode.util.CapstoneVision;
import org.firstinspires.ftc.teamcode.util.Carousel;
import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.ClawIntake;
import org.firstinspires.ftc.teamcode.util.OdometryRetraction;
import org.firstinspires.ftc.teamcode.util.Slide2;
import org.firstinspires.ftc.teamcode.util.TubeIntake;

public class MurderCycleAuton extends LinearOpMode {
    protected Carousel carousel;
    protected OdometryMecanumDrive drive;
    protected TubeIntake intake;
    protected Slide2 slide;
    protected Carriage carriage;

    protected void setupDevices() {
        drive = new OdometryMecanumDrive(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        slide = new Slide2(hardwareMap);
        carriage = new Carriage(hardwareMap);
        carousel = new Carousel(hardwareMap);
        intake = new TubeIntake(hardwareMap);
    }

    public void dumpHigh(){
        do{
            slide.runToPosition(Slide2.MAX_POSITION);
        }
        while(opModeIsActive() && slide.getPower()>0.0);

        carriage.dump();
        sleep(2000);
        carriage.idle();
        sleep(2000);

        do {
            slide.runToPosition(Slide2.MIN_POSITION + 200);
        } while (opModeIsActive() && slide.getPower() < 0.0);
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d rightOfRedHub = new Pose2d(0,-45,Math.toRadians(-75));
        Pose2d redWarehouse = new Pose2d(48,-65.15,Math.toRadians(0));
        Pose2d redWarehouseIntermediate = new Pose2d(12,-65.15,Math.toRadians(0));
        Pose2d carouselPos = new Pose2d(-55, -60,Math.toRadians(-90));
        Pose2d startingPosition = new Pose2d(-36,-60, Math.toRadians(-90));

        int cycles = 1;

        TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(startingPosition)
                .lineToLinearHeading(carouselPos)
                .addTemporalMarker(() -> carousel.spinReverse(false))
                .waitSeconds(8)
                .addTemporalMarker(carousel::stop)
                .setTangent(0)
                .lineToSplineHeading(rightOfRedHub)
                //.waitSeconds(timeAtHub)
                .addTemporalMarker(()->{
                    dumpHigh();
                });

                for(int i = 0; i < cycles; i++){
                    builder = builder.setReversed(false)
                            .setTangent(0)
                            .splineToSplineHeading(redWarehouseIntermediate, Math.toRadians(0))
                            .splineToSplineHeading(redWarehouse,0)
                            .addTemporalMarker(()->{
                                intake.setPower(1.0);
                            })
                            .waitSeconds(1)
                            .addTemporalMarker(()->{
                                intake.setPower(-1.0);
                            })
                            .setReversed(true)
                            .splineToLinearHeading(redWarehouseIntermediate,Math.toRadians(180))
                            .addTemporalMarker(()->{
                                intake.stop();
                            })
                            .splineToSplineHeading(rightOfRedHub, -rightOfRedHub.getHeading())
                            .addTemporalMarker(()->{
                                dumpHigh();
                            });
                }

                builder = builder.setReversed(false)
                        .splineToSplineHeading(redWarehouseIntermediate, Math.toRadians(0))
                        .splineToSplineHeading(redWarehouse,0)
                        .setReversed(true);
        TrajectorySequence sequence = builder.build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);

        do {
            slide.runToPosition(Slide2.MIN_POSITION + 200);
        } while (opModeIsActive() && slide.getPower() > 0.0);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);
    }
}
