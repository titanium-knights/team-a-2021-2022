package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import org.firstinspires.ftc.teamcode.util.Slide

@Autonomous(name = "Lower Slides")
class LowerSlide: LinearOpMode() {
    override fun runOpMode() {
        val slide = Slide(hardwareMap)
        slide.stopAndResetEncoder()
        slide.zeroPosition = -Slide.maxPosition

        waitForStart()
        slide.targetPosition = -10
        slide.power = 0.5

        while (slide.currentPosition > 0 && opModeIsActive()) {
            idle()
        }

        slide.stop()
    }
}