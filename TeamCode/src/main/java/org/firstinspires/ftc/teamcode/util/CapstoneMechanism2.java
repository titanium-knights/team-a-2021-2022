package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config public class CapstoneMechanism2 {
    DcMotor motor;
    public static double power = 0.8;
    public static int idle = -490;
    public static int pickup = -2480;

    public CapstoneMechanism2(HardwareMap hardwareMap, boolean resetEncoders){
        motor = hardwareMap.dcMotor.get("capstone");
        if (resetEncoders) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public CapstoneMechanism2(HardwareMap hardwareMap) {
        this(hardwareMap, true);
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
