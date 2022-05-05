package org.firstinspires.ftc.teamcode.test;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.IMU;

@TeleOp
public class IMUTest extends OpMode {
    MultipleTelemetry t;
    IMU imu;
    Telemetry dashT = FtcDashboard.getInstance().getTelemetry();
    @Override
    public void init(){
        imu.initializeIMU();
        t = new MultipleTelemetry(telemetry, dashT);
    }

    @Override
    public void loop(){
        t.addData("x angle",imu.getXAngle());
        t.addData("y angle",imu.getYAngle());
        t.addData("z angle",imu.getZAngle());
        t.update();
    }
}
