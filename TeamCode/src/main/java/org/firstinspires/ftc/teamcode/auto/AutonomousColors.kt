package org.firstinspires.ftc.teamcode.auto

import com.qualcomm.robotcore.eventloop.opmode.Autonomous

@Autonomous(name = "Autonomous - Park (Red)")
class Autonomous_Park_Red: Autonomous_Park() {
    override fun isRed() = true
}

@Autonomous(name = "Autonomous - Park (Blue)")
class Autonomous_Park_Blue: Autonomous_Park() {
    override fun isRed() = false
}

@Autonomous(name = "Autonomous - Jan 30 Carousel (Red)")
class Jan30ScoreCarouselRed: Jan30ScoreCarousel() {
    override fun isRed() = true
}

@Autonomous(name = "Autonomous - Jan 30 Carousel (Blue)")
class Jan30ScoreCarouselBlue: Jan30ScoreCarousel() {
    override fun isRed() = false
}

@Autonomous(name = "Autonomous - Jan 30 Warehouse (Red)")
class Jan30ScoreWarehouseRed: Jan30ScoreWarehouse() {
    override fun isRed() = true
}

@Autonomous(name = "Autonomous - Jan 30 Warehouse (Blue)")
class Jan30ScoreWarehouseBlue: Jan30ScoreWarehouse() {
    override fun isRed() = false
}