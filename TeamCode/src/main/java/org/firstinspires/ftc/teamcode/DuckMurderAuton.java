package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@Autonomous(name = "Duck Murder Autonomous")
public class DuckMurderAuton extends LinearOpMode{

//    Pose2d startingPose= new Pose2d(-60.0,-36.0,Math.toRadians(180));
//    Trajectory t1;
//    Pose2d warehouse = new Pose2d(-5.0,-3.5,Math.toRadians(180.0));

    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        DcMotor motor =  hardwareMap.dcMotor.get("motor");

        waitForStart();

        drive.move(0, -1, 0);
        sleep(1000);
        drive.stop();

        motor.setPower(1);
        sleep(3000);
        motor.setPower(0);

        drive.move(0,1,0);
        sleep(5000);
        drive.stop();


    }



}