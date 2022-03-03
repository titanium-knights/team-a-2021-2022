package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class Generic2ServoTest extends OpMode {
    public static String servo1Name = "carriage1";
    public static String servo2Name = "carriage2";
    public static double pos = 0.87;
    Servo servo1;
    Servo servo2;
    @Override
    public void init() {
        servo1 = hardwareMap.get(Servo.class, servo1Name);
        servo2 = hardwareMap.get(Servo.class, servo2Name);
    }

    @Override
    public void loop() {
        servo1.setPosition(pos);
        servo2.setPosition(1-pos);
    }
}


