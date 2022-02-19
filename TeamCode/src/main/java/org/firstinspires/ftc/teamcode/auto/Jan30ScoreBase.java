package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.IMU;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Slide2;

import static java.lang.Math.abs;

@Config public abstract class Jan30ScoreBase extends LinearOpMode {
    public static int MOTION_SCALE_FACTOR = 25;

    MecanumDrive drive;
    Slide2 slides;
    Carriage carriage;
    IMU imu;
    @Override
    public void runOpMode(){
        initialize();
        waitForStart();
        dumpInTopLevel();
        alignWithWall();
        runAfterDump();
    }

    public abstract boolean isRed();

    public void initialize(){
        drive = new MecanumDrive(hardwareMap);
        slides = new Slide2(hardwareMap);
        slides.stopAndResetEncoder();
        carriage = new Carriage(hardwareMap);
        imu = new IMU(hardwareMap);
        imu.initializeIMU();
    }

    public void dumpInTopLevel(){
        slides.setTargetPosition(Slide2.getMaxPosition());
        slides.setPower(0.8);
        sleep(2000);

        drive.driveBackwardsWithPower(0.5);
        sleep(14 * MOTION_SCALE_FACTOR);
        drive.stop();

        carriage.dump();
        sleep(2000);
        carriage.idle();
        sleep(1000);

        drive.driveForwardsWithPower(0.5);
        sleep(9 * MOTION_SCALE_FACTOR);
        drive.stop();

        slides.setTargetPosition(0);
        sleep(4000);
        slides.stop();
    }

    public void alignWithWall() {
        turn(isRed() ? -90 : 90);

        if (isRed()) {
            drive.strafeRightWithPower(0.3);
        } else {
            drive.strafeLeftWithPower(0.3);
        }
        sleep(2500);
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
