package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.TelemetryPacket
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.PushButton

open class ServoTestOpMode(private val servoName: String): LinearOpMode() {
    override fun runOpMode() {
        val drb = PushButton { gamepad1.dpad_right }
        val dlb = PushButton { gamepad1.dpad_left }
        val xb = PushButton { gamepad1.x }
        val bb = PushButton { gamepad1.b }
        val dashboard = FtcDashboard.getInstance()

        val servo = hardwareMap.servo[servoName]
        waitForStart()

        var position = 0.5
        while (opModeIsActive()) {
            if (drb.isPressed) position += 0.1
            if (dlb.isPressed) position -= 0.1
            if (bb.isPressed) position += 0.01
            if (xb.isPressed) position -= 0.01

            servo.position = position

            val packet = TelemetryPacket()
            packet.put("Position", position)
            dashboard.sendTelemetryPacket(packet)

            telemetry.addData("Position", position)
            telemetry.update()

            drb.update()
            dlb.update()
            xb.update()
            bb.update()
        }
    }
}

@TeleOp(name = "Claw Test Op Mode", group = "Test")
class ClawTestOpMode: ServoTestOpMode("claw")