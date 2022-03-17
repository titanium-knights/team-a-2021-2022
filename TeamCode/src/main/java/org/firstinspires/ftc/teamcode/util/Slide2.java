package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

@Config public class Slide2 {
    DcMotor motor;
    DcMotor motor2;

    public static int MIN_POSITION = 0;
    public static int MAX_POSITION = 2000;

    public Slide2(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("slide_r");
        motor2 = hardwareMap.dcMotor.get("slide_l");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void setPower(double pow){
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(pow);
        motor2.setPower(-pow);
    }

    public double getPower() {
        return motor.getPower();
    }

    public void runToPosition(int pos){
        int currentPos = motor.getCurrentPosition();
        //double multiplier = Math.min(1, Math.max(0, Math.abs(pos - currentPos) / 150.0));
        double multiplier = 1;
        if(pos - currentPos > 100){
            setPower(0.7 * multiplier);
        }
        else if(pos - currentPos < -100){
            setPower(-0.7 * multiplier);
        }
        else
        {
            setPower(0);
        }
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }
}
