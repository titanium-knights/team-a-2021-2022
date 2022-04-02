package org.firstinspires.ftc.teamcode.teleop;

import android.annotation.SuppressLint;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Gamepad;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.util.*;

import java.util.concurrent.atomic.AtomicBoolean;

@TeleOp(name = "MTI Tele-Op", group = "MTI")
@Config
public class MTITeleOp extends PassdionOpMode {
    public static boolean ENABLE_DRIVING = true;
    public static boolean ENABLE_FIELD_CENTRIC = false;
    public static boolean ENABLE_INTAKE = true;
    public static boolean ENABLE_CAROUSEL = true;
    public static boolean ENABLE_CAPSTONE = true;
    public static boolean ENABLE_OUTTAKE = true;
    public static boolean ENABLE_DISTANCE_SENSOR = false;

    @SuppressLint("DefaultLocale")
    @Override
    protected void registerComponents() {
        OdometryRetraction odometryRetraction = new OdometryRetraction(hardwareMap);
        odometryRetraction.retract();
        ToggleButton slowMode = new ToggleButton(() -> (gamepad1.left_stick_button && gamepad1.right_stick_button && gamepad1.cross) || (gamepad2.left_stick_button && gamepad2.right_stick_button) || gamepad1.cross || gamepad2.cross);
        register(slowMode);
        addTelemetryData("Speed", () -> {
            if (slowMode.isActive()) {
                return "SLOW";
            } else {
                return "fast";
            }
        });

        if (ENABLE_DRIVING) {
            MecanumDrive drive = new MecanumDrive(hardwareMap);

            if (ENABLE_FIELD_CENTRIC) {
                MecanumDrive.FieldCentricComponent drivingController = drive.fieldCentricComponent();
                register(drivingController);

                addTelemetryData("IMU X Angle", () -> drivingController.imu.getXAngle());
                addTelemetryData("IMU Y Angle", () -> drivingController.imu.getYAngle());
                addTelemetryData("IMU Z Angle", () -> drivingController.imu.getZAngle());

                onLoop(() -> {
                    if (slowMode.isActive()) {
                        drivingController.multiplier = 0.3;
                    } else {
                        drivingController.multiplier = 1.0;
                    }
                });
            } else {
                MecanumDrive.RobotCentricComponent drivingController = drive.robotCentricComponent();
                register(drivingController);

                onLoop(() -> {
                    if (slowMode.isActive()) {
                        drivingController.multiplier = 0.3;
                    } else {
                        drivingController.multiplier = 1.0;
                    }
                });
            }
        }

        if (ENABLE_INTAKE) {
            TubeIntake intake = new TubeIntake(hardwareMap);
            register(intake.new Controller(gamepad1));
        }

        if (ENABLE_CAROUSEL) {
            Carousel carousel = new Carousel(hardwareMap);
            register(carousel.new Controller(gamepad1));
        }

        if (ENABLE_CAPSTONE) {
            TapeMeasureMechanism capstoneMechanism = new TapeMeasureMechanism(hardwareMap);
            TapeMeasureMechanism.Controller capstoneController = capstoneMechanism.new Controller(gamepad2);
            register(capstoneController);
            onLoop(() -> {
                if (slowMode.isActive()) {
                    capstoneController.multiplier = 0.2;
                } else {
                    capstoneController.multiplier = 0.4;
                }
            });

            addTelemetryData("Pitch Pos", () -> capstoneMechanism.pitchServo.getPosition());
        }

        if (ENABLE_OUTTAKE) {
            Slide2 slide = new Slide2(hardwareMap);
            CarriageDC carriage = new CarriageDC(hardwareMap);
            OuttakeController outtakeController = new OuttakeController(slide, carriage, gamepad1);
            register(outtakeController);

            addTelemetryData("Slide Pos", slide::getCurrentPosition);
            addTelemetryData("Slide Status", () -> {
                if (outtakeController.isSlideUnderManualControl()) {
                    return "Manual";
                } else {
                    return String.format("Moving to %d", outtakeController.getSlideTargetPos());
                }
            });
            addTelemetryData("Carriage Pos", () -> carriage.getPosition());
            if (ENABLE_DISTANCE_SENSOR) {
                AtomicBoolean didRumbleForFreight = new AtomicBoolean(false);
                DistanceSensor ds = hardwareMap.get(DistanceSensor.class, "carriagedist");
                onLoop(() -> {
                    if (didRumbleForFreight.get()) {
                        if (outtakeController.getState() == OuttakeController.State.DUMPED) {
                            didRumbleForFreight.set(false);
                        }
                    } else {
                        if (ds.getDistance(DistanceUnit.MM) < 80) {
                            didRumbleForFreight.set(true);
                            gamepad1.rumble(100);
                        }
                    }
                });
                addTelemetryData("Carriage Distance", () -> ds.getDistance(DistanceUnit.MM));
            }
        }

        register(new TimeTelemetry());
    }
}
