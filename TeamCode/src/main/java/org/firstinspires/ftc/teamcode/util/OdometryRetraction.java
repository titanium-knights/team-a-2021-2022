package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

@Config public class OdometryRetraction {
    private final Servo servo1;
    private final Servo servo2;

    public static String SERVO1_NAME = "odometry1";
    public static String SERVO2_NAME = "odometry2";

    public static double SERVO1_EXTENDED_POS = 0;
    public static double SERVO2_EXTENDED_POS = 0;
    public static double SERVO1_RETRACTED_POS = 0.1;
    public static double SERVO2_RETRACTED_POS = 0.1;

    public Servo getServo1() {
        return servo1;
    }

    public Servo getServo2() {
        return servo2;
    }

    public OdometryRetraction(HardwareMap hardwareMap) {
        servo1 = hardwareMap.servo.get(SERVO1_NAME);
        servo2 = hardwareMap.servo.get(SERVO2_NAME);
    }

    public boolean isRetracted() {
        double position = servo1.getPosition();
        return Math.abs(position - SERVO1_EXTENDED_POS) > Math.abs(position - SERVO1_RETRACTED_POS);
    }

    public void extend() {
        servo1.setPosition(SERVO1_EXTENDED_POS);
        servo2.setPosition(SERVO2_EXTENDED_POS);
    }

    public void retract() {
        servo1.setPosition(SERVO1_RETRACTED_POS);
        servo2.setPosition(SERVO2_RETRACTED_POS);
    }
}
