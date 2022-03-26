package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.arcrobotics.ftclib.hardware.motors.Motor
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo

/**
 * Carriage that holds freight on our new robot's outtake.
 */
@Config class Carriage(hardwareMap: HardwareMap) {
    val servo: Servo = hardwareMap.servo["carriage"]

    /**
     * Position of the carriage.
     */
    var position
        get() = servo.position
        set(value) {
            servo.position = value
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
        @JvmStatic var dumpPosition = 0.0
    }
}