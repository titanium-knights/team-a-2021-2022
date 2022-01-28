package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class SlideOuttake(hardwareMap: HardwareMap): Stoppable {
    val slide: DcMotor = hardwareMap.dcMotor["slide"]

    init {
        slide.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        slide.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    var power: Double
        get() = slide.power
        set(value) {
            slide.power = power
        }

    var targetPosition: Int?
        get() = when (slide.mode) {
            DcMotor.RunMode.RUN_TO_POSITION -> slide.targetPosition
            else -> null
        }
        set(value) {
            if (value == null) {
                slide.mode = DcMotor.RunMode.RUN_USING_ENCODER
            } else {
                slide.targetPosition = value
                slide.mode = DcMotor.RunMode.RUN_TO_POSITION
            }
        }

    fun clearTargetPosition() {
        targetPosition = null
    }

    override fun stop() {
        power = 0.0
        targetPosition = null
    }

    val atLimit: Boolean
        get() = false
}