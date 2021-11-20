package org.firstinspires.ftc.teamcode;

import android.util.Pair;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import java.util.ArrayList;
import java.util.List;

@Autonomous(name = "Rudimentary Autonomous")
public class RudimentaryAutonomous extends LinearOpMode {
    /** Motor names, powers **/
    public static Pair[] motors = {
            Pair.create("motor", 1.0),
            Pair.create("motor", 1.0)
    };

    /** Time robot should move for **/
    public static long time = 1000;

    // That's it! You can stop editing here.

    @Override
    public void runOpMode() throws InterruptedException {
        List<DcMotor> dcMotors = new ArrayList<>();
        for (Pair<String, Double> pair: motors) {
            dcMotors.add(hardwareMap.dcMotor.get(pair.first));
        }

        waitForStart();

        for (int i = 0; i < motors.length; ++i) {
            dcMotors.get(i).setPower((Double)(motors[i].second));
        }

        sleep(time);

        for (DcMotor dcMotor: dcMotors) {
            dcMotor.setPower(0);
        }
    }
}
