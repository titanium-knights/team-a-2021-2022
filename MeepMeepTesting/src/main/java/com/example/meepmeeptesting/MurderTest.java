package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.entity.TrajectorySequenceEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MurderTest {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);
        meepMeep.setBackgroundAlpha(1.0f);
        meepMeep.setTheme(new ColorSchemeBlueDark());
        //meepMeep.setBotDimensions(12, 18);

//        meepMeep.setConstraints(
//                60, // max velocity
//                60, // max acceleration
//                Math.toRadians(180), // max angular velocity
//                Math.toRadians(180), // max angular acceleration
//                15 // track width
//        );

//        meepMeep.followTrajectorySequence(drive ->
//                drive.trajectorySequenceBuilder(new Pose2d(12, -66, 0))
//                        // Build your trajectory here...
//
//                        .waitSeconds(0.5)
//                        .setTangent(Math.toRadians(90))
//
//                        .splineToLinearHeading(new Pose2d(-12, -41,Math.toRadians(-90)), Math.toRadians(90))
//
//                        .waitSeconds(0.5)
//                        .forward(3)
//                        .turn(Math.toRadians(90))
//
//                        .lineTo(new Vector2d(36, -44))
//                        // .waitSeconds(1)
//                        // .lineTo(new Vector2d(-60, -36))
//                        // .setTangent(Math.toRadians(180))
//                        // .splineToConstantHeading(new Vector2d(-36, -28), Math.toRadians(-90))
//                        // .splineToConstantHeading(new Vector2d(12, -40), Math.toRadians(12))
//                        .build()
//        );

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                .setConstraints(60,60,Math.toRadians(180),Math.toRadians(180),15)
        .followTrajectorySequence(drive ->
                drive.trajectorySequenceBuilder(new Pose2d(-36, -66 , 0))
                        .lineTo(new Vector2d(-56, -66 ))
                        .waitSeconds(0.5)
                        .splineToConstantHeading(new Vector2d(-40, -56 ), Math.toRadians(30))
                        .splineToSplineHeading(new Pose2d(-12, -41, Math.toRadians(90)),
                                Math.toRadians(90))
                        .waitSeconds(0.5)
                        .forward(-3)
                        .turn(Math.toRadians(90))
                        .splineToConstantHeading(new Vector2d(-60, -35 ), Math.toRadians(0))
                        .build());

        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}
