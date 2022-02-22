package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism;
import org.firstinspires.ftc.teamcode.util.Carousel;
import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.Slide2;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

/**
 * Duck Murder 2: Electric Boogaloo
 */
@Config
@Autonomous(name = "Murder Duck & Warehouse")
public class DuckWarehouseAuton extends LinearOpMode {
    public static int TEST_POSITION = 2;
    public static int X_POSITION = -59;
    public static int Y_POSITION = -63;

    @Override
    public void runOpMode() throws InterruptedException {
        double colorMultiplier = -1; // TODO: Change to -1 for blue

        OdometryMecanumDrive drive = new OdometryMecanumDrive(hardwareMap);
        CapstoneMechanism capstoneMechanism = new CapstoneMechanism(hardwareMap);
        Carriage carriage = new Carriage(hardwareMap);
        Slide2 slide = new Slide2(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);

        /* int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
                "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "NAME_OF_CAMERA_IN_CONFIG_FILE");
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
                 *//*

            }
        }); */

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

        /* while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        } */

        TrajectorySequence sequenceStart = drive.trajectorySequenceBuilder(new Pose2d(-36, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier))
                .back(8)
                .lineToLinearHeading(new Pose2d(X_POSITION, Y_POSITION * colorMultiplier, Math.toRadians(180)))
                .addTemporalMarker(() -> carousel.spin(false))
                .waitSeconds(10)
                .addTemporalMarker(carousel::stop)
                .setTangent(0)
                .splineToConstantHeading(new Vector2d(-40, -56 * colorMultiplier),
                        Math.toRadians(30) * colorMultiplier)
                .splineToSplineHeading(new Pose2d(-9, (destinationY - 3) * colorMultiplier, Math.toRadians(-90) * colorMultiplier),
                        Math.toRadians(90) * colorMultiplier)
                .build();

        drive.setPoseEstimate(sequenceStart.start());
        drive.followTrajectorySequence(sequenceStart);

        do {
            slide.runToPosition(Slide2.MIN_POSITION);
        } while (opModeIsActive() && slide.getPower() < 0.0);

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
                .strafeLeft(26)
                .forward(36)
                .waitSeconds(0.5)
                .build();

        drive.followTrajectorySequence(sequenceEnd);
    }
}
