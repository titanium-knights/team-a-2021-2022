package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.entity.TrajectorySequenceEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class CyclingTest {
    public static void main(String[] args) {
        int multiplier = -1;
        Pose2d carousel = new Pose2d(-55, -60,Math.toRadians(-90*multiplier));
        MeepMeep meepMeep = new MeepMeep(500);
        Pose2d startingPosition = new Pose2d(6,-60*multiplier, Math.toRadians(-90*multiplier));
        Pose2d rightOfRedHub = new Pose2d(-12,-48*multiplier,Math.toRadians(90*multiplier));

        Pose2d redWarehouseIntermediate = new Pose2d(12,-65.15*multiplier,Math.toRadians(0));
        Vector2d redWarehouseIntermediateVec = new Vector2d(redWarehouseIntermediate.getX(),redWarehouseIntermediate.getY());
        Pose2d redWarehouse = new Pose2d(48,-65.15*multiplier,Math.toRadians(0));
        Telemetry telemetry = new Telemetry();
        double timeAtHub = 3;
        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 9)
                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(startingPosition)
                                .addTemporalMarker(() -> {
                                    System.out.println("#########################################");
                                    telemetry.addData("Lift", "Extending");
                                })
                                .setReversed(true)
                                .lineToSplineHeading(rightOfRedHub)
                                .waitSeconds(timeAtHub)
                                .UNSTABLE_addTemporalMarkerOffset((-timeAtHub) + 1, () -> {
                                    telemetry.addData("Claw", "Dump");
                                })
                                .UNSTABLE_addTemporalMarkerOffset((-timeAtHub) + 1.5, () -> {
                                    telemetry.addData("Claw", "Not Dump");
                                })
                                .UNSTABLE_addTemporalMarkerOffset((-timeAtHub) + 2, () -> {
                                    telemetry.addData("Lift", "Retracting");
                                })

                                //start cycle 1
                                .setReversed(false)
                                .splineToLinearHeading(redWarehouseIntermediate, Math.toRadians(0))
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Intake ", "On");
                                })
                                .splineToLinearHeading(redWarehouse,0)
                                .setReversed(true)
                                .splineToLinearHeading(redWarehouseIntermediate,0)
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Intake ", "reverse");
                                })
                                .waitSeconds(0.5)
                                .addTemporalMarker(()->{
                                    telemetry.addData("Intake", "Off");
                                    telemetry.addData("Lift", "Extending");
                                })
                                .splineToLinearHeading(rightOfRedHub, Math.toRadians(0))
                                .waitSeconds(timeAtHub)
                                .UNSTABLE_addTemporalMarkerOffset((-timeAtHub) + 1, () -> {
                                    telemetry.addData("Claw", "Dump");
                                })
                                .UNSTABLE_addTemporalMarkerOffset((-timeAtHub) + 1.5, () -> {
                                    telemetry.addData("Claw", "Not Dump");
                                })
                                .UNSTABLE_addTemporalMarkerOffset((-timeAtHub) + 2, () -> {
                                    telemetry.addData("Lift", "Retracting");
                                })
                                //end cycle 1


                                .build()
                );


        meepMeep.setBackground(MeepMeep.Background.FIELD_FREIGHTFRENZY_ADI_DARK)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();

    }
}
