package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.IMU;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Slide;

import static java.lang.Math.abs;

@Config public abstract class Jan30ScoreBase extends LinearOpMode {
    public static long MOTION_SCALE_FACTOR = 50;

    MecanumDrive drive;
    Slide slides;
    Carriage carriage;
    IMU imu;
    @Override
    public void runOpMode(){
        initialize();
        waitForStart();
        dumpInTopLevel();
        runAfterDump();
    }

    public abstract boolean isRed();

    public void initialize(){
        drive = new MecanumDrive(hardwareMap);
        slides = new Slide(hardwareMap);
        slides.stopAndResetEncoder();
        carriage = new Carriage(hardwareMap);
        imu = new IMU(hardwareMap);
        imu.initializeIMU();
    }

    public void dumpInTopLevel(){
        slides.setTargetPosition(Slide.getMaxPosition());
        slides.setPower(0.8);
        sleep(2000);

        drive.driveForwardsWithPower(0.5);
        sleep(20 * MOTION_SCALE_FACTOR);
        drive.stop();

        carriage.dump();
        sleep(1000);
        carriage.idle();
        sleep(1000);

        drive.driveBackwardsWithPower(0.5);
        sleep(10 * MOTION_SCALE_FACTOR);
        drive.stop();

        slides.setTargetPosition(0);
        sleep(2000);
        slides.stop();

        turn(-90);

        drive.strafeLeftWithPower(0.3);
        sleep(2000);
        drive.stop();
    }

    public abstract void runAfterDump();

    public void driveLeaningIntoWall(boolean reverse) {
        drive.move(isRed() ? 0.15 : -0.15, 0.5 * (reverse ? -1 : 1), 0);
    }

    public void driveLeaningIntoWall() {
        driveLeaningIntoWall(false);
    }

    public void turn(double target, double power) {
        double initialAngle = imu.getZAngle();
        if (initialAngle == target) return;
        if (abs(initialAngle - target) > 2) {
            if (target > initialAngle) {
                drive.turnRightWithPower(power);
            } else {
                drive.turnLeftWithPower(power);
            }
            while (abs(target - imu.getZAngle()) > 2 && opModeIsActive()) {
                idle();
            }
            drive.stop();
        }
    }

    public void turn(double target) {
        turn(target, 0.3);
    }
}
