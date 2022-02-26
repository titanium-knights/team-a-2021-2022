package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.TeleOpLeagues;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism;
import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.Slide;
import org.firstinspires.ftc.teamcode.util.Slide2;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Config
public abstract class DuckSpareAuton extends LinearOpMode {
    public abstract double getColorMultiplier();

    public static double HIGH_POS = -50.5;
    public static double MID_POS = -45;
    public static double LOW_POS = -45.25;
    public static double LOW_HORIZ_POS = -3.75;

    @Override
    public void runOpMode() throws InterruptedException {
        double colorMultiplier = getColorMultiplier(); // TODO: Change to -1 for blue
        OdometryMecanumDrive drive = new OdometryMecanumDrive(hardwareMap);
        CapstoneMechanism capstoneMechanism = new CapstoneMechanism(hardwareMap);
        Carriage carriage = new Carriage(hardwareMap);
        Slide2 slide = new Slide2(hardwareMap);

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "Webcam 1");
        OpenCvCamera camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewId);
        DuckMurderPipeline pipeline = new DuckMurderPipeline(telemetry);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                // Usually this is where you'll want to start streaming from the camera (see section 4)
                camera.startStreaming(1920, 1080, OpenCvCameraRotation.UPRIGHT);
                camera.setPipeline(pipeline);
            }
            @Override
            public void onError(int errorCode)
            {
                /*
                 * This will be called if the camera could not be opened
                 */
            }
        });

        waitForStart();
        int position = 2;
        DuckMurderPipeline.CapstonePosition capstonePosition = pipeline.getAnalysis();

        if (capstonePosition == DuckMurderPipeline.CapstonePosition.CENTER) {
            position = 1;
        } else if (capstonePosition == DuckMurderPipeline.CapstonePosition.LEFT) {
            position = 0;
        }

        telemetry.addData("position",position);
        telemetry.update();
        double destinationY;
        if (position == 2) {
            destinationY = HIGH_POS;
        } else if (position == 1) {
            destinationY = MID_POS;
        } else {
            destinationY = LOW_POS;
        }
        pipeline.updateTelemetry = false;
        capstoneMechanism.setPosition(0.6);
        TrajectorySequence sequenceStart = drive.trajectorySequenceBuilder(new Pose2d(12, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)

                .splineToLinearHeading(new Pose2d(position == 0 ? LOW_HORIZ_POS : -11.5, destinationY * colorMultiplier, Math.toRadians(-90) * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .build();

        drive.setPoseEstimate(sequenceStart.start());
        drive.followTrajectorySequence(sequenceStart);

        TeleOpLeagues.startPose = drive.getPoseEstimate();

        do {
            if (position == 2) {
                slide.runToPosition(Slide2.MAX_POSITION);
            } else if (position == 1) {
                slide.runToPosition((Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2);
            } else {
                slide.runToPosition(760);
            }
        } while (opModeIsActive() && slide.getPower() > 0.0);

        carriage.dump();
        sleep(2000);
        carriage.idle();
        sleep(2000);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);

        capstoneMechanism.setPosition(CapstoneMechanism.getStorage());

        TrajectorySequence sequenceEnd = drive.trajectorySequenceBuilder(sequenceStart.end())
                .lineTo(new Vector2d(-6, -46 * colorMultiplier))
                .turn(Math.toRadians(90) * colorMultiplier)

                .lineTo(new Vector2d(15, -46 * colorMultiplier))
                .waitSeconds(10)
                .lineTo(new Vector2d(12, -72 * colorMultiplier))
                .forward(24)
                .waitSeconds(0.5)
                .build();

        drive.followTrajectorySequence(sequenceEnd);

        TeleOpLeagues.startPose = drive.getPoseEstimate();
    }
}
