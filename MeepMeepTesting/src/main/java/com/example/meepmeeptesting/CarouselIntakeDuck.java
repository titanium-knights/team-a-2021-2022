package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class CarouselIntakeDuck {
    static int colorMultiplier = 1;
    public static void main(String[] args) {
        Pose2d startingPosition = new Pose2d(-36, -63 * colorMultiplier, Math.toRadians(-90) * colorMultiplier);
        MeepMeep meepMeep = new MeepMeep(500);
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 9)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startingPosition)

                        .build()
                );

    }
    public enum Color {
        BLUE,
        RED
    }

    public enum ShippingHubLevel {
        LOW,
        MID,
        HIGH
    }
    public static double HIGH_POS = -57;
    public static double MID_POS = -64;
    public static double LOW_POS = -57;
//    static Pose2d poseForDumping(double x, ShippingHubLevel level, double yOffset) {
//        double destinationY;
//        if (level == ShippingHubLevel.HIGH) {
//            destinationY = HIGH_POS;
//        } else if (level == ShippingHubLevel.MID) {
//            destinationY = MID_POS;
//        } else {
//            destinationY = LOW_POS;
//        }
//        double y = ((destinationY + yOffset) * colorMultiplier);
//        return new Pose2d((x - (getColor() == Color.RED ? RED_OFFSET : 0)), (y - (getColor() == Color.RED ? RED_OFFSET_Y : 0)), Math.toRadians(90) * getColorMultiplier());
//    };
}
