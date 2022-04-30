package org.firstinspires.ftc.teamcode.demo

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.teamcode.util.*
import java.util.*

data class Timepoint(val seconds: Double, val run: () -> Unit): Comparable<Timepoint> {
    override fun compareTo(other: Timepoint): Int {
        return if (seconds > other.seconds) {
            1
        } else if (seconds < other.seconds) {
            -1
        } else {
            0
        }
    }
}

abstract class TimepointAuton: LinearOpMode() {
    abstract fun setup()

    val timepoints = TreeSet<Timepoint>()
    lateinit var drive: MecanumDrive
    lateinit var slide: Slide2
    lateinit var carriage: CarriageDC
    lateinit var odometryRetraction: OdometryRetraction
    lateinit var carousel: Carousel
    lateinit var elapsedTime: ElapsedTime
    lateinit var intake: TubeIntake

    var slideTargetPosition: Int? = null
        set(value) {
            if (value == null) {
                slide.power = 0.0
            }
            field = value
        }

    fun at(seconds: Double, run: () -> Unit) {
        timepoints.add(Timepoint(seconds, run))
    }

    fun at(seconds: Int, run: () -> Unit) {
        at(seconds.toDouble(), run)
    }

    fun after(seconds: Double, run: () -> Unit): Double {
        val last = timepoints.last()?.seconds ?: 0.0
        val time = last + seconds
        timepoints.add(Timepoint(time, run))
        return last
    }

    fun after(seconds: Int, run: () -> Unit): Double {
        return after(seconds.toDouble(), run)
    }

    var currentTimepoints = TreeSet<Timepoint>()

    fun restart() {
        elapsedTime.reset()
        currentTimepoints = TreeSet(timepoints)
    }

    override fun runOpMode() {
        drive = MecanumDrive(hardwareMap)
        slide = Slide2(hardwareMap)
        carriage = CarriageDC(hardwareMap)
        odometryRetraction = OdometryRetraction(hardwareMap)
        carousel = Carousel(hardwareMap)
        elapsedTime = ElapsedTime()
        intake = TubeIntake(hardwareMap)

        setup()

        waitForStart()
        restart()

        while (timepoints.isNotEmpty() && opModeIsActive() && !Thread.currentThread().isInterrupted) {
            val (seconds, run) = currentTimepoints.pollFirst()
            while (elapsedTime.seconds() < seconds && opModeIsActive() && !Thread.currentThread().isInterrupted) {
                if (slideTargetPosition != null) {
                    slide.runToPosition(slideTargetPosition!!)
                }
            }
            if (!opModeIsActive() || Thread.currentThread().isInterrupted) {
                break
            }
            run()
        }
    }
}