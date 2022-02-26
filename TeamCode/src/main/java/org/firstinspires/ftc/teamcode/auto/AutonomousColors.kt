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

@Autonomous(name = "Autonomous - Jan 30 Carousel (Red)")
@Disabled
class Jan30ScoreCarouselRed: Jan30ScoreCarousel() {
    override fun isRed() = true
}

@Autonomous(name = "Autonomous - Jan 30 Carousel (Blue)")
@Disabled
class Jan30ScoreCarouselBlue: Jan30ScoreCarousel() {
    override fun isRed() = false
}

@Autonomous(name = "Autonomous - Jan 30 Warehouse (Red)")
@Disabled
class Jan30ScoreWarehouseRed: Jan30ScoreWarehouse() {
    override fun isRed() = true
}

@Autonomous(name = "Autonomous - Jan 30 Warehouse (Blue)")
@Disabled
class Jan30ScoreWarehouseBlue: Jan30ScoreWarehouse() {
    override fun isRed() = false
}

@Autonomous(name = "Spare Duck (No Carousel) - Blue")
class DuckSpareAutonBlue: DuckSpareAuton() {
    override fun getColorMultiplier(): Double {
        return -1.0
    }
}

@Autonomous(name = "Spare Duck (No Carousel) - Red")
class DuckSpareAutonRed: DuckSpareAuton() {
    override fun getColorMultiplier(): Double {
        return 1.0
    }
}

@Autonomous(name = "MURDER Duck & Warehouse (Carousel) - Blue")
class DuckWarehouseAutonBlue: DuckWarehouseAuton() {
    override fun getColorMultiplier(): Double {
        return -1.0
    }
}

@Autonomous(name = "MURDER Duck & Warehouse (Carousel) - Red")
class DuckWarehouseAutonRed: DuckWarehouseAuton() {
    override fun getColorMultiplier(): Double {
        return 1.0
    }
}

@Autonomous(name = "MURDER Duck & Storage Area (Carousel) - Blue")
class DuckStorageUnitAutonBlue: DuckStorageUnitAuton() {
    override fun getColorMultiplier(): Double {
        return -1.0
    }
}

@Autonomous(name = "MURDER Duck & Storage Area (Carousel) - Red")
class DuckStorageUnitAutonRed: DuckStorageUnitAuton() {
    override fun getColorMultiplier(): Double {
        return 1.0
    }
}