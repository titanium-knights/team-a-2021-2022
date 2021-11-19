package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DistanceSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.ClawIntake;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Carousel;
import org.firstinspires.ftc.teamcode.util.IMU;

public class TimeBasedRedAuton extends LinearOpMode {
    MecanumDrive drive;
    ClawIntake clawIn;
    IMU imu;
    DistanceSensor front;
    Carousel carousel;
    public enum ScoringLevel{
        LEVELONE, LEVELTWO,LEVELTHREE;
    }
    public ScoringLevel getLevel(){
        return ScoringLevel.LEVELTHREE;
    }
    public void turnLeft90Degrees(){
        while(imu.getZAngle()<90){
            drive.turnLeftWithPower(0.4);
        }
        drive.stop();
    }
    @Override
    public void runOpMode() throws InterruptedException {
        imu = new IMU(hardwareMap);
        imu.initializeIMU();
        carousel = new Carousel(hardwareMap);
        drive = new MecanumDrive(hardwareMap);
        clawIn = new ClawIntake(hardwareMap);
        clawIn.grab();
        front = hardwareMap.get(DistanceSensor.class,"front_distance");
        waitForStart();
        ScoringLevel level = getLevel();
        double kp=0.5;
        switch(level){
            case LEVELTHREE:
                while(front.getDistance(DistanceUnit.INCH)<5.5){
                    drive.driveBackwardsWithPower(kp*(5.5-front.getDistance(DistanceUnit.INCH)));
                }
                drive.stop();
                sleep(500);
                clawIn.liftArm();
                sleep(500);
                clawIn.stopArm();
                clawIn.release();
                while(front.getDistance(DistanceUnit.INCH)>0.5){
                    drive.driveForwardsWithPower(kp*(front.getDistance(DistanceUnit.INCH)));
                }
                drive.stop();
                turnLeft90Degrees();
                drive.driveBackwardsWithPower(0.4);
                sleep(1000);
                drive.stop();
                carousel.spin();
                sleep(2000);
                carousel.stop();
                drive.driveForwardsWithPower(0.4);
                sleep(2000);
                drive.stop();
                drive.strafeRightWithPower(0.4);
                sleep(1000);
                drive.stop();
                drive.driveForwardsWithPower(0.7);
                sleep(4000);
                drive.stop();


        }
    }
}
