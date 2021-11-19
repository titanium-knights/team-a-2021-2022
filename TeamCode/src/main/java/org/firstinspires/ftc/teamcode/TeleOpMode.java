package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Tele-Op Mode")
public class TeleOpMode extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {
        MecanumDrive drive = new MecanumDrive(hardwareMap);
        ClawIntake intake = new ClawIntake(hardwareMap);

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

            drive.move(gamepad1.left_stick_x, -gamepad1.left_stick_y,gamepad1.right_stick_x);
            if(gamepad1.y) {
                intake.grab();
            } else if(gamepad1.a) {
                intake.release();
            } else if (gamepad1.left_bumper) {
                intake.incrementClawPosition(-0.05);
            } else if (gamepad1.right_bumper) {
                intake.incrementClawPosition(0.05);
            }
            if(gamepad1.dpad_up) {
                intake.liftArm();
            } else if(gamepad1.dpad_down) {
                intake.lowerArm();
            } else {
                intake.stopArm();
            }



        }
    }
}