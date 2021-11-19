package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel {
    DcMotor m;

    public Carousel(HardwareMap hmap){
        m=hmap.get(DcMotor.class,"carousel");
    }
    public void spin(){
        m.setPower(0.75);
    }
    public void stop(){
        m.setPower(0);
    }
    public void spinReverse(){ m.setPower(-0.75); }
}
