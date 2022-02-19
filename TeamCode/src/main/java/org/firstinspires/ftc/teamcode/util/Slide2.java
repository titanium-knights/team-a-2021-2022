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
    }

    public void setPower(double pow){
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
}
