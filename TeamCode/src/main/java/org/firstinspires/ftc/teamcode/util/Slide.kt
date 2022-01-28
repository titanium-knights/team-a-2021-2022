package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * The slide of our new robot's outtake.
 */
@Config class Slide(hardwareMap: HardwareMap): Stoppable {
    val motor: DcMotor = hardwareMap.dcMotor["slide"]

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
        motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
    }

    /**
     * Stops the motors and resets the encoder.
     */
    fun stopAndResetEncoder() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
    }

    /**
     * Power of the slide.
     */
    var power
        get() = motor.power
        set(value) {
            if (motor.mode == DcMotor.RunMode.STOP_AND_RESET_ENCODER) {
                motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
            }
            motor.power = power
        }

    /**
     * Current position of the slide.
     */
    val currentPosition
        get() = motor.currentPosition - zeroPosition

    /**
     * Position on which all position-related calculations are made.
     */
    var zeroPosition = 0

    /**
     * Target position of the slide. Set to null to move the slide freely.
     */
    var targetPosition
        get() = when (motor.mode) {
            DcMotor.RunMode.RUN_TO_POSITION -> motor.targetPosition - zeroPosition
            else -> null
        }
        set(value) {
            if (value == null) {
                motor.mode = DcMotor.RunMode.RUN_USING_ENCODER
            } else {
                motor.targetPosition = value + zeroPosition
                motor.mode = DcMotor.RunMode.RUN_TO_POSITION
            }
        }

    /**
     * Clears the target position, allowing the slide to move freely.
     */
    fun clearTargetPosition() {
        targetPosition = null
    }

    /**
     * Stops the slide.
     */
    override fun stop() {
        power = 0.0
        targetPosition = null
    }

    /**
     * Whether the slide has reached its physical limits. If so, it is not safe to move the slide.
     */
    val isAtLimit
        get() = !(minPosition..maxPosition).contains(currentPosition)

    companion object {
        var minPosition = Int.MIN_VALUE
        var maxPosition = Int.MAX_VALUE
    }
}