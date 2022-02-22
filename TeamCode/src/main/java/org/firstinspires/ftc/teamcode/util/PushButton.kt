package org.firstinspires.ftc.teamcode.util

class PushButton(val readState: () -> Boolean) {
    var previousState: Boolean = false
    var currentState: Boolean = readState()

    val isPressed: Boolean get() {
        return currentState && !previousState
    }

    fun update() {
        previousState = currentState
        currentState = readState()
    }
}

class ToggleButton(readState: () -> Boolean) {
    val pushButton = PushButton(readState)

    var isActive = false

    fun update() {
        if (pushButton.isPressed) {
            isActive = !isActive
        }
        pushButton.update()
    }
}