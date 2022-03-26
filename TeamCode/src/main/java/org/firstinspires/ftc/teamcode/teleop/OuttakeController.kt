package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.util.Carriage
import org.firstinspires.ftc.teamcode.util.MotorInterpolation
import org.firstinspires.ftc.teamcode.util.PushButton
import org.firstinspires.ftc.teamcode.util.Slide2

@Config class OuttakeController(
    private val slide: Slide2,
    private val carriage: Carriage,
    private val gamepad: Gamepad
    ): BasicPassdionComponent() {

    enum class State {
        IDLE,
        DUMPING,
        DUMPED,
        RETRACTING
    }

    private val interpolation = MotorInterpolation(Carriage.idlePosition, 1.6)
    private val dumpButton = PushButton { gamepad.b }
    private val highButton = PushButton { gamepad.y }
    private val midButton = PushButton { gamepad.x }
    private val elapsedTime = ElapsedTime()

    private var stateTransitionTime: Double = elapsedTime.seconds()
    var state = State.IDLE
        private set(value) {
            field = value
            stateTransitionTime = elapsedTime.seconds()
        }

    val timeInState
        get() = elapsedTime.seconds() - stateTransitionTime

    var slideTargetPos: Int? = null
    val isSlideUnderManualControl
        get() = slideTargetPos == null

    var disableSlideLimits = false

    override fun init(opMode: PassdionOpMode) {
        carriage.position = interpolation.current
        opMode.register(dumpButton)
        opMode.register(highButton)
        opMode.register(midButton)
    }

    override fun update(opMode: PassdionOpMode) {
        when (state) {
            State.IDLE -> {
                interpolation.target = Carriage.idlePosition
            }
            State.DUMPING -> {
                interpolation.target = Carriage.dumpPosition
                if (!interpolation.isBusy) {
                    state = State.DUMPED
                }
            }
            State.DUMPED -> {
                if (timeInState >= DELAY_MS / 1000.0) {
                    state = State.RETRACTING
                }
            }
            State.RETRACTING -> {
                interpolation.target = Carriage.idlePosition
                if (!interpolation.isBusy) {
                    state = State.IDLE
                    slideTargetPos = LOW
                }
            }
        }

        when {
            highButton.isPressed -> {
                slideTargetPos = if (slideTargetPos == HIGH) LOW else HIGH
            }
            midButton.isPressed -> {
                slideTargetPos = if (slideTargetPos == MID) LOW else MID
            }
        }

        if (dumpButton.isPressed && slide.currentPosition >= SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD) {
            state = State.DUMPING
        }

        when {
            gamepad.dpad_up && (slide.currentPosition < Slide2.MIN_POSITION || disableSlideLimits) -> {
                slide.power = 0.85
                slideTargetPos = null
                state = State.IDLE
            }
            gamepad.dpad_down && (slide.currentPosition > Slide2.MIN_POSITION || disableSlideLimits) -> {
                slide.power = -0.85
                slideTargetPos = null
                state = State.IDLE
            }
            slideTargetPos == null -> {
                slide.power = 0.0
            }
            else -> {
                slide.runToPosition(slideTargetPos!!)
            }
        }

        carriage.position = interpolation.current
    }

    companion object {
        @JvmStatic var DELAY_MS = 600
        @JvmStatic var HIGH = Slide2.MAX_POSITION
        @JvmStatic var MID = 250
        @JvmStatic var LOW = Slide2.MIN_POSITION - 40
        @JvmStatic var SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = 200;
    }
}