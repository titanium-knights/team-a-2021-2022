package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Autonomous(name = "LED Auton")
public class LEDAuton extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        RevBlinkinLedDriver leds = hardwareMap.get(RevBlinkinLedDriver.class, "blingbling");
        waitForStart();

        while (opModeIsActive()) {
            leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_OCEAN_PALETTE);
            sleep(2000);
            leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_LAVA_PALETTE);
            sleep(2000);
            leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.COLOR_WAVES_RAINBOW_PALETTE);
            sleep(2000);
            leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.TWINKLES_FOREST_PALETTE);
            sleep(2000);
            leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.STROBE_GOLD);
            sleep(2000);
        }
    }
}