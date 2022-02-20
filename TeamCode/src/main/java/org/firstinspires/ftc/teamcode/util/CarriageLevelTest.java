package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@Config
@TeleOp
public class CarriageLevelTest extends OpMode {
    public static double position;
    Servo carriage;
    @Override
    public void init() {
        carriage = hardwareMap.get(Servo.class, "carriage");
    }

    @Override
    public void loop() {
        carriage.setPosition(position);
    }
}
