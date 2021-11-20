package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ClawIntake {
    DcMotor arm;
    Servo claw;
    public static double releasePos = 0.47;
    public static double ballPos = 0.8;
    public static double grabPos = 0.86;
    public static double armMultiplier = -0.5;
    public ClawIntake(HardwareMap hardwareMap) {
        this.arm = hardwareMap.dcMotor.get("lift");
        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }

    public void liftArm() {
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setPower(armMultiplier);
    }

    public void lowerArm(){
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setPower(-armMultiplier);
    }

    public void stopArm() {
        arm.setPower(0);
        this.arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void grab() {
        claw.setPosition(grabPos);
    }

    public void grabBall() {
        claw.setPosition(ballPos);
    }

    public void setArmPower(double p){
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setPower(p * armMultiplier);
        if (p == 0) {
            arm.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
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
