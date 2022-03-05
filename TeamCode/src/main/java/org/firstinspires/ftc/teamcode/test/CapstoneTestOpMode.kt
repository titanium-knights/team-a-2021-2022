package org.firstinspires.ftc.teamcode.test

import com.acmerobotics.dashboard.FtcDashboard
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.CapstoneMechanism2

@TeleOp(name = "Capstone Test Op Mode", group = "Test")
class CapstoneTestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val capstone = CapstoneMechanism2(hardwareMap)
        val telemetry = MultipleTelemetry(telemetry, FtcDashboard.getInstance().telemetry)
        waitForStart()

        while (opModeIsActive()) {
            when {
                gamepad1.left_bumper -> {
                    capstone.setManualPower(-CapstoneMechanism2.power)
                }
                gamepad1.right_bumper -> {
                    capstone.setManualPower(CapstoneMechanism2.power)
                }
                else -> capstone.setManualPower(0.0)
            }

            telemetry.addData("Position", capstone.position)
            telemetry.update()
        }
    }
}