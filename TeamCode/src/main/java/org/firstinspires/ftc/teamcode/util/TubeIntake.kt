package org.firstinspires.ftc.teamcode.util

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.teamcode.teleop.BasicPassdionComponent
import org.firstinspires.ftc.teamcode.teleop.PassdionOpMode

/**
 * Intake on our new robot.
 */
@Config class TubeIntake(hardwareMap: HardwareMap): Stoppable {
    val motor: DcMotor = hardwareMap.dcMotor["intake"]

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
//        power = -1.0
        power= outtakePower
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
        var intakePower = -0.8
        var outtakePower = 0.8
    }

    inner class Controller(private val gamepad: Gamepad): BasicPassdionComponent() {
        override fun init(opMode: PassdionOpMode) {}

        override fun update(opMode: PassdionOpMode) {
            power = when {
                gamepad.right_trigger > 0.3 -> gamepad.right_trigger * 0.85
                gamepad.left_trigger > 0.3 -> -gamepad.left_trigger * 0.85
                else -> 0.0
            }
        }
    }
}