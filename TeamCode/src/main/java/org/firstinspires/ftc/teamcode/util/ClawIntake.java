package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config
public class ClawIntake {
    DcMotorEx arm;
    public Servo claw;
    public static double releasePos = 0.47;
    public static double ballPos = 0.8;
    public static double grabPos = 0.86;
    public static double armMultiplier = -0.5;
    public ClawIntake(HardwareMap hardwareMap) {
        this.arm = hardwareMap.get(DcMotorEx.class,"lift");
        this.arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        this.arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }
    public boolean intakeMotorIsBusy(){
        return this.arm.isBusy();
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
    public double getArmPosition(){
        return arm.getCurrentPosition();
    }
    public void grab() {
        claw.setPosition(grabPos);
    }

    public void grabBall() {
        claw.setPosition(ballPos);
    }

    public void setArmPower(double p){
//        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setPower(p * armMultiplier);
        if (p == 0) {
//            arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
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
//    public void goDumpPos(){
//        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
//    public void goInitPos(){
//        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
//    }
    public void setArmPosition(int pos){
        arm.setTargetPosition(pos);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);

    }
}
