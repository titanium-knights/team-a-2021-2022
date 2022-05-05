package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.teleop.BasicPassdionComponent;
import org.firstinspires.ftc.teamcode.teleop.PassdionOpMode;
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism2;
import org.firstinspires.ftc.teamcode.util.ClawIntake;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.jetbrains.annotations.NotNull;

@Config
@Autonomous

public class BreaddieDance extends LinearOpMode {
    MecanumDrive drive;
    CapstoneMechanism2 capstone;
    ClawIntake clawIntake;

    public static int INTRO_BEAT = 300;
    public static int SPINNY_BEAT = 94;
    public static int SPINNY_BEAT_MID = 78;

    public static double POWER = .5;

    public void initialize() {
        drive = new MecanumDrive(hardwareMap);
        capstone = new CapstoneMechanism2(hardwareMap);
        clawIntake = new ClawIntake(hardwareMap);
    }

    @Override
    public void runOpMode(){
        initialize();
        waitForStart();

        //dance :-)
            //drive left, drive right immediately
        //cc left, cw - right

        //initial turn left turn right taps
        drive.turnLeftWithPower(POWER);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        drive.turnRightWithPower(POWER);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        drive.turnRightWithPower(POWER);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        drive.turnLeftWithPower(POWER);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        //stop-motion looking 180 turn (with a little jaz in-between)
        //three counter clockwise spins
        for (int i=0; i<3; i++) {
            drive.turnLeftWithPower(POWER);
            sleep(SPINNY_BEAT);
            drive.stop();
            sleep(INTRO_BEAT/2);
        }

        sleep(INTRO_BEAT);
        //semi pause and flip flop
        //one-two cw, one-two cc
        for (int i=0; i<2; i++) {
            drive.turnRightWithPower(POWER);
            sleep(SPINNY_BEAT_MID);
            drive.stop();
            sleep(INTRO_BEAT/4);
        }

        for (int i=0; i<2; i++) {
            drive.turnLeftWithPower(POWER);
            sleep(SPINNY_BEAT_MID);
            drive.stop();
            sleep(INTRO_BEAT/4);
        }

        //long cw spin into sexy pause
        drive.turnRightWithPower(POWER);
        sleep((long) (SPINNY_BEAT*1.25));
        drive.stop();
        sleep(INTRO_BEAT);

        //wave time like the queen (of england)
        //three arm waves clockwise
        //pause
        //more arm waves

        //sit
        //stand
        //sit
        //wave like a girlboss
        //sit









    }

}

