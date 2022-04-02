package org.firstinspires.ftc.teamcode.odometry;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.ThreeTrackingWheelLocalizer;
import com.acmerobotics.roadrunner.util.Angle;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.util.Encoder;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/*
 * Sample tracking wheel localizer implementation assuming the standard configuration:
 *
 *    /--------------\
 *    |     ____     |
 *    |     ----     |
 *    | ||        || |
 *    | ||        || |
 *    |              |
 *    |              |
 *    \--------------/
 *
 */
@Config
public class StandardTrackingWheelLocalizerIMU extends ThreeTrackingWheelLocalizer {
    public static double TICKS_PER_REV = 8192;
    public static double WHEEL_RADIUS = 0.6889764; // in
    public static double GEAR_RATIO = 1; // output (wheel) speed / input (encoder) speed

    public static double LATERAL_DISTANCE = 4.8689017098 ; // in; distance between the left and right wheels
    //4.8972898307766055 8 deg under
    //4.886553189202017 5 deg over

    //4.874816607843807

    public static double FORWARD_OFFSET = -0.27; // in; offset of the lateral wheel

    public static double X_MULTIPLIER = 1.007575126893;
    public static double Y_MULTIPLIER = 1.011849600972945;

    public static double MIN_IMU_UPDATE_INTERVAL = 0.5;
    public static double MIN_STABLE_HEADING_TIME = 0.2;
    public static double HEADING_EPSILON = Math.toRadians(0.5);

    private OdometryMecanumDriveIMU drive;
    private Encoder leftEncoder, rightEncoder, frontEncoder;

    private double baseExtHeading;

    private ElapsedTime lastIMUUpdateTimer = new ElapsedTime();
    private ElapsedTime stableHeadingTimer = new ElapsedTime();
    private double stableCheckHeading;

    private List<Double> cachedWheelPositions = Collections.emptyList();
    private List<Double> cachedWheelVelocities = Collections.emptyList();
    private boolean useCachedWheelData = false;


    public StandardTrackingWheelLocalizerIMU(HardwareMap hardwareMap, OdometryMecanumDriveIMU drive) {
        super(Arrays.asList(
                new Pose2d(0, LATERAL_DISTANCE / 2, 0), // left
                new Pose2d(0, -LATERAL_DISTANCE / 2, 0), // right
                new Pose2d(FORWARD_OFFSET, 0, Math.toRadians(90)) // front
        ));

        this.drive = drive;

        leftEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "fl"));
        rightEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "intake"));
        frontEncoder = new Encoder(hardwareMap.get(DcMotorEx.class, "bl"));

        leftEncoder.setDirection(Encoder.Direction.FORWARD);
        rightEncoder.setDirection(Encoder.Direction.FORWARD);
        frontEncoder.setDirection(Encoder.Direction.REVERSE);

    }

    private double getRawExternalHeading() {
        return drive.getRawExternalHeading();
    }

    private double getExternalHeading() {
        return Angle.norm(getRawExternalHeading() - baseExtHeading);
    }

    @Override
    public void setPoseEstimate(@NotNull Pose2d pose) {
        baseExtHeading = Angle.norm(getRawExternalHeading() - pose.getHeading());

        super.setPoseEstimate(pose);
    }

    @Override
    public void update() {
        double currentHeading = getPoseEstimate().getHeading();
        // reset timer and stableCheckHeading if our heading has changed too much
        if (Math.abs(Angle.normDelta(currentHeading - stableCheckHeading)) > HEADING_EPSILON) {
            stableHeadingTimer.reset();
            stableCheckHeading = currentHeading;
        }

        if (lastIMUUpdateTimer.seconds() > MIN_IMU_UPDATE_INTERVAL
                && stableHeadingTimer.seconds() > MIN_STABLE_HEADING_TIME) {
            lastIMUUpdateTimer.reset();

            // load in the latest wheel positions and update to apply to pose
            super.update();
            double extHeading = getExternalHeading();
            Pose2d pose = new Pose2d(getPoseEstimate().vec(), extHeading);
            super.setPoseEstimate(pose);

            // Don't update with new positions, but instead use previous (internally cached) wheel positions.
            // This ensures wheel movement isn't "lost" when the lastWheelPositions list (this list is internal to the
            // ThreeTrackingWheelLocalizer/super class) is emptied with our call to setPoseEstimate. Calling update
            // fills super's lastWheelPositions with the cached wheel positions and allows the later update calls to
            // increment the pose rather than simply filling the empty lastWheelPositions list.
            useCachedWheelData = true;
            super.update();
            useCachedWheelData = false;
        } else {
            super.update();
        }
    }

    public static double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @NonNull
    @Override
    public List<Double> getWheelPositions() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCurrentPosition()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCurrentPosition()) * Y_MULTIPLIER
        );
    }

    @NonNull
    @Override
    public List<Double> getWheelVelocities() {
        return Arrays.asList(
                encoderTicksToInches(leftEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(rightEncoder.getCorrectedVelocity()) * X_MULTIPLIER,
                encoderTicksToInches(frontEncoder.getCorrectedVelocity()) * Y_MULTIPLIER
        );
    }
}
