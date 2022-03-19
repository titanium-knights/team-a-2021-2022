package org.firstinspires.ftc.teamcode.teleop

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.Gamepad
import org.firstinspires.ftc.teamcode.util.PushButton

abstract class PassdionOpMode: OpMode() {
    private val components = mutableListOf<PassdionComponent>()

    protected abstract fun registerComponents()

    fun register(component: PassdionComponent) {
        if (!components.contains(component)) {
            components.add(component)
            component.init(this)
        }
    }

    fun onLoop(function: Runnable) {
        register(LoopFunction { function.run() })
    }

    fun addTelemetryData(name: String, data: () -> Any) {
        onLoop {
            telemetry.addData(name, data())
        }
    }

    fun deregister(component: PassdionComponent) {
        if (components.remove(component)) {
            component.cleanup(this)
        }
    }

    override fun init() {
        registerComponents()
    }

    override fun init_loop() {
        super.init_loop()
        components.forEach {
            it.initLoop(this)
        }
    }

    override fun start() {
        super.start()
        components.forEach {
            it.initLoop(this)
        }
    }

    override fun loop() {
        components.forEach {
            it.update(this)
        }
        telemetry.update()
    }

    override fun stop() {
        components.forEach {
            it.cleanup(this)
        }

        components.clear()
    }
}

interface PassdionComponent {
    fun init(opMode: PassdionOpMode)
    fun initLoop(opMode: PassdionOpMode)
    fun start(opMode: PassdionOpMode)
    fun update(opMode: PassdionOpMode)
    fun cleanup(opMode: PassdionOpMode)
}

abstract class BasicPassdionComponent: PassdionComponent {
    abstract override fun init(opMode: PassdionOpMode)
    override fun initLoop(opMode: PassdionOpMode) {}
    override fun start(opMode: PassdionOpMode) {}
    abstract override fun update(opMode: PassdionOpMode)
    override fun cleanup(opMode: PassdionOpMode) {}
}

class LoopFunction(val onLoop: PassdionOpMode.() -> Unit): BasicPassdionComponent() {
    override fun init(opMode: PassdionOpMode) {}
    override fun update(opMode: PassdionOpMode) {
        onLoop(opMode)
    }
}