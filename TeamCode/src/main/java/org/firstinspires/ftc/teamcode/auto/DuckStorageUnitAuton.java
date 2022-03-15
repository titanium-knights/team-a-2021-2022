package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.util.*;

@Config public abstract class DuckStorageUnitAuton extends BaseAutonomousOpMode {
    @Override
    protected boolean grabsOnInit() {
        return true;
    }

    public static double PARK_BLUE_X = -63;
    public static double PARK_BLUE_Y = 35;
    public static double PARK_RED_X = -69; // nice
    public static double PARK_RED_Y = -60;

    private Vector2d getParkPos() {
        if (getColor() == Color.RED) {
            return new Vector2d(PARK_RED_X, PARK_RED_Y);
        } else {
            return new Vector2d(PARK_BLUE_X, PARK_BLUE_Y);
        }
    }

    @Override
    public void run() {
        double colorMultiplier = getColorMultiplier();
        ShippingHubLevel level = performAnalysis();

        telemetry.addData("Position", level);
        telemetry.update();

        TrajectorySequence sequence = duckDeliverySequence(level)
                .setTangent(Math.toRadians(-90) * colorMultiplier)
                .splineToConstantHeading(new Vector2d(-36, -56 * colorMultiplier), Math.toRadians(180))
                .addTemporalMarker(() -> capstone.setPosition(CapstoneMechanism2.getIdle()))
                .splineToConstantHeading(getParkPos(), Math.toRadians(90) * colorMultiplier)
                .waitSeconds(0.5)
                .addTemporalMarker(() -> claw.grab())
                .addTemporalMarker(() -> retraction.retract())
                .waitSeconds(0.5)
                .build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }
}
