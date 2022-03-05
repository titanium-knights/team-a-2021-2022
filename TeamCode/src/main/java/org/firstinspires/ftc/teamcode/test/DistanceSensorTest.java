package org.firstinspires.ftc.teamcode.test;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp
public class DistanceSensorTest extends OpMode {
    DistanceSensor ds;
    @Override
    public void init() {
        ds = hardwareMap.get(DistanceSensor.class, "carriagedist");
    }

    @Override
    public void loop() {
        telemetry.addData("Distance Sensor Dist",ds.getDistance(DistanceUnit.MM));
        telemetry.update();
    }
}
