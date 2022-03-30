package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.teleop.BasicPassdionComponent;
import org.firstinspires.ftc.teamcode.teleop.PassdionOpMode;
import org.jetbrains.annotations.NotNull;

public class Carousel {
//    public CRServo motor;
    public CRServo motor2;

    public Carousel(HardwareMap hmap){
//        motor = hmap.get(CRServo.class,"carousel_l");
        motor2 = hmap.get(CRServo.class, "carousel_r");
    }
    public void setPower(double power) {
//        motor.setPower(power);
        motor2.setPower(-power);
    }
    public void spin(boolean fast){
        setPower(fast ? (1) : (0.75 / 3));
    }
    public void spin() {
        this.spin(false);
    }
    public void stop(){
        setPower(0);
    }
    public void spinReverse(boolean fast){ setPower(fast ? (-1) : (-0.75 / 3)); }
    public void spinReverse() {
        this.spinReverse(false);
    }

    public class Controller extends BasicPassdionComponent {
        private Gamepad gamepad;

        public Controller(Gamepad gamepad) {
            this.gamepad = gamepad;
        }

        @Override
        public void init(@NotNull PassdionOpMode opMode) {}

        @Override
        public void update(@NotNull PassdionOpMode opMode) {
            if (gamepad.dpad_right) {
                setPower(0.75);
            } else if (gamepad.dpad_left) {
                setPower(-0.75);
            } else {
                setPower(0);
            }
        }
    }
}
