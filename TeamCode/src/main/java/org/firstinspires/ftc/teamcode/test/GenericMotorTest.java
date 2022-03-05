package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@Config
@TeleOp
public class GenericMotorTest extends OpMode {
    DcMotor motor;
    public static String MOTOR_NAME = "capstone";
    @Override
    public void init() {
        motor = hardwareMap.get(DcMotor.class, MOTOR_NAME);
    }

    @Override
    public void loop() {
        motor.setPower(gamepad1.left_stick_y/2);
    }
}
