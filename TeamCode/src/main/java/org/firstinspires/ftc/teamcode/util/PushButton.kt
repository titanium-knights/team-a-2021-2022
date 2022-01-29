package org.firstinspires.ftc.teamcode.util

class PushButton(private val readState: () -> Boolean) {
    private var previousState: Boolean = false
    val currentState get() = readState()

    val isPressed: Boolean get() {
        return currentState && !previousState
    }

    fun update() {
        previousState = readState()
    }
}

class ToggleButton(readState: () -> Boolean) {
    val pushButton = PushButton(readState)

    var isActive = false

    override fun update() {
        if (pushButton.isPressed) {
            isActive = !isActive
        }
        pushButton.update()
    }
}