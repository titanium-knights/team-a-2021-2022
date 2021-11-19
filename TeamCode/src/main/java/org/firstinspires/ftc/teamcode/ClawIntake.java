package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class ClawIntake {
    DcMotor arm;
    Servo claw;
    double releasePos = 0.47;
    double grabPos = 0.86;
    double armMultiplier = -0.5;
    public ClawIntake(HardwareMap hardwareMap) {
        this.arm = hardwareMap.dcMotor.get("lift");
        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }

    public void liftArm() {
        arm.setPower(armMultiplier);
    }

    public void lowerArm(){
        arm.setPower(-armMultiplier);
    }

    public void stopArm() {
        arm.setPower(0);
        this.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void grab() {
        claw.setPosition(this.grabPos);
    }
    public void setArmPower(double p){
        arm.setPower(p * armMultiplier);
        if (p == 0) this.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }
    public void release() {
        claw.setPosition(this.releasePos);
    }

    public void incrementClawPosition(double amount) {
        double position = claw.getPosition();

        if (amount > 0 && position >= Math.max(releasePos, grabPos)) return;
        if (amount < 0 && position <= Math.min(releasePos, grabPos)) return;

        claw.setPosition(position + amount);
    }
}
