package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.*;

@Config
@TeleOp(name = "TeleOp Leagues", group = "TeleOp")
public class TeleOpLeagues extends OpMode {
    DistanceSensor ds;
    enum DumpState {
        IDLE,
        RETURNING_TO_IDLE,
        DUMPING,
        POST_DUMPING
    }

    public static int DELAY_MS = 600;

    MecanumDrive drive;
    TubeIntake intake;
    Carriage carriage;
    Slide2 slide2;
    int targetPos = -1;
    public static int HIGH = Slide2.MAX_POSITION;
    public static int MID = (Slide2.MIN_POSITION + Slide2.MAX_POSITION) / 2;
    public static int LOW = Slide2.MIN_POSITION;
    public static int SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD = 500;
    public static boolean DISABLE_LIMITS = false;
    DumpState dumpState = DumpState.IDLE;
    Carousel carousel;
    CapstoneMechanism2 capstone;
    MotorInterpolation carriageInterpolation;
    OdometryRetraction odometryRetraction;
    ClawIntake claw;
    boolean capstoneMoved = false;

    PushButton slideHighButton;
    PushButton slideMidButton;

    PushButton dumpButton;
    ToggleButton slowModeButton;
    ToggleButton clawButton;
    boolean freightInCarriage = false;
    double dumpCompleteSeconds;
    ElapsedTime elapsedTime;
    boolean endGameStatus = false;
    @Override
    public void init() {
        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        intake = new TubeIntake(hardwareMap);
        drive = new MecanumDrive(hardwareMap);
        slide2 = new Slide2(hardwareMap);
        carousel = new Carousel(hardwareMap);
        ds = hardwareMap.get(DistanceSensor.class, "carriagedist");
        carriage = new Carriage(hardwareMap);
        capstone = new CapstoneMechanism2(hardwareMap);
        claw = new ClawIntake(hardwareMap);
        carriageInterpolation = new MotorInterpolation(Carriage.getIdlePosition(), 0.5);
        slowModeButton = new ToggleButton(() -> gamepad1.left_stick_button && gamepad1.right_stick_button);
        dumpButton = new PushButton(() -> gamepad1.b);
        slideHighButton = new PushButton(() -> gamepad1.y);
        slideMidButton = new PushButton(() -> gamepad1.x);
        clawButton = new ToggleButton(() -> gamepad1.a);
        carriage.setPosition(carriageInterpolation.getCurrent());
        odometryRetraction = new OdometryRetraction(hardwareMap);
        odometryRetraction.retract();
        elapsedTime = new ElapsedTime();
    }
    @Override
    public void start(){
        elapsedTime.reset();
        capstone.setPosition(CapstoneMechanism2.getIdle());
    }
    @Override
    public void loop() {
        drive.teleOpRobotCentric(gamepad1, slowModeButton.isActive() ? 0.3 : 0.75);
        if (gamepad1.right_trigger > 0.3) {
            intake.setPower(-gamepad1.right_trigger * 0.85);
        } else if (gamepad1.left_trigger > 0.3) {
            intake.setPower(gamepad1.left_trigger * 0.85);
        } else {
            intake.stop();
        }

        if (slideHighButton.isPressed()) {
            if (targetPos == HIGH) {
                targetPos = LOW;
            } else {
                targetPos = HIGH;
            }

            dumpState = DumpState.IDLE;
        } else if (slideMidButton.isPressed()) {
            if (targetPos == MID) {
                targetPos = LOW;
            } else {
                targetPos = MID;
            }

            dumpState = DumpState.IDLE;
        }

        if (dumpButton.isPressed() && slide2.getCurrentPosition() >= SLIDE_SAFE_CARRIAGE_MOTION_THRESHOLD) {
            dumpState = DumpState.DUMPING;
            freightInCarriage = false;
        }
        switch (dumpState) {
            case DUMPING:
                carriageInterpolation.setTarget(Carriage.getDumpPosition());
                if (!carriageInterpolation.isBusy()) {
                    dumpState = DumpState.POST_DUMPING;
                    dumpCompleteSeconds = elapsedTime.seconds();
                }
                break;
            case POST_DUMPING:
                if (elapsedTime.seconds() - dumpCompleteSeconds >= DELAY_MS / 1000.0) {
                    dumpState = DumpState.RETURNING_TO_IDLE;
                }
                break;
            case RETURNING_TO_IDLE:
                carriageInterpolation.setTarget(Carriage.getIdlePosition());
                if (!carriageInterpolation.isBusy()) {
                    dumpState = DumpState.IDLE;
                    targetPos = LOW;
                }
                break;
            case IDLE:
                carriageInterpolation.setTarget(Carriage.getIdlePosition());
        }
        carriage.setPosition(carriageInterpolation.getCurrent());

        if (gamepad1.dpad_right) {
            carousel.setPower(0.75);
        } else if (gamepad1.dpad_left) {
            carousel.setPower(-0.75);
        } else {
            carousel.setPower(0);
        }

        if (gamepad1.right_bumper && (slide2.getCurrentPosition() < Slide2.MAX_POSITION || DISABLE_LIMITS)) {
            slide2.setPower(0.75);
            targetPos = -1;
            dumpState = DumpState.IDLE;
        } else if (gamepad1.left_bumper && (slide2.getCurrentPosition() > Slide2.MIN_POSITION || DISABLE_LIMITS)) {
            slide2.setPower(-0.75);
            targetPos = -1;
            dumpState = DumpState.IDLE;
        } else if (targetPos == -1) {
            slide2.setPower(0);
        } else {
            slide2.runToPosition(targetPos);
        }

        String slideStatus;
        if (targetPos == -1) {
            slideStatus = "Manual";
        } else {
            slideStatus = "Moving to " + targetPos;
        }

        if(clawButton.isActive()){
            claw.grab();
        }
        else{
            claw.release();
        }

        int capstonePos = capstone.getPosition();
        if (gamepad1.dpad_up) {
            if (capstonePos <= CapstoneMechanism2.getIdle()) {
                capstone.setManualPower(0.2);
                capstoneMoved = true;
            } else {
                capstone.setManualPower(0);
                capstoneMoved = true;
                gamepad1.rumble(50);
            }
        } else if (gamepad1.dpad_down) {
            if (capstonePos >= CapstoneMechanism2.getPickup()) {
                capstone.setManualPower(-0.2);
                capstoneMoved = true;
            } else {
                capstone.setManualPower(0);
                capstoneMoved = true;
                gamepad1.rumble(50);
            }
        } else if (capstoneMoved) {
            capstone.setManualPower(0);
        }

        if(elapsedTime.time() < 120){
            telemetry.addData("Teleop Time Remaining",(int)(90-elapsedTime.time()));
        }
        else{
            telemetry.addData("Endgame Time Remaining", (int)(120-elapsedTime.time()));
            if(!endGameStatus){
                gamepad1.rumble(1000);
            }
            endGameStatus=true;
        }
        if(ds.getDistance(DistanceUnit.MM)<80 && !freightInCarriage){
            gamepad1.rumble(500);
            freightInCarriage = true;
        }
        telemetry.addData("speed", slowModeButton.isActive() ? "slow" : "fast");
        telemetry.addData("slide pos", slide2.getCurrentPosition());
        telemetry.addData("slide status", slideStatus);
        telemetry.update();

        dumpButton.update();
        slowModeButton.update();
        slideHighButton.update();
        slideMidButton.update();
        clawButton.update();
    }
}
