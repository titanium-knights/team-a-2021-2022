package org.firstinspires.ftc.teamcode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.teamcode.util.Carousel;

public abstract class DuckMurderAuton extends LinearOpMode{

    public abstract boolean isRed();

//    Pose2d startingPose= new Pose2d(-60.0,-36.0,Math.toRadians(180));
//    Trajectory t1;
//    Pose2d warehouse = new Pose2d(-5.0,-3.5,Math.toRadians(180.0));

    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);

        double xMultiplier = isRed() ? -1 : 1;

        waitForStart();

        sleep(10000);

        drive.move(0.2 * xMultiplier, -0.3, 0);
        sleep(1000);
        drive.stop();

        drive.move(0.075 * xMultiplier, -0.15, 0);
        sleep(2200);
        drive.stop();

        if (isRed()) {
            drive.move(0.15, -0.15, 0.5);
            sleep(250);
            drive.stop();
        }

        carousel.spin();
        sleep(10000); // Ari: "10 Mississippi's"
        carousel.stop();

        if (isRed()) {
            drive.move(0, 0.1, 0.5);
            sleep(250);
            drive.stop();
        }

        drive.move(-0.2 * xMultiplier,0.3,0);
        sleep(5000);
        drive.stop();


    }



}