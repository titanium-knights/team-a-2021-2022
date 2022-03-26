package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

@Config public class Slide2 {
    DcMotor motor;
    DcMotor motor2;

    public static int MIN_POSITION = 0;
    public static int MAX_POSITION = 591;

    public static double IDLE_POWER = 0.01;
    public static double IDLE_POWER_RTP = 0.14;
    public static boolean USE_ENCODER = false;

    public Slide2(HardwareMap hardwareMap){
        motor = hardwareMap.dcMotor.get("slide_r");
        motor2 = hardwareMap.dcMotor.get("slide_l");
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(USE_ENCODER ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    }

    public void setPower(double pow){
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(USE_ENCODER ? DcMotor.RunMode.RUN_USING_ENCODER : DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        if (pow == 0) {
            pow = IDLE_POWER;
        }

        motor.setPower(pow);
        motor2.setPower(-pow);
    }

    public double getPower() {
        return motor.getPower();
    }

    public void runToPosition(int pos){
        int currentPos = motor.getCurrentPosition();
        //double multiplier = Math.min(1, Math.max(0, Math.abs(pos - currentPos) / 150.0));
        double multiplier = 1;
        if(pos - currentPos > 100){
            setPower(0.7 * multiplier);
        }
        else if(pos - currentPos < -100){
            setPower(-0.7 * multiplier);
        }
        else if (pos == 0) {
            setPower(0);
        } else {
            setPower(IDLE_POWER_RTP);
        }
    }

    public int getCurrentPosition() {
        return motor.getCurrentPosition();
    }
}
