package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Config
@TeleOp
public class CarriageServoTester extends OpMode {
    Servo carriage;
    public static double position = 0.0;
    Telemetry dashTelemetry;
    @Override
    public void init() {
        carriage = hardwareMap.get(Servo.class,"carriage");
        dashTelemetry = FtcDashboard.getInstance().getTelemetry();
    }

    @Override
    public void loop() {
        if(gamepad1.dpad_right){
            if(position<1) {
                position += 0.05;
            }
        }
        if(gamepad1.dpad_left){
            if(position>0){
                position-=0.05;
            }
        }
        carriage.setPosition(position);
        telemetry.addData("Current Position: ", position);
        dashTelemetry.addData("Current Position: ", position);
        telemetry.update();
        dashTelemetry.update();
    }
}
