package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.util.Carousel;

@TeleOp(name = "Tele-Op Mode")
public class TeleOpMode extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        ClawIntake intake = new ClawIntake(hardwareMap);
        Carousel carousel = new Carousel(hardwareMap);
        RevBlinkinLedDriver leds = hardwareMap.get(RevBlinkinLedDriver.class, "oohshiny");
        leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.BEATS_PER_MINUTE_LAVA_PALETTE);

        FtcDashboard dashboard = FtcDashboard.getInstance();
        Telemetry telemetry = dashboard.getTelemetry();

        boolean dpad_left_down = false;
        boolean dpad_right_down = false;
        boolean a_down = false;
        boolean slowMode = false;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

           // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
//            double drive = -gamepad1.left_stick_y;
//            double turn  =  gamepad1.right_stick_x;
            //leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            //rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            double multiplier = slowMode ? 0.3 : 0.75;
            drive.move(gamepad1.left_stick_x * multiplier, -gamepad1.left_stick_y * multiplier,gamepad1.right_stick_x * multiplier);
            if(gamepad1.right_bumper) {
                intake.grab();
            } else if (gamepad1.y) {
                intake.grabBall();
            } else if(gamepad1.left_bumper) {
                intake.release();
            } else if (gamepad1.dpad_left && !dpad_left_down) {
                intake.incrementClawPosition(-0.07);
            } else if (gamepad1.dpad_right && !dpad_right_down) {
                intake.incrementClawPosition(0.07);
            }
            dpad_left_down = gamepad1.dpad_left;
            dpad_right_down = gamepad1.dpad_right;
            if(Math.abs(gamepad1.right_trigger)>0.15){
                intake.setArmPower(gamepad1.right_trigger);
            }
            else if(Math.abs(gamepad1.left_trigger)>0.15){
                intake.setArmPower(-gamepad1.left_trigger);
            }
            else {
                intake.setArmPower(0);
            }
            if(gamepad1.b){
                carousel.spin(true);
            }
            else if(gamepad1.x){
                carousel.spinReverse(true);
            }
            else{
                carousel.stop();
            }
            if (gamepad1.a && !a_down) {
                slowMode = !slowMode;
            }
            a_down = gamepad1.a;

            if (Math.abs(intake.claw.getPosition() - ClawIntake.grabPos) < 0.03) {
                leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.YELLOW);
            } else if (Math.abs(intake.claw.getPosition() - ClawIntake.ballPos) < 0.03) {
                leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.WHITE);
            } else if (Math.abs(intake.claw.getPosition() - ClawIntake.releasePos) < 0.03) {
                leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.RAINBOW_RAINBOW_PALETTE);
            } else {
                leds.setPattern(RevBlinkinLedDriver.BlinkinPattern.BLUE);
            }

            telemetry.addData("Left Trigger", gamepad1.left_trigger);
            telemetry.addData("Right Trigger", gamepad1.right_trigger);
            telemetry.addData("Claw Position", intake.claw.getPosition());
            this.telemetry.addData("TorqueNADO Mode", slowMode ? "Yes" : "No");
            this.telemetry.update();
            telemetry.update();

        }
    }
}