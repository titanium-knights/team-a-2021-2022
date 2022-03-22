package org.firstinspires.ftc.teamcode.util;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.teamcode.teleop.BasicPassdionComponent;
import org.firstinspires.ftc.teamcode.teleop.PassdionOpMode;
import org.jetbrains.annotations.NotNull;

@Config @Deprecated public class CapstoneMechanism2 {
    DcMotor motor;
    public static double power = 0.8;
    public static int idle = -490;
    public static int pickup = -2480;

    public CapstoneMechanism2(HardwareMap hardwareMap, boolean resetEncoders){
        motor = hardwareMap.dcMotor.get("capstone");
        if (resetEncoders) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public CapstoneMechanism2(HardwareMap hardwareMap) {
        this(hardwareMap, true);
    }

    public void setPosition(int pos){
        motor.setTargetPosition(pos);
        motor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        motor.setPower(power);
    }

    public void setManualPower(double pow){
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        motor.setPower(pow);
    }

    public int getPosition(){
        return motor.getCurrentPosition();
    }

    public static int getIdle(){
        return idle;
    }
    public static int getPickup() { return pickup; }

    public class Controller extends BasicPassdionComponent {
        private Gamepad gamepad;
        private boolean movedManually = false;

        public double multiplier = 1.0;

        public Controller(Gamepad gamepad) {
            this.gamepad = gamepad;
        }

        @Override
        public void init(@NotNull PassdionOpMode opMode) {
            movedManually = false;
        }

        @Override
        public void start(@NotNull PassdionOpMode opMode) {
            super.start(opMode);
            setPosition(getIdle());
        }

        @Override
        public void update(@NotNull PassdionOpMode opMode) {
            int pos = getPosition();
            if (gamepad.right_bumper) {
                if (pos <= CapstoneMechanism2.getIdle()) {
                    setManualPower(CapstoneMechanism2.power * multiplier);
                } else {
                    setManualPower(0);
                    gamepad.rumble(50);
                }
                movedManually = true;
            } else if (gamepad.left_bumper) {
                if (pos >= CapstoneMechanism2.getPickup()) {
                    setManualPower(-CapstoneMechanism2.power * multiplier);
                } else {
                    setManualPower(0);
                    gamepad.rumble(50);
                }
                movedManually = true;
            } else if (movedManually) {
                setManualPower(0);
            }
        }
    }
}
