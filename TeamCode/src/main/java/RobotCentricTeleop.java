import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.ClawIntake;
import org.firstinspires.ftc.teamcode.FlywheelIntake;
import org.firstinspires.ftc.teamcode.MecanumDrive;
import org.firstinspires.ftc.teamcode.util.Carousel;

@TeleOp(name="Robot Centric Telop")
public class RobotCentricTeleop extends OpMode {
    MecanumDrive drive;
    ClawIntake intake;
    Carousel carousel;
    @Override
    public void init() {
        drive = new MecanumDrive(hardwareMap);
        intake = new ClawIntake(hardwareMap);
        carousel = new Carousel(hardwareMap);
    }

    @Override
    public void loop() {
        drive.move(gamepad1.left_stick_x, -gamepad1.left_stick_y,gamepad1.right_stick_x);
        if(gamepad1.right_bumper){
            intake.release();
        }
        if(gamepad1.left_bumper){
            intake.grab();
        }
        if(Math.abs(gamepad1.left_trigger)>0.15){
            intake.setArmPower(gamepad1.left_trigger);
        }
        else if(Math.abs(gamepad1.right_trigger)>0.15){
            intake.setArmPower(-gamepad1.right_trigger);
        }
        if(gamepad1.b){
            carousel.spin();
        }
        else if(gamepad1.x){
            carousel.spinReverse();
        }
        else{
            carousel.stop();
        }
    }
}
