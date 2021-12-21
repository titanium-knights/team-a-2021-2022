package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.Disabled

@Autonomous(name = "Autonomous - Park (Red)")
class Autonomous_Park_Red: Autonomous_Park() {
    override fun isRed() = true
}

@Autonomous(name = "Autonomous - Park (Blue)")
class Autonomous_Park_Blue: Autonomous_Park() {
    override fun isRed() = false
}