package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.util.ElapsedTime

class TimeTelemetry: PassdionComponent {
    private val elapsedTime = ElapsedTime()

    override fun init(opMode: PassdionOpMode) {}
    override fun initLoop(opMode: PassdionOpMode) {}

    override fun start(opMode: PassdionOpMode) {
        elapsedTime.reset()
    }

    override fun update(opMode: PassdionOpMode) {
        if (elapsedTime.time() < 90) {
            opMode.telemetry.addData("Tele-Op Time Remaining", (90 - elapsedTime.seconds()).toInt())
        } else {
            opMode.telemetry.addData("Endgame Time Remaining", (120 - elapsedTime.seconds()).toInt())
        }
    }

    override fun cleanup(opMode: PassdionOpMode) {}
}