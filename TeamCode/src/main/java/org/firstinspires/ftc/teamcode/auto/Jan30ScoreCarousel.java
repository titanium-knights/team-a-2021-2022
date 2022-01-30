package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;
import org.firstinspires.ftc.teamcode.util.Carousel;
import org.firstinspires.ftc.teamcode.util.MotorInterpolation;

@Config public abstract class Jan30ScoreCarousel extends Jan30ScoreBase {
    public static int CAROUSEL_DISTANCE = 54;
    public static int CAROUSEL_STRAFE_DISTANCE = 15;
    public static int CAROUSEL_FINAL_DISTANCE = 20;
    public static int CAROUSEL_SPIN_TIME = 3000;
    public static int CAROUSEL_RED_DISTANCE = 100;
    public static int STRAFE_DISTANCE = 48;
    public static int BACKUP_DISTANCE = 30;

    Carousel carousel;
    MotorInterpolation carouselInterpolation = new MotorInterpolation(0, 0.75);
    MotorInterpolation driveInterpolation = new MotorInterpolation(0.5, 1.5);

    @Override
    public void initialize() {
        super.initialize();
        carousel = new Carousel(hardwareMap);
    }

    @Override
    public void alignWithWall() {
        // If we're on red, don't align with the wall.
        if (!isRed()) {
            super.alignWithWall();
        }
    }

    @Override
    public void runAfterDump() {
        if (isRed()) {
            turn(163);

            drive.strafeLeftWithPower(0.5);
            sleep(CAROUSEL_RED_DISTANCE * MOTION_SCALE_FACTOR);
            driveInterpolation.setTarget(0);
            while (driveInterpolation.isBusy()) {
                drive.strafeLeftWithPower(driveInterpolation.getCurrent());
                idle();
            }
            drive.stop();
        } else {
            driveLeaningIntoWall(true);
            sleep(CAROUSEL_DISTANCE * MOTION_SCALE_FACTOR);
            drive.stop();

            drive.strafeRightWithPower(0.5);
            sleep(CAROUSEL_STRAFE_DISTANCE * MOTION_SCALE_FACTOR);
            drive.stop();

            drive.driveBackwardsWithPower(0.5);
            sleep(CAROUSEL_FINAL_DISTANCE * MOTION_SCALE_FACTOR);
            driveInterpolation.setTarget(0);
            while (driveInterpolation.isBusy()) {
                drive.driveBackwardsWithPower(driveInterpolation.getCurrent());
                idle();
            }
            drive.stop();
        }

        carouselInterpolation.setTarget(isRed() ? -0.75 : 0.75);
        while (carouselInterpolation.isBusy()) {
            carousel.motor.setPower(carouselInterpolation.getCurrent());
            idle();
        }
        carousel.motor.setPower(carouselInterpolation.getCurrent());
        sleep(CAROUSEL_SPIN_TIME);
        carousel.stop();

        if (isRed()) {
            drive.driveForwardsWithPower(0.5);
            sleep(BACKUP_DISTANCE * MOTION_SCALE_FACTOR);
            drive.stop();

            drive.strafeLeftWithPower(0.5);
            sleep(2000);
            drive.stop();
        } else {
            drive.driveForwardsWithPower(0.5);
            sleep(8 * MOTION_SCALE_FACTOR);
            drive.stop();

            drive.strafeRightWithPower(0.5);
            sleep(STRAFE_DISTANCE * MOTION_SCALE_FACTOR);
            drive.stop();

            drive.driveBackwardsWithPower(0.5);
            sleep(1500);
            drive.stop();
        }
    }
}
