package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel {
    public DcMotor motor;

    public Carousel(HardwareMap hmap){
        motor =hmap.get(DcMotor.class,"carousel");
    }
    public void spin(boolean fast){
        motor.setPower(fast ? (0.75 / 2) : (0.75 / 4));
    }
    public void spin() {
        this.spin(false);
    }
    public void stop(){
        motor.setPower(0);
    }
    public void spinReverse(boolean fast){ motor.setPower(fast ? (-0.75 / 2) : (-0.75 / 4)); }
    public void spinReverse() {
        this.spinReverse(false);
    }
}
