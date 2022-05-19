package org.firstinspires.ftc.teamcode.demo

import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import org.firstinspires.ftc.teamcode.util.Slide2

@Autonomous(name = "Dance", group = "Demo")
class DanceAuton: TimepointAuton() {
    override fun setup() {
        at(0) {
            drive.turnRightWithPower(1.0)
            intake.spin()
            slideTargetPosition = (Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2
        }

        after(1.0) {
            drive.turnLeftWithPower(1.0)
            intake.outtake()
            slideTargetPosition = Slide2.MIN_POSITION + 10
        }

        after(1.0) {
            drive.driveForwardsWithPower(1.0)
            intake.stop()
        }

        after(0.5) {
            drive.driveForwardsWithPower(-1.0)
        }

        after(0.5) {
            drive.stop()
            slideTargetPosition = Slide2.MAX_POSITION - 10
        }

        after(1.0) {
            slideTargetPosition = Slide2.MIN_POSITION + 10
            drive.driveForwardsWithPower(-1.0)
        }

        after(0.5) {
            drive.driveForwardsWithPower(1.0)
        }

        after(0.5) {
            drive.stop()
            restart()
        }
    }
}