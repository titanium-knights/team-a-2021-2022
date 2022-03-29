package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class CarriageDC {
    DcMotor carriage;
    public static final int DUMP_POS = 100;
    public static final int IDLE_POS = 50;
    public final String MOTOR_NAME = "carriage";
    public CarriageDC(HardwareMap hmap){
        carriage = hmap.get(DcMotor.class, MOTOR_NAME);
        carriage.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }
    public double getPosition(){
        return carriage.getCurrentPosition();
    }
    public void setPosition(int pos){
        carriage.setTargetPosition(pos);
    }
    public void dump(){
        this.setPosition(DUMP_POS);
    }
    public void idle(){
        this.setPosition(IDLE_POS);
    }
}
