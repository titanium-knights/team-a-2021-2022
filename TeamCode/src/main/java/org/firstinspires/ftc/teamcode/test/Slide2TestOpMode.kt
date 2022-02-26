package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.Slide2

@TeleOp(name = "2-Motor Slide Test Op Mode", group = "Test")
class Slide2TestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val slide = Slide2(hardwareMap)
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        waitForStart()

        var target: Int? = null

        while (opModeIsActive()) {
            when {
                gamepad1.y -> {
                    target = Slide2.MAX_POSITION
                }

                gamepad1.x -> {
                    target = (Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2
                }

                gamepad1.a -> {
                    target = Slide2.MIN_POSITION
                }
            }

            when {
                gamepad1.left_bumper -> {
                    target = null
                    slide.setPower(-0.7)
                }
                gamepad1.right_bumper -> {
                    target = null
                    slide.setPower(0.7)
                }
                target != null -> slide.runToPosition(target)
                else -> slide.setPower(0.0)
            }

            telemetry.addData("Position", slide.currentPosition)
            if (target == null) {
                telemetry.addData("Mode", "Manual")
            } else {
                telemetry.addData("Mode", "Automatic")
                telemetry.addData("Target", target)
            }
            telemetry.update()
        }
    }
}