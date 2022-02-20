package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.Slide2

@TeleOp(name = "2-Motor Slide Test Op Mode")
class Slide2TestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val slide = Slide2(hardwareMap)
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        waitForStart()

        while (opModeIsActive()) {
            slide.setPower(when {
                gamepad1.left_bumper -> -0.3
                gamepad1.right_bumper -> 0.3
                else -> 0.0
            })

            telemetry.addData("Position", slide.currentPosition)
            telemetry.update()
        }
    }
}