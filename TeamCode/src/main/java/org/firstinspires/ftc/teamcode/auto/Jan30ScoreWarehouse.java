package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.Carriage;
import org.firstinspires.ftc.teamcode.util.IMU;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Slide;

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
}
