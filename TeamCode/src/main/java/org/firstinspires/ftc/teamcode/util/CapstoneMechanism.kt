package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Deprecated("Use CapstoneMechanism2 instead") @Config class CapstoneMechanism(hardwareMap: HardwareMap) {
    val servo = hardwareMap.servo["capstone"]
    var position
        get() = servo.position
        set(value) {
            servo.position = value
        }

    companion object Presets {
        @JvmStatic val storage = 0.8
        @JvmStatic val idle = 0.7
        @JvmStatic val preDump = 0.67
        @JvmStatic val postDump = 0.53
        @JvmStatic val pickup = 0.06
    }
}