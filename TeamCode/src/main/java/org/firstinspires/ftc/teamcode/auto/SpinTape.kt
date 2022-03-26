package org.firstinspires.ftc.teamcode.auto

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.util.TapeMeasureMechanism

@Autonomous(name = "tape go spin")
@Config
class SpinTape: OpMode() {
    private lateinit var tape: TapeMeasureMechanism
    lateinit var elapsedTime: ElapsedTime

    override fun init() {
        tape = TapeMeasureMechanism(hardwareMap)
        tape.setPitchPosition(initialPos, true)
        elapsedTime = ElapsedTime()
    }

    override fun start() {
        elapsedTime.reset()
    }

    override fun loop() {
        val i = (elapsedTime.seconds() / interval).toInt()
        tape.setEjection(when {
            i % 2 == 0 -> 0.3
            else -> -0.3
        })
        tape.setYawPower(when {
            i % 2 == 0 -> 0.3
            else -> -0.3
        })
    }

    companion object {
        @JvmStatic var initialPos = 0.4
        @JvmStatic var interval = 1.5
    }

}