package org.firstinspires.ftc.teamcode.test;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
@Disabled
public class IntakeTest extends OpMode {
    DcMotor intake;

    @Override
    public void init() {
        intake = hardwareMap.get(DcMotor.class,"intake");
    }

    @Override
    public void loop() {
        if(gamepad1.x){
            intake.setPower(1);
        }
        if(gamepad1.b){
            intake.setPower(-1);
        }
    }
}
