package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism;
import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.Slide;
import org.firstinspires.ftc.teamcode.util.Slide2;

@Config
@Autonomous(name = "Spare Duck & Warehouse")
public class DuckSpareAuton extends LinearOpMode {
    public static int TEST_POSITION = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        double colorMultiplier = -1; // TODO: Change to -1 for blue
        OdometryMecanumDrive drive = new OdometryMecanumDrive(hardwareMap);
        CapstoneMechanism capstoneMechanism = new CapstoneMechanism(hardwareMap);
        Carriage carriage = new Carriage(hardwareMap);
        Slide2 slide = new Slide2(hardwareMap);

        int position = TEST_POSITION;

        double destinationY;
        if (position == 2) {
            destinationY = -49;
        } else if (position == 1) {
            destinationY = -43.5;
        } else {
            destinationY = -49;
        }

        waitForStart();
        capstoneMechanism.setPosition(CapstoneMechanism.getIdle());
        TrajectorySequence sequenceStart = drive.trajectorySequenceBuilder(new Pose2d(12, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(-9, destinationY * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();

        drive.setPoseEstimate(sequenceStart.start());
        drive.followTrajectorySequence(sequenceStart);

        do {
            if (position == 2) {
                slide.runToPosition(Slide2.MAX_POSITION);
            } else {
                slide.runToPosition((Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2);
            }
        } while (opModeIsActive() && slide.getPower() > 0.0);

        carriage.dump();
        sleep(2000);
        carriage.idle();
        sleep(2000);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);

        TrajectorySequence sequenceEnd = drive.trajectorySequenceBuilder(sequenceStart.end())
                .lineTo(new Vector2d(-6, -46 * colorMultiplier))
                .turn(Math.toRadians(90) * colorMultiplier)

                .lineTo(new Vector2d(15, -46 * colorMultiplier))
                .waitSeconds(10)
                .strafeLeft(26)
                .forward(36)
                .waitSeconds(0.5)
                .build();

        drive.followTrajectorySequence(sequenceEnd);
    }
}
