package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * The slide of our new robot's outtake.
 */
@Deprecated("Use Slide2 instead") @Config class Slide(hardwareMap: HardwareMap): Stoppable {
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
            motor.power = value
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
     * Applies encoder limits to the given power.
     */
    fun getSafePower(power: Double) = when {
        power > 0.0 -> if (currentPosition > maxPosition) 0.0 else power
        power < 0.0 -> if (currentPosition < minPosition) 0.0 else power
        else -> 0.0
    }

    companion object {
        @JvmStatic var minPosition = -50
        @JvmStatic var maxPosition = 4500
    }
}