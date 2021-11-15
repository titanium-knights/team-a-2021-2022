package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class FlywheelIntake {
    DcMotor arm;
    CRServo flywheel;
    public FlywheelIntake(HardwareMap hardwareMap) {
        this.arm = hardwareMap.dcMotor.get("lift");
        this.flywheel = hardwareMap.crservo.get("intake");
    }

    public void liftArm() {
        arm.setPower(1);
    }

    public void lowerArm(){
        arm.setPower(-1);
    }

    public void stopArm() {
        arm.setPower(0);
    }

    public void intakeCargo() {
        flywheel.setPower(1);
    }

    public void placeCargo() {
        flywheel.setPower(-1);
    }

    public void stopCargo() {
        // HACK: This is incredibly hacky but at this point I've run out of options
        // This WILL cause problems with other servos on the same hub
        // But we don't have any other servos as of this writing
        // So, 🤷

        // TODO: Fix
        flywheel.getController().pwmDisable();
    }

}