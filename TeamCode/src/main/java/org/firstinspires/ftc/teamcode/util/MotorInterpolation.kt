package org.firstinspires.ftc.teamcode.util

import com.qualcomm.robotcore.util.ElapsedTime
import kotlin.math.abs
import kotlin.math.sign

class MotorInterpolation(initial: Double, val rate: Double) {
    private val elapsedTime = ElapsedTime()

    private var startingTime = elapsedTime.seconds()

    var initial = initial
        private set

    val isBusy: Boolean
        get() {
            val change = rate * (elapsedTime.seconds() - startingTime)
            val targetChange = abs(target - initial)
            return change < targetChange
        }

    val current: Double
        get() {
            val change = rate * (elapsedTime.seconds() - startingTime)
            val targetChange = abs(target - initial)
            return if (change >= targetChange) {
                target
            } else {
                change + sign(target - initial) * change
            }
        }

    var target = initial
        set(value) {
            if (value != target) {
                initial = current
                startingTime = elapsedTime.seconds()
                field = value
            }
        }
}