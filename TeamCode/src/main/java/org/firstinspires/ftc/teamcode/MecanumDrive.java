package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MecanumDrive {
    DcMotor fl, fr, bl, br;

    public MecanumDrive(HardwareMap hmap){
        fl = hmap.dcMotor.get("fl");
        fr = hmap.dcMotor.get("fr");
        bl = hmap.dcMotor.get("bl");
        br = hmap.dcMotor.get("br");

        fl.setDirection(DcMotorSimple.Direction.FORWARD);
        fr.setDirection(DcMotorSimple.Direction.FORWARD);
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        br.setDirection(DcMotorSimple.Direction.FORWARD);
    }
    double[] powerArr = new double[4];

    public void move(double x, double y, double turn) {
        powerArr[0] = x+y+turn; //fl power
        powerArr[1] = y-x-turn; //Fr Power
        powerArr[2] = y-x+turn;//bl Power
        powerArr[3] = y+x-turn; //br Power

        double max = Math.max(Math.abs(powerArr[0]),Math.abs(powerArr[1]));
        double tempMax = Math.max(Math.abs(powerArr[2]),Math.abs(powerArr[3]));
        max = Math.max(max, tempMax);

        if(max>1){
            //We have to normalize
            for(int i=0;i<4;i++){
                powerArr[i]=powerArr[i]/max;
            }
        }

        fl.setPower(powerArr[0]);
        fr.setPower(powerArr[1]);
        bl.setPower(powerArr[2]);
        br.setPower(powerArr[3]);
    }
    public void driveForwardsWithPower(double power){
        move(0,power,0);
    }
    public void driveBackwardsWithPower(double power){
        move(0,power,0);
    }

    public void strafeLeftWithPower(double power){
        move(-power,0,0);
    }
    public void strafeRightWithPower(double power){
        move(power,0,0);
    }
    public void turnLeftWithPower(double power){
        move(0,0,-power);
    }
    public void turnRightWithPower(double power){
        move(0,0, power);
    }
    public void stop(){
        move(0,0,0);
    }

}
