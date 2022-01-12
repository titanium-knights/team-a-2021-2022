package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.pipelines.DuckMurderPipeline;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

@Autonomous(name = "Murder Duck & StorageUnit")
public class DuckStorageUnitAuton extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        double colorMultiplier = 1; // TODO: Change to -1 for blue

        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId",
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
                 */

            }
        });

        waitForStart();

        while (opModeIsActive())
        {
            telemetry.addData("Analysis", pipeline.getAnalysis());
            telemetry.update();

            // Don't burn CPU cycles busy-looping in this sample
            sleep(50);
        }

        OdometryMecanumDrive drive = new OdometryMecanumDrive(hardwareMap);


        TrajectorySequence sequence = drive.trajectorySequenceBuilder(new Pose2d(-36, -66 * colorMultiplier, 0))
                .lineTo(new Vector2d(-56, -66 * colorMultiplier))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(90) * colorMultiplier)
                .splineToConstantHeading(new Vector2d(-54, -60 * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .turn(Math.toRadians(90) * colorMultiplier)
                .forward(36)
                .splineToSplineHeading(new Pose2d(-31, -20 * colorMultiplier, Math.toRadians(180)), Math.toRadians(0))
                .waitSeconds(0.5)
                .setTangent(Math.toRadians(180 - colorMultiplier * 45))
                .splineToConstantHeading(new Vector2d(-60, -35 * colorMultiplier), Math.toRadians(-90) * colorMultiplier)
                .build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }

}
