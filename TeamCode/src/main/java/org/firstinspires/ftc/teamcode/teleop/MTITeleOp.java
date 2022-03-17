package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.util.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.ToggleButton;

@TeleOp(name = "MTI Tele-Op", group = "MTI")
public class MTITeleOp extends PassdionOpMode {
    @Override
    protected void registerComponents() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        MecanumDrive.FieldCentricComponent driving = drive.fieldCentricComponent();
        register(driving);

        ToggleButton slowMode = new ToggleButton(() -> gamepad1.dpad_up);
        register(slowMode);
        onLoop(() -> driving.multiplier = slowMode.isActive() ? 0.3 : 1.0);

        addTelemetryData("IMU X Angle", () -> driving.imu.getXAngle());
        addTelemetryData("IMU Y Angle", () -> driving.imu.getYAngle());
        addTelemetryData("IMU Z Angle", () -> driving.imu.getZAngle());
    }
}
