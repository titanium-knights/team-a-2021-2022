package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
@TeleOp
public class DistanceSensorTest extends LinearOpMode {
    DistanceSensor sensor;
    public void runOpMode() {
        telemetry = new MultipleTelemetry(FtcDashboard.getInstance().getTelemetry(), telemetry);
        sensor = hardwareMap.get(DistanceSensor.class, "distance_sensor");

        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Distance", sensor.getDistance(DistanceUnit.MM));
            telemetry.update();
        }
    }
}
