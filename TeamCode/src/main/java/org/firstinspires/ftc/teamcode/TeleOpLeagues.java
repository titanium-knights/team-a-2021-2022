package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive;
import org.firstinspires.ftc.teamcode.odometry.StandardTrackingWheelLocalizer;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Slide;
import org.firstinspires.ftc.teamcode.util.Slide2;
import org.firstinspires.ftc.teamcode.util.TubeIntake;

@Config
@TeleOp(name = "TeleOp Leagues", group = "TeleOp")
public class TeleOpLeagues extends OpMode {
    MecanumDrive drive;
    TubeIntake intake;
    OdometryMecanumDrive odoDrive;
    Slide2 slide2;
    int targetPos;
    public static int HIGH = Slide.getMaxPosition();
    public static int MID = (Slide.getMinPosition() + Slide.getMaxPosition()) / 2;
    public static int LOW = Slide.getMinPosition();

    @Override
    public void init() {
        intake = new TubeIntake(hardwareMap);
        drive = new MecanumDrive(hardwareMap);
        odoDrive = new OdometryMecanumDrive(hardwareMap);
        slide2 = new Slide2(hardwareMap);
    }

    @Override
    public void loop() {
        drive.teleOpRobotCentric(gamepad1,0.5);
        if(gamepad1.right_trigger>0.3) {
            intake.setPower(gamepad1.right_trigger);
        } else if(gamepad1.left_trigger>0.3) {
            intake.setPower(-gamepad1.left_trigger);
        } else {
            intake.stop();
        }

        if(gamepad1.y){
            targetPos = HIGH;
        }
        else if(gamepad1.x){
            targetPos = MID;
        }
        else if(gamepad1.a){
            targetPos = LOW;
        }


        slide2.runToPosition(targetPos);

        odoDrive.update();

        Pose2d poseEstimate = odoDrive.getPoseEstimate();
        telemetry.addData("x", poseEstimate.getX());
        telemetry.addData("y", poseEstimate.getY());
        telemetry.addData("heading", poseEstimate.getHeading());
        telemetry.update();
    }
}
