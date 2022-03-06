package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism2;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Slide2;

import java.util.concurrent.atomic.AtomicReference;

@Config public abstract class Cycling extends BaseAutonomousOpMode {
    Pose2d warehouseIntermediate = new Pose2d(6,-65.15*getColorMultiplier(),Math.toRadians(0));
    Pose2d warehouse = new Pose2d(WAREHOUSE_X,-65.15*getColorMultiplier(),Math.toRadians(0));

    public static double DUMP_X = -12;
    public static double DUMP_Y = -52.5;
    public static double WAREHOUSE_X = 48;
    public static long PARK_TIME = 1000;
    public static double STRAFE_DIST = 5;

    @Override
    protected boolean grabsOnInit() {
        return true;
    }

    private Pose2d performCycle(Pose2d start) {
        TrajectorySequence first = drive.trajectorySequenceBuilder(start)
                .setReversed(false)
                .setTangent(Math.toRadians(-90) * getColorMultiplier())
                .splineToLinearHeading(warehouseIntermediate, Math.toRadians(0))
                .addTemporalMarker(() -> capstone.setPosition(CapstoneMechanism2.getIdle()))
                .addTemporalMarker(() -> claw.grab())
                .strafeLeft(STRAFE_DIST)
                .build();
        drive.followTrajectorySequence(first);

        Pose2d current = new Pose2d(drive.getPoseEstimate().getX(), warehouseIntermediate.getY(), 0);
        drive.setPoseEstimate(current);
        intake.setPower(-1.0);

        TrajectorySequence second = drive.trajectorySequenceBuilder(current)
                .splineToLinearHeading(warehouse,0)
                .setReversed(true)
                .waitSeconds(1)
                .addTemporalMarker(()->{
                    intake.setPower(1.0);
                })
                .splineToLinearHeading(warehouseIntermediate,0)
                .addTemporalMarker(()->{
                    intake.stop();
                })
                .splineToSplineHeading(new Pose2d(DUMP_X, DUMP_Y * getColorMultiplier(), Math.toRadians(-90) * getColorMultiplier()), Math.toRadians(90) * getColorMultiplier())
                .build();
        drive.followTrajectorySequence(second);

        return second.end();
    }

    void park(Pose2d start) {
        TrajectorySequence first = drive.trajectorySequenceBuilder(start)
                .setReversed(false)
                .setTangent(Math.toRadians(-90) * getColorMultiplier())
                .splineToLinearHeading(warehouseIntermediate, Math.toRadians(0))
                .addTemporalMarker(() -> capstone.setPosition(CapstoneMechanism2.getIdle()))
                .addTemporalMarker(() -> claw.grab())
                .strafeLeft(STRAFE_DIST)
                .build();
        drive.followTrajectorySequence(first);

        retraction.retract();

        MecanumDrive mecanumDrive = new MecanumDrive(hardwareMap);
        mecanumDrive.driveForwardsWithPower(1.0);
        sleep(PARK_TIME);
        mecanumDrive.stop();
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
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);
    }

    @Override
    public void run(){
        ShippingHubLevel level = performAnalysis();

        TrajectorySequence sequenceStart = spareDuckSequence(level)
                .back(3)
                .build();

        drive.setPoseEstimate(sequenceStart.start());
        drive.followTrajectorySequence(sequenceStart);

        Pose2d start = sequenceStart.end();
        for (int i = 0; i < 1; i++) {
            start = performCycle(start);
            dumpHigh();
        }

        park(start);
    }
}