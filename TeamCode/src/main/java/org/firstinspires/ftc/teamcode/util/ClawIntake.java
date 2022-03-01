package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ClawIntake {
    public Servo claw;
    public static double releasePos = 0.47;
    public static double ballPos = 0.8;
    public static double grabPos = 0.86;
    public static double armMultiplier = -0.5;
    public ClawIntake(HardwareMap hardwareMap) {

        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }

    public void grab() {
        claw.setPosition(grabPos);
    }

    public void grabBall() {
        claw.setPosition(ballPos);
    }

    public void release() {
        claw.setPosition(releasePos);
    }

    public void incrementClawPosition(double amount) {
        double position = claw.getPosition();

        if (amount > 0 && position >= Math.max(releasePos, grabPos)) return;
        if (amount < 0 && position <= Math.min(releasePos, grabPos)) return;

        claw.setPosition(position + amount);
    }

}
