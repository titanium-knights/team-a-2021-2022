package org.firstinspires.ftc.teamcode

import com.qualcomm.robotcore.eventloop.opmode.Disabled
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.firstinspires.ftc.teamcode.util.IMU

@TeleOp(name = "TeleOpMode (Field-Centric)")
@Disabled
class FieldCentricTeleOpMode: TeleOpMode() {
    private lateinit var imu: IMU

    override fun init() {
        super.init()
        imu = IMU(hardwareMap)
        imu.initializeIMU()
    }

    override fun controlDrivetrain(preferredSpeed: Double) = drive.teleOpFieldCentric(gamepad1, imu, preferredSpeed)
}

@TeleOp(name = "TeleOpMode (Robot-Centric)")
@Disabled
class RobotCentricTeleOpMode: TeleOpMode() {
    override fun controlDrivetrain(preferredSpeed: Double) = drive.teleOpRobotCentric(gamepad1, preferredSpeed)
}