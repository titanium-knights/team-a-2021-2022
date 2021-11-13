package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

@TeleOp(name = "Motor Test Op Mode")
class MotorTestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val mapping = mapOf(gamepad1::x to "fl", gamepad1::y to "fr", gamepad1::a to "bl", gamepad1::b to "br")
        val motors = mapping.mapValues { hardwareMap.dcMotor[it.value] }

        waitForStart()

        while (opModeIsActive()) {
            motors.forEach {
                val button = it.key
                val motor = it.value
                motor.power = if (button.get()) 0.3 else 0.0
            }
        }
    }
}