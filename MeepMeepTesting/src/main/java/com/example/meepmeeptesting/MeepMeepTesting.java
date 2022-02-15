package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.entity.TrajectorySequenceEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepTesting {
    public static Pose2d carousel = new Pose2d(-55, -60,Math.toRadians(-90));
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);
        Pose2d startingPosition = new Pose2d(-36,-60, Math.toRadians(-90));
        Pose2d leftOfRedHub = new Pose2d(-36,-24,Math.toRadians(180));
        Telemetry telemetry = new Telemetry();
        double timeAtHub1 = 3;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 9)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startingPosition)
                                .addTemporalMarker(() -> System.out.println("################################"))
                                .lineToSplineHeading(carousel).setReversed(true)
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Carousel", "Setting Power to 1");
                                    // Spin the carousel
                                    // Set Carousel Power to 1
                                })
                                .waitSeconds(2.75)
                                .UNSTABLE_addTemporalMarkerOffset(-0.25,() -> {
                                    telemetry.addData("Carousel", "Setting Power to 0");
                                    //Stop the carousel
                                    //Set Carousel Power to 0
                                })
                                .lineToSplineHeading(leftOfRedHub).setReversed(true)
                                .UNSTABLE_addTemporalMarkerOffset(-1, () -> {
                                    telemetry.addData("Lift", "Extending");
                                })
                                .waitSeconds(timeAtHub1)
                                .UNSTABLE_addTemporalMarkerOffset(-timeAtHub1+1.75, () -> {
                                    telemetry.addData("Claw", "Dump");
                                })
                                .UNSTABLE_addTemporalMarkerOffset(-timeAtHub1+2.25, () -> {
                                    telemetry.addData("Claw", "Normal");
                                })
                                .UNSTABLE_addTemporalMarkerOffset(-timeAtHub1+2.67, () -> {
                                    telemetry.addData("Lift", "Retracting");
                                })
                                .setReversed(false)
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Intake", "Spinning");
                                }
                                )
                                .splineTo(new Vector2d(-60,-40), Math.toRadians(270))
                                .splineTo(new Vector2d(-60,-60), Math.toRadians(270))
                                .lineToSplineHeading(leftOfRedHub).setReversed(true)
                                .UNSTABLE_addTemporalMarkerOffset(-1.5, () -> {
                                    telemetry.addData("Intake", "Stopping");
                                })
                                .UNSTABLE_addTemporalMarkerOffset(-1, () -> {
                                    telemetry.addData("Lift", "Extending");
                                })
                                .waitSeconds(timeAtHub1)
                                .UNSTABLE_addTemporalMarkerOffset(-timeAtHub1+1.75, () -> {
                                    telemetry.addData("Claw", "Dump");
                                })
                                .UNSTABLE_addTemporalMarkerOffset(-timeAtHub1+2.25, () -> {
                                    telemetry.addData("Claw", "Normal");
                                })
                                .UNSTABLE_addTemporalMarkerOffset(-timeAtHub1+2.67, () -> {
                                    telemetry.addData("Lift", "Retracting");
                                })
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Parking Sequence", "Going to Warehouse");
                                })
                                .lineToSplineHeading(new Pose2d(-60,-36,Math.toRadians(180)))
                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }
}
class Telemetry{
    public void addData(String s, Object o){
        System.out.println(s + ": " + o);
    }
}