package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CapstoneMechanism2 {
    DcMotor motor;
    static double power = 0.2;
    public static int idle = -490;
    public static int pickup = -2480;

    public CapstoneMechanism2(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("capstone");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public void setPosition(int pos){
        motor.setTargetPosition(pos);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    public void setManualPower(double pow){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(pow);
    }

    public int getPosition(){
        return motor.getCurrentPosition();
    }

    public static int getIdle(){
        return idle;
    }
    public static int getPickup() { return pickup; }
}
