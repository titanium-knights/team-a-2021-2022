package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp

open class ServoTestOpMode(private val servoName: String): LinearOpMode() {
    override fun runOpMode() {
        val dashboard = FtcDashboard.getInstance()

        val servo = hardwareMap.servo[servoName]
        waitForStart()

        servo.position = 0.0
        while (opModeIsActive()) {
            servo.position = (gamepad1.right_stick_x.toDouble() + 1) / 2

            val packet = TelemetryPacket()
            packet.put("Position", servo.position)
            dashboard.sendTelemetryPacket(packet)

            telemetry.addData("Position", servo.position)
            telemetry.update()

            sleep(50)
        }
    }
}

@TeleOp(name = "Claw Test Op Mode")
class ClawTestOpMode: ServoTestOpMode("claw")