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
    public CRServo pitchServo;
    public CRServo yawServo;
    public CRServo tapeServo;

    public TapeMeasureMechanism(HardwareMap hardwareMap) {
        pitchServo = hardwareMap.crservo.get("tapepitch");
        yawServo = hardwareMap.crservo.get("tapeyaw");
        tapeServo = hardwareMap.crservo.get("tape");
    }

    public static double RETRACT_POWER = -1.0;
    public static double EXTEND_POWER = 1.0;
    public static double ANGLE_ADJUSTMENT = 0.02;

    public static double MIN_PITCH = 0.0;
    public static double MAX_PITCH = 1.0;

    public static double MIN_YAW = 0.0;
    public static double MAX_YAW = 1.0;


    public void setPitchPower(double pwr){
        pitchServo.setPower(pwr);
    }

    public void setYawPower(double pwr){
        yawServo.setPower(pwr);
    }

    public void setEjection(double pwr){
        tapeServo.setPower(pwr);
    }

    public void stopYaw(){
        yawServo.setPower(0);
    }
    public void stopPitch(){
        pitchServo.setPower(0);
    }
    public void stopEjection(){
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
            if(Math.abs(gamepad.left_stick_y)>0.2){
                setPitchPower(-gamepad.left_stick_y);
            }
            else{
                setPitchPower(0);
            }


            if(Math.abs(gamepad.right_stick_x)>0.2){
                setYawPower(gamepad.right_stick_x);
            }
            else{
                setYawPower(0);
            }


            if(Math.abs(gamepad.right_trigger)>0.2){
                setEjection(gamepad.right_trigger);
            }
            else if(Math.abs(gamepad.left_trigger)>0.2){
                setEjection(-gamepad.left_trigger);
            }
            else{
                setEjection(0);
            }


        }
    }
}
