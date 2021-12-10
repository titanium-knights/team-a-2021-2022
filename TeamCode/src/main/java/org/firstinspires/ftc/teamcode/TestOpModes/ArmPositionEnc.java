package org.firstinspires.ftc.teamcode.TestOpModes;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ClawIntake;

@Config
@TeleOp
public class ArmPositionEnc extends OpMode {
    ClawIntake intake;
    public static int armPos=0;
    public static double armPwr=0;

    FtcDashboard dashboard = FtcDashboard.getInstance();
    Telemetry dashTelemetry = dashboard.getTelemetry();

    @Override
    public void init(){
        intake = new ClawIntake(hardwareMap);
    }

    @Override
    public void loop() {
        intake.setArmPosition(armPos);
        intake.setArmPower(armPwr);
        if(!intake.intakeMotorIsBusy()){
            intake.setArmPosition((int) intake.getArmPosition());
        }
        dashTelemetry.addData("Arm Pos",intake.getArmPosition());
        dashTelemetry.update();

    }
}
