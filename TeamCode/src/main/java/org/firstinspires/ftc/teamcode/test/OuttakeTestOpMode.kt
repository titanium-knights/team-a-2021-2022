package org.firstinspires.ftc.teamcode.test

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.Slide

@TeleOp(name = "Outtake Test Op Mode")
class OuttakeTestOpMode: LinearOpMode() {
    override fun runOpMode() {
        val slide = Slide(hardwareMap)
        slide.stopAndResetEncoder()

        waitForStart()

        while (opModeIsActive()) {
            if (gamepad1.x) {
                slide.power = -gamepad1.left_stick_y.toDouble()
            } else {
                slide.power = slide.getSafePower(-gamepad1.left_stick_y.toDouble())
            }
            if (gamepad1.y) {
                slide.zeroPosition += slide.currentPosition
            }

            telemetry.addData("Slide Power", slide.power)
            telemetry.addData("Slide Pos", "${slide.zeroPosition} + ${slide.currentPosition}")
            telemetry.update()
        }
    }
}