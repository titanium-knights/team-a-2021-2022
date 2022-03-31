package com.example.meepmeeptesting;


import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.entity.TrajectorySequenceEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MurderCycleTest {
    public static void main(String[] args) {
        int multiplier = -1;
        Pose2d carousel = new Pose2d(-55, 60,Math.toRadians(180));
        MeepMeep meepMeep = new MeepMeep(500);
        Pose2d startingPosition = new Pose2d(-36,60, Math.toRadians(90));
        Pose2d rightOfBlueHub = new Pose2d(0,45,Math.toRadians(90));

        Pose2d blueWarehousePreIntermediate = new Pose2d(0,60,Math.toRadians(0));
        Pose2d blueWarehouseIntermediate = new Pose2d(9,65.15,Math.toRadians(0));
        Pose2d blueWarehouse = new Pose2d(48,65.15,Math.toRadians(0));
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
                                .back(6)
                                .splineToSplineHeading(carousel, Math.toRadians(180))
                                .waitSeconds(3)
                                .lineToSplineHeading(rightOfBlueHub)
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
                                .splineToSplineHeading(blueWarehousePreIntermediate, Math.toRadians(90))
                                .splineToConstantHeading(blueWarehouseIntermediate.vec(), blueWarehouseIntermediate.getHeading())
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Intake ", "On");
                                })
                                .splineToSplineHeading(blueWarehouse,0)
                                .setReversed(true)
                                .splineToLinearHeading(blueWarehouseIntermediate,Math.toRadians(180))
                                .addTemporalMarker(() -> {
                                    telemetry.addData("Intake ", "reverse");
                                })
                                .addTemporalMarker(()->{
                                    telemetry.addData("Intake", "Off");
                                    telemetry.addData("Lift", "Extending");
                                })
                                .splineToSplineHeading(rightOfBlueHub, -rightOfBlueHub.getHeading())
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
                                .setReversed(false)
                                .splineToSplineHeading(blueWarehouseIntermediate, Math.toRadians(0))
                                .splineToSplineHeading(blueWarehouse,0)
                                .setReversed(true)
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

