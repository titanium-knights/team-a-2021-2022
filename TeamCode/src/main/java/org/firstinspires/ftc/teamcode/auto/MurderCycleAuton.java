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
    }

    @Override
    public void runOpMode() throws InterruptedException {
        Pose2d rightOfRedHub = new Pose2d(0,-45,Math.toRadians(-75));
        Pose2d redWarehouse = new Pose2d(48,-65.15,Math.toRadians(0));
        Pose2d redWarehouseIntermediate = new Pose2d(12,-65.15,Math.toRadians(0));
        double timeAtHub = 3;

        int cycles = 1;

        TrajectorySequenceBuilder builder = drive.trajectorySequenceBuilder(new Pose2d(-36, -60 , Math.toRadians(-90) ))
                .back(12)
                .lineToLinearHeading(new Pose2d(-61, -56, Math.toRadians(180) - Math.toRadians(15)))
                .addTemporalMarker(() -> carousel.spinReverse(false))
                .waitSeconds(8)
                .addTemporalMarker(carousel::stop)
                .waitSeconds(4)
                .setTangent(0)
                .lineToSplineHeading(rightOfRedHub)
                .waitSeconds(timeAtHub);

                for(int i = 0; i < cycles; i++){
                    builder = builder.setReversed(false)
                            .splineToLinearHeading(redWarehouseIntermediate, Math.toRadians(0))
                            .splineToLinearHeading(redWarehouse,0)
                            .setReversed(true)
                            .splineToLinearHeading(redWarehouseIntermediate,0)
                            .waitSeconds(0.5)
                            .splineToLinearHeading(rightOfRedHub, Math.toRadians(180))
                            .waitSeconds(timeAtHub);
                }

                builder = builder.setTangent(0)
                .splineToLinearHeading(redWarehouseIntermediate, Math.toRadians(0))
                .splineToLinearHeading(redWarehouse,0);
        TrajectorySequence sequence = builder.build();
    }
}
