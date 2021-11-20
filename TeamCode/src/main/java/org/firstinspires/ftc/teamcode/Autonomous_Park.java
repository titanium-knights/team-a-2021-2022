package org.firstinspires.ftc.teamcode;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Config public abstract class Autonomous_Park extends LinearOpMode{

    public static long parkTime = 3250;

//    Pose2d startingPose= new Pose2d(-60.0,-36.0,Math.toRadians(180));
//    Trajectory t1;
//    Pose2d warehouse = new Pose2d(-5.0,-3.5,Math.toRadians(180.0));

    public abstract boolean isRed();

    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);

        waitForStart();

        drive.move(isRed() ? 0.15 : -0.15, 0.3, 0);
        sleep(parkTime);
        drive.stop();

        //make ver. with sleep() at beginning with starting position farther from warehouse
    }



}