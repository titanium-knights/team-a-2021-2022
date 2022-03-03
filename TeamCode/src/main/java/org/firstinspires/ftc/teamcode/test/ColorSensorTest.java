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
public class ColorSensorTest extends OpMode {
    ColorSensor cs;
    DistanceSensor ds;
    @Override
    public void init() {
        cs = hardwareMap.get(ColorSensor.class, "carriagecolor");
        ds = hardwareMap.get(DistanceSensor.class, "carriagedist");
    }

    @Override
    public void loop() {
        telemetry.addData("Color Sensor R",cs.red());
        telemetry.addData("Color Sensor G",cs.green());
        telemetry.addData("Color Sensor B",cs.blue());
        telemetry.addData("Color Sensor Dist",((DistanceSensor)cs).getDistance(DistanceUnit.MM));
        telemetry.addData("Distance Sensor Dist",ds.getDistance(DistanceUnit.MM));
        telemetry.update();
    }
}
