package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.*;
import org.firstinspires.ftc.teamcode.teleop.BasicPassdionComponent;
import org.firstinspires.ftc.teamcode.teleop.PassdionOpMode;
import org.jetbrains.annotations.NotNull;

@Config
public class ClawIntake {
    public Servo claw;
    public static double releasePos = 1.0;
    public static double ballPos = 0.8;
    public static double grabPos = 0.75;
    public static double armMultiplier = -0.5;
    public ClawIntake(HardwareMap hardwareMap) {

        this.claw = hardwareMap.servo.get("claw"); //slide open and close
    }

    public void grab() {
        claw.setPosition(grabPos);
    }

    public void grabBall() {
        claw.setPosition(ballPos);
    }

    public void release() {
        claw.setPosition(releasePos);
    }

    public void incrementClawPosition(double amount) {
        double position = claw.getPosition();

        if (amount > 0 && position >= Math.max(releasePos, grabPos)) return;
        if (amount < 0 && position <= Math.min(releasePos, grabPos)) return;

        claw.setPosition(position + amount);
    }

    public class Controller extends BasicPassdionComponent {
        private ToggleButton clawButton;

        public Controller(Gamepad gamepad) {
            clawButton = new ToggleButton(() -> gamepad.a);
        }

        @Override
        public void init(@NotNull PassdionOpMode opMode) {
            opMode.register(clawButton);
        }

        @Override
        public void update(@NotNull PassdionOpMode opMode) {
            if (clawButton.isActive()) {
                grab();
            } else {
                release();
            }
        }
    }

}
