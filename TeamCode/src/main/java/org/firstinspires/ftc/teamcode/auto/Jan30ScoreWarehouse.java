package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.IMU;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Slide;

import static java.lang.Math.abs;

public abstract class Jan30ScoreWarehouse extends LinearOpMode {
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

    public void initialize(){
        drive = new MecanumDrive(hardwareMap);
        slides = new Slide(hardwareMap);
        slides.clearTargetPosition();
        carriage = new Carriage(hardwareMap);
        imu = new IMU(hardwareMap);
        imu.initializeIMU();
    }

    public void dumpInTopLevel(){
        slides.setTargetPosition(Slide.getMaxPosition());
        sleep(2000);
        carriage.dump();
        sleep(750);
        carriage.idle();
        sleep(750);
        slides.setTargetPosition(0);
        sleep(2000);
    }

    public abstract void runAfterDump();

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
