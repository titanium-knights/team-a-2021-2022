package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.HardwareMap

@Config class CapstoneMechanism(hardwareMap: HardwareMap) {
    val servo = hardwareMap.servo["capstone"]
    var position by servo::position

    companion object Presets {
        @JvmStatic val storage = 1.0
        @JvmStatic val idle = 0.87
        @JvmStatic val preDump = 0.67
        @JvmStatic val postDump = 0.53
        @JvmStatic val pickup = 0.26
    }
}