package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

public class Slide2 {
    DcMotor motor;
    DcMotor motor2;

    public Slide2(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("dcMotor");
        motor2 = hardwareMap.dcMotor.get("dcMotor2");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void setPower(double pow){
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(pow);
        motor2.setPower(pow);
    }

    public void runToPosition(int pos){
        int currentPos = motor.getCurrentPosition();
        if(pos - currentPos > 30){
            setPower(1);
        }
        else if(pos - currentPos < -30){
            setPower(-1);
        }
        else{
            setPower(0);
        }
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }
}
