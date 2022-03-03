package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class Rumble extends OpMode {
    @Override
    public void init() {
        gamepad1.rumble(100000);
    }

    @Override
    public void loop() {
    }
}
