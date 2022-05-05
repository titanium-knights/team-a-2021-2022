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

    public static int beat = 500;

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

        //initial turn left turn right taps
        drive.turnLeftWithPower(.5);
        sleep(beat);
        drive.stop();

        drive.turnRightWithPower(.5);
        sleep(beat);
        drive.stop();
        sleep(beat);

        drive.turnRightWithPower(.5);
        sleep(beat);
        drive.stop();

        drive.turnLeftWithPower(.5);
        sleep(beat);
        drive.stop();

        //stop-motion looking 180 turn (with a little jaz inbetween)









    }

}

