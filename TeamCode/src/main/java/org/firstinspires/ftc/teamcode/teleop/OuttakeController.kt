package org.firstinspires.ftc.teamcode.teleop

import com.acmerobotics.dashboard.config.Config
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.util.*

@Config object OuttakeControllerSettings {
    @JvmField var DELAY_MS = 500
    @JvmField var HIGH = Slide2.MAX_POSITION
    @JvmField var MID = 250
    @JvmField var LOW = Slide2.MIN_POSITION + 10
    @JvmField var SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = 200
}

class OuttakeController(
    private val slide: Slide2,
    private val carriage: CarriageDC,
    private val gamepad: Gamepad
    ): BasicPassdionComponent() {

    enum class State {
        IDLE,
        DUMPING,
        DUMPED,
        RETRACTING
    }

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
        // carriage.idle()
        opMode.register(dumpButton)
        opMode.register(highButton)
        opMode.register(midButton)
    }

    override fun update(opMode: PassdionOpMode) {
        when (state) {
            State.IDLE -> {
                carriage.idle()
            }
            State.DUMPING -> {
                carriage.dump()
                if (!carriage.isBusy) {
                    state = State.DUMPED
                }
            }
            State.DUMPED -> {
                if (timeInState >= OuttakeControllerSettings.DELAY_MS / 1000.0) {
                    state = State.RETRACTING
                }
            }
            State.RETRACTING -> {
                carriage.idle()
                if (!carriage.isBusy) {
                    state = State.IDLE
                    slideTargetPos = OuttakeControllerSettings.LOW
                }
            }
        }

        when {
            highButton.isPressed -> {
                slideTargetPos = if (slideTargetPos == OuttakeControllerSettings.HIGH) OuttakeControllerSettings.LOW else OuttakeControllerSettings.HIGH
            }
            midButton.isPressed -> {
                slideTargetPos = if (slideTargetPos == OuttakeControllerSettings.MID) OuttakeControllerSettings.LOW else OuttakeControllerSettings.MID
            }
        }

        if (dumpButton.isPressed && slide.currentPosition >= OuttakeControllerSettings.SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD) {
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
    }
}