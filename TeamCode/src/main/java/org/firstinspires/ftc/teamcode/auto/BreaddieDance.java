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
    CapstoneMechanism2 arm;
    ClawIntake claw;

    public static int INTRO_BEAT = 300;

    public static int SPINNY_BEAT = 94;
    public static int SPINNY_BEAT_MID = 78;

    public static int ALL_ACTIONS_BEAT = 250;
    public static int ALL_ACTIONS_PAUSE = 120;

    public static int SIT_PAUSE = 250;
    public static int WAVE_BEAT = 200;

    public static double POWER_BODY = .5;
    public static double POWER_ARM = .25;

    public void initialize() {
        drive = new MecanumDrive(hardwareMap);
        arm = new CapstoneMechanism2(hardwareMap);

        claw = new ClawIntake(hardwareMap);
    }

    public void sit() {

    }

    @Override
    public void runOpMode(){
        initialize();
        waitForStart();

        //dance :-)
            //drive left, drive right immediately
        //cc left, cw - right

        //initial turn left turn right taps
        drive.turnLeftWithPower(POWER_BODY);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        drive.turnRightWithPower(POWER_BODY);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        drive.turnRightWithPower(POWER_BODY);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        drive.turnLeftWithPower(POWER_BODY);
        sleep(INTRO_BEAT);
        drive.stop();
        sleep(INTRO_BEAT/2);

        //stop-motion looking 180 turn (with a little jaz in-between)
        //three clockwise spins
        for (int i=0; i<3; i++) {
            drive.turnRightWithPower(POWER_BODY);
            sleep(SPINNY_BEAT);
            drive.stop();
            sleep(INTRO_BEAT/2);
        }

        sleep(INTRO_BEAT);
        //semi pause and flip flop
        //one-two ccw, one-two cw
        for (int i=0; i<2; i++) {
            drive.turnLeftWithPower(POWER_BODY);
            sleep(SPINNY_BEAT_MID);
            drive.stop();
            sleep(INTRO_BEAT/4);
        }

        for (int i=0; i<2; i++) {
            drive.turnRightWithPower(POWER_BODY);
            sleep(SPINNY_BEAT_MID);
            drive.stop();
            sleep(INTRO_BEAT/4);
        }

        //long cw spin into sexy pause
        drive.turnRightWithPower(POWER_BODY);
        sleep((long) (SPINNY_BEAT*1.25));
        drive.stop();
        sleep(INTRO_BEAT);

        //fuller spin

        //all movement x3: while spinning, arm goes up and down 3 cycles
        for (int i=0; i<3; i++) {
            drive.turnLeftWithPower(POWER_BODY); //movement thorughout spinnyyyy
            sleep(ALL_ACTIONS_BEAT);

            for (i=0; i<2; i++) {
                arm.setManualPower(POWER_ARM); //arm up and down
                sleep(ALL_ACTIONS_BEAT);

                arm.setManualPower(-POWER_ARM);
                sleep(ALL_ACTIONS_BEAT);
            }

            arm.setManualPower(0);
            drive.stop();
            sleep(ALL_ACTIONS_PAUSE);

        }

        //all movement done --> goes into sit,, instead of sitting moving arm up and down --> do this twice
        for (int i=0; i<2; i++) {
            arm.setManualPower(-POWER_ARM);
            sleep(SIT_PAUSE);
            arm.setManualPower(0);
            sleep(SIT_PAUSE);

            arm.setManualPower(POWER_ARM);
            sleep(SIT_PAUSE);
            arm.setManualPower(0);
            sleep(SIT_PAUSE);
        }

        //wave time like the queen (of england) --claw

        for (int i=0; i<2; i++) {
            arm.setManualPower(POWER_ARM);
            sleep(WAVE_BEAT);
            arm.setManualPower(0);
            sleep(WAVE_BEAT);

            claw.grab();
            sleep(WAVE_BEAT);
            claw.release();
            sleep(WAVE_BEAT);


            sleep(SIT_PAUSE);
            arm.setManualPower(0);
            sleep(SIT_PAUSE);
        }

        //three arm waves clockwise
        //pause
        //more arm waves

        //sit
        //stand
        //sit
        //wave like a girlboss --claw
        //sit









    }

}

