package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

@Config public class CarriageDC {
    DcMotor carriage;
    public static int DUMP_POS = -192;
    public static int IDLE_POS = -5;
    public static double CARRIAGE_POWER = 1;
    public final String MOTOR_NAME = "carriage";
    public CarriageDC(HardwareMap hmap){
        carriage = hmap.get(DcMotor.class, MOTOR_NAME);
        carriage.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public int getPosition(){
        return carriage.getCurrentPosition();
    }
    public void setPosition(int pos){
        carriage.setTargetPosition(pos);
        carriage.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        carriage.setPower(CARRIAGE_POWER);
    }
    public void dump(){
        this.setPosition(DUMP_POS);
    }
    public void idle(){
        this.setPosition(IDLE_POS);
    }
    public boolean isBusy() {
        return carriage.isBusy();
    }
}
