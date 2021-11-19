package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawIntake {
    DcMotor arm;
    Servo claw;
    public ClawIntake(HardwareMap hardwareMap) {
        this.arm = hardwareMap.dcMotor.get("lift");
        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }

    public void liftArm() {
        arm.setPower(1);
    }

    public void lowerArm(){
        arm.setPower(-1);
    }

    public void stopArm() {
        arm.setPower(0);
    }

    public void grab() {
        claw.setPosition(0.8);
    }

    public void release() {
        claw.setPosition(0);
    }

    public void incrementClawPosition(double amount) {
        double position = claw.getPosition();

        if (amount > 0 && position >= 1) return;
        if (amount < 0 && position <= 0) return;

        claw.setPosition(position + amount);
    }
}
