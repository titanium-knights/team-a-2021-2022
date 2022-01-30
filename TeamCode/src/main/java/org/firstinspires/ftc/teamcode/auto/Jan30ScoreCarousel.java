package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.util.Carousel;

@Config public abstract class Jan30ScoreCarousel extends Jan30ScoreBase {
    public static long CAROUSEL_DISTANCE = 36;
    public static long CAROUSEL_SPIN_TIME = 2000;
    public static long STRAFE_DISTANCE = 30;

    Carousel carousel;

    @Override
    public void initialize() {
        super.initialize();
        carousel = new Carousel(hardwareMap);
    }

    @Override
    public void runAfterDump() {
        driveLeaningIntoWall(true);
        sleep(CAROUSEL_DISTANCE * MOTION_SCALE_FACTOR);
        drive.stop();

        carousel.spin(false);
        sleep(CAROUSEL_SPIN_TIME);
        carousel.stop();

        drive.driveForwardsWithPower(0.5);
        sleep(8 * MOTION_SCALE_FACTOR);
        drive.stop();

        if (isRed()) {
            drive.strafeLeftWithPower(0.5);
        } else {
            drive.strafeRightWithPower(0.5);
        }
        sleep(STRAFE_DISTANCE * MOTION_SCALE_FACTOR);
        drive.stop();

        drive.driveBackwardsWithPower(0.5);
        sleep(1500);
        drive.stop();
    }
}
