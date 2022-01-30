package org.firstinspires.ftc.teamcode.auto;

import com.acmerobotics.dashboard.config.Config;

@Config public abstract class Jan30ScoreWarehouse extends Jan30ScoreBase {
    public static long PARK_DISTANCE = 72;

    @Override
    public void runAfterDump() {
        driveLeaningIntoWall();
        sleep(PARK_DISTANCE * MOTION_SCALE_FACTOR);
        drive.stop();
    }
}
