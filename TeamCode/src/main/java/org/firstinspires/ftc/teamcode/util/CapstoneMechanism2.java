package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CapstoneMechanism2 {
    DcMotor motor;
    static double power = 0.2;
    static int idle = 0;

    public CapstoneMechanism2(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("capstone");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void setPosition(int pos){
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
        motor.setTargetPosition(pos);

    }

    public void setManualPower(double pow){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(pow);
    }

    public double getPosition(){
        return motor.getCurrentPosition();
    }

    public static int getIdle(){
        return idle;
    }
}
