package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    //DANIEL comment: For this, we don't really care about degrees so, we deal with
    //everything in encoder ticks or number of rotations
    DcMotor intakeMotor;


    public Intake(HardwareMap hmap) {
        this.intakeMotor = hmap.dcMotor.get(CONFIG.intake);
        setInit();
    }

    public void setInit() {
        // makes it so the motor is not loose when power is 0
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(BRAKE);
    }

    public void stop(){ // sets power to 0 - everything stops
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        pullUpMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);
    }
    public void setPower(double power) {
        intakeMotor.setPower(power);
//        pullUpMotor2.setPower(power);
    }


    // pullUpMotor1 and 2 are reversed. If you want it to go up, power will be negative. If you want it to go down, power will be positive.
    public void takeIn(){ // -1
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setPower(1);
    }

//    public void shoot(){ // 1
//        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        intakeMotor.setPower(-1);
//
//    }
//
//    public void stopIntake(){
//
////        pullUpMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
////        pullUpMotor2.setPower(0);
//    }
}