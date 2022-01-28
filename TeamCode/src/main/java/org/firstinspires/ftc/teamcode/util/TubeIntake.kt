package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.HardwareMap

class TubeIntake(hardwareMap: HardwareMap): Stoppable {
    val motor: DcMotor = hardwareMap.dcMotor["tube_intake"]

    init {
        motor.zeroPowerBehavior = DcMotor.ZeroPowerBehavior.BRAKE
    }

    var power: Double
        get() = motor.power
        set(value) {
            motor.power = value
        }

    override fun stop() {
        power = 0.0
    }
}