package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.PushButton

@Config object FanSettings {
    @JvmField var power = 0.2
    @JvmField var name = "fan"
    @JvmField var enable = true
}

@TeleOp(name = "it's hot and i need a fan")
class FAN: PassdionOpMode() {
    override fun registerComponents() {
        val fan = hardwareMap.dcMotor[FanSettings.name]

        onLoop {
            fan.power = if (FanSettings.enable) FanSettings.power else 0.0
        }

        val button = PushButton { gamepad1.a }
        onLoop {
            if (button.isPressed) {
                FanSettings.enable = !FanSettings.enable
            }
        }

        addTelemetryData("Fan Status") {
            if (FanSettings.enable) "ENABLED" else "DISABLED"
        }
    }
}