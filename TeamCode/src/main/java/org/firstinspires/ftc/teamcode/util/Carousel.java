package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Carousel {
    public CRServo motor;
    public CRServo motor2;

    public Carousel(HardwareMap hmap){
        motor = hmap.get(CRServo.class,"carousel_l");
        motor2 = hmap.get(CRServo.class, "carousel_r");
    }
    public void setPower(double power) {
        motor.setPower(power);
        motor2.setPower(-power);
    }
    public void spin(boolean fast){
        setPower(fast ? (0.75) : (0.75 / 3));
    }
    public void spin() {
        this.spin(false);
    }
    public void stop(){
        setPower(0);
    }
    public void spinReverse(boolean fast){ setPower(fast ? (-0.75) : (-0.75 / 3)); }
    public void spinReverse() {
        this.spinReverse(false);
    }
}
