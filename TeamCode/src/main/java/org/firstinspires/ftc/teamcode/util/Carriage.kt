package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Carriage that holds freight on our new robot's outtake.
 */
@Config class Carriage(hardwareMap: HardwareMap) {
    val servo1: Servo = hardwareMap.servo["carriage1"]
    val servo2: Servo = hardwareMap.servo["carriage2"]

    /**
     * Position of the carriage.
     */
    var position
        get() = servo1.position
        set(value) {
            servo1.position = value
            servo2.position = 1-value
        }

    /**
     * Sets the carriage to the idle position (i.e. carrying freight.)
     */
    fun idle() {
        position = idlePosition
    }

    /**
     * Sets the carriage to the dump position (i.e. dropping freight.)
     */
    fun dump() {
        position = dumpPosition
    }

    companion object {
        @JvmStatic var idlePosition = 1.0
        @JvmStatic var dumpPosition = 0.5
    }
}