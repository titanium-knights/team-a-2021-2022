package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism2;
import org.firstinspires.ftc.teamcode.util.CapstoneVision;
import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.Slide2;
import org.firstinspires.ftc.teamcode.util.TubeIntake;

@Autonomous
@Disabled
public class Cycling extends LinearOpMode {
    public double colorMultiplier = -1;
    OdometryMecanumDrive drive;
    CapstoneMechanism2 capstone;
    Carriage carriage;
    Slide2 slide;
    TubeIntake intake;
    CapstoneVision vis;
    Pose2d startPose;
    Pose2d warehouseIntermediate = new Pose2d(12,65.15*colorMultiplier,Math.toRadians(0));
    Pose2d warehouse = new Pose2d(48,65.15*colorMultiplier,Math.toRadians(0));
    double[] destinationY = {46.6,43.5,49};
    TrajectorySequence initialCapstoneDump, cycle1,park;
    public void initializeTrajs(int pos){
        TrajectorySequence sequenceStartLow = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(-9, destinationY[0] * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();
        TrajectorySequence sequenceStartMid = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(-9, destinationY[1] * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();
        TrajectorySequence sequenceStartHigh = drive.trajectorySequenceBuilder(startPose)
                .setReversed(true)
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(-9, destinationY[2] * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();
        if(pos==0){
            initialCapstoneDump = sequenceStartLow;
        }
        else if(pos==1){
            initialCapstoneDump = sequenceStartMid;
        }
        else{
            initialCapstoneDump = sequenceStartHigh;
        }
        cycle1 = drive.trajectorySequenceBuilder(initialCapstoneDump.end())
                .setReversed(false)
                .splineToLinearHeading(warehouseIntermediate, Math.toRadians(0))
                .waitSeconds(.25)
                .addTemporalMarker(() ->{
                    intake.setPower(0.7);
                })
                .waitSeconds(.25)
                .splineToLinearHeading(warehouse,0)
                .setReversed(true)
                .waitSeconds(3)
                .addTemporalMarker(()->{
                    intake.setPower(-0.7);
                })
                .splineToLinearHeading(warehouseIntermediate,0)
                .addTemporalMarker(()->{
                    intake.stop();
                })
                .splineToLinearHeading(new Pose2d(-9, destinationY[2] * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();
        park = drive.trajectorySequenceBuilder(cycle1.end())
                .setReversed(false)
                .splineToLinearHeading(warehouseIntermediate, Math.toRadians(0))
                .waitSeconds(.25)
                .splineToLinearHeading(warehouse,0)
                .build();
        }

    public void initializeObjects(){
        drive = new OdometryMecanumDrive(hardwareMap);
        capstone = new CapstoneMechanism2(hardwareMap);
        carriage = new Carriage(hardwareMap);
        slide = new Slide2(hardwareMap);
        intake = new TubeIntake(hardwareMap);
        vis = new CapstoneVision(hardwareMap,telemetry);
        startPose = new Pose2d(12, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier);
    }
    public void dumpHigh(){
        do{
            slide.runToPosition(Slide2.MAX_POSITION);
        }
        while(opModeIsActive()&&slide.getPower()>0.0);
    }
    public void dumpMidLow(int position){
        if(position == 1){
            do{
                slide.runToPosition((Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2);
            }
            while(opModeIsActive()&&slide.getPower()>0.0);
        }
        else{
            do{
                slide.runToPosition(760);
            }
            while(opModeIsActive()&&slide.getPower()>0.0);
        }
        carriage.dump();
        sleep(2000);
        carriage.idle();
        sleep(2000);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);
    }
    @Override
    public void runOpMode(){
        initializeObjects();
        capstone.setPosition(CapstoneMechanism2.getIdle());

        waitForStart();
        int position = vis.getPosition();
        initializeTrajs(position);
        drive.setPoseEstimate(startPose);
        drive.followTrajectorySequence(initialCapstoneDump);

        if (position == 2) {
            dumpHigh();
        }
        else{
            dumpMidLow(position);
        }


        drive.followTrajectorySequence(cycle1);
        dumpHigh();

        drive.followTrajectorySequence(park);



    }
}