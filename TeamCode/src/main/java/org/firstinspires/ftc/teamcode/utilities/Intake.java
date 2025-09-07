package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake {
    //DANIEL comment: For this, we don't really care about degrees so, we deal with
    //everything in encoder ticks or number of rotations
    DcMotor motor;
    Servo servo;


    public Intake(HardwareMap hmap) {
        this.motor = hmap.dcMotor.get(CONFIG.motor);
        this.servo = hmap.servo.get(CONFIG.servo);
        setInit();
    }

    public void setInit() {
        // makes it so the motor is not loose when power is 0
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(BRAKE);
    }

    public void stop(){ // sets power to 0 - everything stops
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        pullUpMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);
    }
    public void setPower(double power) {
        motor.setPower(power);
//        pullUpMotor2.setPower(power);
    }


    // pullUpMotor1 and 2 are reversed. If you want it to go up, power will be negative. If you want it to go down, power will be positive.
    public void runMotor(){ // -1
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(1);
    }

    boolean ran = true;

    public void runServo(){ // -1
        if (ran) {
            servo.setPosition(0.5);
            ran = false;
        }
        else {
            servo.setPosition(0);
            ran = true;
        }
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