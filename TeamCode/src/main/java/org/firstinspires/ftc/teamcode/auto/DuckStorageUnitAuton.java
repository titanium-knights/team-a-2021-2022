package org.firstinspires.ftc.teamcode.auto;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import com.acmerobotics.roadrunner.geometry.Vector2d;
import org.firstinspires.ftc.teamcode.util.*;

public abstract class DuckStorageUnitAuton extends BaseAutonomousOpMode {
    @Override
    protected boolean grabsOnInit() {
        return true;
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
                .splineToConstantHeading(new Vector2d(-63, -35 * colorMultiplier), Math.toRadians(90) * colorMultiplier)
                .waitSeconds(0.5)
                .addTemporalMarker(() -> retraction.retract())
                .waitSeconds(0.5)
                .build();

        drive.setPoseEstimate(sequence.start());
        drive.followTrajectorySequence(sequence);
    }
}
