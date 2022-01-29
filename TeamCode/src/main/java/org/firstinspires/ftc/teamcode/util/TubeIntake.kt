package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

/**
 * Intake on our new robot.
 */
@Config class TubeIntake(hardwareMap: HardwareMap): Stoppable {
    val motor: DcMotor = hardwareMap.dcMotor["tube_intake"]

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    /**
     * Power of the intake.
     */
    var power: Double
        get() = motor.power
        set(value) {
            motor.power = value
        }

    /**
     * Stops the intake.
     */
    override fun stop() {
        power = 0.0
    }
    fun reverse(){
        power = -intakePower
    }
    /**
     * Spins the intake in order to take in freight.
     */
    fun spin() {
        power = intakePower
    }

    /**
     * Spins the intake in reverse.
     */
    fun outtake() {
        power = outtakePower
    }

    companion object {
        var intakePower = 1.0
        var outtakePower = -1.0
    }
}