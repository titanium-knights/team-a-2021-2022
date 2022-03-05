package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.OdometryRetraction

@TeleOp(name = "Manual Odometry Retraction")
class OdometryRetractionOpMode: LinearOpMode() {
    override fun runOpMode() {
        val odometry = OdometryRetraction(hardwareMap)
        waitForStart()

        while (opModeIsActive()) {
            when {
                gamepad1.dpad_up -> {
                    odometry.retract()
                }
                gamepad1.dpad_down -> {
                    odometry.extend()
                }
            }
        }
    }
}