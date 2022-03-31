package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@Config
@TeleOp
public class DistanceSensorTest extends LinearOpMode {
    DistanceSensor sensor;
    public void runOpMode() {
        sensor = hardwareMap.get(DistanceSensor.class, "distance_sensor");
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Distance", sensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
}
