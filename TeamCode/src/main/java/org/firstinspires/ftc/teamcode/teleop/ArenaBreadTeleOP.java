package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.*;

import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp(name = "ArenaBreadTeleOP", group = "Arena")
@Config
public class ArenaBreadTeleOP extends PassdionOpMode {
    public static boolean ENABLE_DRIVING = true;
    public static boolean ENABLE_FIELD_CENTRIC = false;
    public static boolean ENABLE_INTAKE = true;
    public static boolean ENABLE_CAROUSEL = true;
    public static boolean ENABLE_CAPSTONE = true;
    public static boolean ENABLE_OUTTAKE = true;
    public static boolean ENABLE_DISTANCE_SENSOR = true;
    public static int RUMBLE_TIME = 500;
    public static double PUBLIC_MOVEMENT_MULTIPLYER = .6;
    public static double PUBLIC_CAPSTONE_MULTIPLYER = .2;

    @SuppressLint("DefaultLocale")
    @Override
    protected void registerComponents() {
        gamepad1.rumble(1000);

//        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
//        odometryRetraction.retract();

//        ToggleButton slowMode = new ToggleButton(() -> (gamepad1.left_stick_button && gamepad1.right_stick_button && gamepad1.cross) || (gamepad2.left_stick_button && gamepad2.right_stick_button) || gamepad1.cross || gamepad2.cross);
//        register(slowMode);

        if (ENABLE_DRIVING) {
            MecanumDrive drive = new MecanumDrive(hardwareMap);

            if (ENABLE_FIELD_CENTRIC) {
                MecanumDrive.FieldCentricComponent drivingController = drive.fieldCentricComponent();
                register(drivingController);

                addTelemetryData("IMU X Angle", () -> drivingController.imu.getXAngle());
                addTelemetryData("IMU Y Angle", () -> drivingController.imu.getYAngle());
                addTelemetryData("IMU Z Angle", () -> drivingController.imu.getZAngle());

                onLoop(() ->drivingController.multiplier = PUBLIC_MOVEMENT_MULTIPLYER);

            } else {
                MecanumDrive.RobotCentricComponent drivingController = drive.robotCentricComponent();
                register(drivingController);

                onLoop(() -> drivingController.multiplier = PUBLIC_MOVEMENT_MULTIPLYER);

            }
        }

//        if (ENABLE_INTAKE) {
//            TubeIntake intake = new TubeIntake(hardwareMap);
//            register(intake.new Controller(gamepad1));
//        }
//
//        if (ENABLE_CAROUSEL) {
//            Carousel carousel = new Carousel(hardwareMap);
//            register(carousel.new Controller(gamepad1));
//        }

        if (ENABLE_CAPSTONE) {
            CapstoneMechanism2 capstoneMechanism = new CapstoneMechanism2(hardwareMap);
            CapstoneMechanism2.Controller capstoneController = capstoneMechanism.new Controller(gamepad1);
            register(capstoneController);
            onLoop(() -> {
                capstoneController.multiplier = PUBLIC_CAPSTONE_MULTIPLYER;

            });
            ClawIntake claw = new ClawIntake(hardwareMap);
            register(claw.new Controller(gamepad1));

            addTelemetryData("Capstone Position:", () -> capstoneMechanism.getPosition());

        }

//        if (ENABLE_OUTTAKE) {
//            Slide2 slide = new Slide2(hardwareMap);
//            CarriageDC carriage = new CarriageDC(hardwareMap);
//            OuttakeController outtakeController = new OuttakeController(slide, carriage, gamepad1);
//            register(outtakeController);
//
//            addTelemetryData("Slide Pos", slide::getCurrentPosition);
//            addTelemetryData("Slide Status", () -> {
//                if (outtakeController.isSlideUnderManualControl()) {
//                    return "Manual";
//                } else {
//                    return String.format("Moving to %d", outtakeController.getSlideTargetPos());
//                }
//            });
//            addTelemetryData("Carriage Pos", () -> carriage.getPosition());
//            if (ENABLE_DISTANCE_SENSOR) {
//                AtomicBoolean didRumbleForFreight = new AtomicBoolean(false);
//                DistanceSensor ds = hardwareMap.get(DistanceSensor.class, "distance_sensor");
//                onLoop(() -> {
//                    if (didRumbleForFreight.get()) {
//                        if (outtakeController.getState() == OuttakeController.State.RETRACTING) {
//                            didRumbleForFreight.set(false);
//                        }
//                    }
//                    else {
//                        if (ds.getDistance(DistanceUnit.MM) < 80) {
////                            addTelemetryData("here",()->"here");
//
//                            didRumbleForFreight.set(true);
//                            gamepad1.rumble(RUMBLE_TIME);
//                        }
//                    }
//                });
//                addTelemetryData("didRumbleForFreight",()->didRumbleForFreight.get());
//                addTelemetryData("Carriage Distance", () -> ds.getDistance(DistanceUnit.MM));
//            }
//        }

        register(new TimeTelemetry());
    }
}
