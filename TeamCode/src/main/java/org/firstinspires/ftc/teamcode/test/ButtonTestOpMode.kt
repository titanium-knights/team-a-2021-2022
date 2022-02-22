package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.PushButton

@TeleOp(name = "Button Test Op Mode")
class ButtonTestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val telemetry = MultipleTelemetry(FtcDashboard.getInstance().telemetry, telemetry)
        val button = PushButton { gamepad1.x }
        var manualPreviousState = false

        waitForStart()

        var presses = 0
        var mismatches = 0
        var manualMismatches = 0
        var updates = 0

        while (opModeIsActive()) {
            if (button.isPressed) presses++
            if (button.currentState != button.previousState) mismatches++
            if (gamepad1.x != manualPreviousState) manualMismatches++

            telemetry.addData("gamepad1.x", gamepad1.x)
            telemetry.addData("button.currentState", button.currentState)
            telemetry.addData("button.previousState", button.previousState)
            telemetry.addData("button.isPressed", button.isPressed)
            telemetry.addData("presses", presses)
            telemetry.addData("mismatches", mismatches)
            telemetry.addData("manualMismatches", manualMismatches)
            telemetry.update()

            button.update()
            manualPreviousState = gamepad1.x

            updates++

            idle()
        }
    }
}