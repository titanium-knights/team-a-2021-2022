package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.teleop.BasicPassdionComponent;
import org.firstinspires.ftc.teamcode.teleop.PassdionOpMode;
import org.jetbrains.annotations.NotNull;

@Config
public class TapeMeasureMechanism {
    public Servo pitchServo;
    public Servo yawServo;
    public CRServo tapeServo;

    public TapeMeasureMechanism(HardwareMap hardwareMap) {
        pitchServo = hardwareMap.servo.get("tapepitch");
        yawServo = hardwareMap.servo.get("tapeyaw");
        tapeServo = hardwareMap.crservo.get("tape");
    }

    public static double RETRACT_POWER = -1.0;
    public static double EXTEND_POWER = 1.0;
    public static double ANGLE_ADJUSTMENT = 0.02;

    public double getPitch() {
        return pitchServo.getPosition();
    }

    public void setPitch(double pitch) {
        pitchServo.setPosition(pitch);
    }

    public double getYaw() {
        return yawServo.getPosition();
    }

    public void setYaw(double yaw) {
        yawServo.setPosition(yaw);
    }

    public void retract(double multiplier) {
        tapeServo.setPower(RETRACT_POWER * multiplier);
    }

    public void retract() {
        retract(1);
    }

    public void extend(double multiplier) {
        tapeServo.setPower(EXTEND_POWER * multiplier);
    }

    public void extend() {
        extend(1);
    }

    public void stopTape() {
        tapeServo.setPower(0);
    }

    public class Controller extends BasicPassdionComponent {
        private Gamepad gamepad;
        public double multiplier = 1;

        public Controller(Gamepad gamepad) {
            this.gamepad = gamepad;
        }

        @Override
        public void init(@NotNull PassdionOpMode opMode) {}

        @Override
        public void update(@NotNull PassdionOpMode opMode) {
            double pitch = getPitch();
            if (gamepad.dpad_up) {
                setPitch(pitch + ANGLE_ADJUSTMENT * multiplier);
            } else if (gamepad.dpad_down) {
                setPitch(pitch - ANGLE_ADJUSTMENT * multiplier);
            }

            double yaw = getYaw();
            if (gamepad.dpad_left) {
                setYaw(yaw - ANGLE_ADJUSTMENT * multiplier);
            } else if (gamepad.dpad_right) {
                setYaw(yaw + ANGLE_ADJUSTMENT * multiplier);
            }

            if (gamepad.left_bumper) {
                retract(multiplier);
            } else if (gamepad.right_bumper) {
                extend(multiplier);
            } else {
                stopTape();
            }
        }
    }
}
