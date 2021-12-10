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

@Autonomous(name = "Duck Murder Autonomous (Red)")
class DuckMurderAutonRed: DuckMurderAuton() {
    override fun isRed() = true
}

@Autonomous(name = "Duck Murder Autonomous (Blue)")
@Disabled
class DuckMurderAutonBlue: DuckMurderAuton() {
    override fun isRed() = false
}