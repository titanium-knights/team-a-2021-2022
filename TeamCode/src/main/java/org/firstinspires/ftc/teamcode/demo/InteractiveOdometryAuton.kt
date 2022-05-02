package org.firstinspires.ftc.teamcode.demo

import com.acmerobotics.dashboard.config.Config
import com.acmerobotics.roadrunner.geometry.Pose2d
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.OpMode
import org.firstinspires.ftc.teamcode.odometry.OdometryMecanumDrive
import org.firstinspires.ftc.teamcode.util.OdometryRetraction

@Config object InteractiveOdometryAutonVars {
    @JvmField var x = 0.0
    @JvmField var y = 0.0
    @JvmField var heading = 0.0
    @JvmField var operationType = ""
}

@Autonomous(name = "Interactive", group = "Demo")
class InteractiveOdometryAuton: OpMode() {
    lateinit var drive: OdometryMecanumDrive
    lateinit var retraction: OdometryRetraction

    override fun init() {
        drive = OdometryMecanumDrive(hardwareMap)
        drive.poseEstimate = Pose2d()

        retraction = OdometryRetraction(hardwareMap)
        retraction.extend()
    }

    override fun loop() {
        when (InteractiveOdometryAutonVars.operationType) {
            "move" -> {
                drive.interrupt()
                drive.followTrajectorySequenceAsync(drive.trajectorySequenceBuilder(drive.poseEstimate)
                    .lineToLinearHeading(Pose2d(InteractiveOdometryAutonVars.x, InteractiveOdometryAutonVars.y, InteractiveOdometryAutonVars.heading))
                    .build())
                InteractiveOdometryAutonVars.operationType = ""
            }
            "reset" -> {
                drive.interrupt()
                drive.setMotorPowers(0.0, 0.0, 0.0, 0.0)
                drive.poseEstimate = Pose2d(InteractiveOdometryAutonVars.x, InteractiveOdometryAutonVars.y, drive.poseEstimate.heading)
                InteractiveOdometryAutonVars.operationType = ""
            }
            "reset_with_heading" -> {
                drive.interrupt()
                drive.setMotorPowers(0.0, 0.0, 0.0, 0.0)
                drive.poseEstimate = Pose2d(InteractiveOdometryAutonVars.x, InteractiveOdometryAutonVars.y, InteractiveOdometryAutonVars.heading)
                InteractiveOdometryAutonVars.operationType = ""
            }
            else -> {}
        }

        drive.update()
    }
}