package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

//NEGATIVE IS UP
//Code credited to Shawn Mendes the handsome ;)
import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.utilities.SlidesState;
import org.firstinspires.ftc.teamcode.utilities.SlidesRotatorState;

public class Slides {

    // to go up
    //Looking from front, Left (acc on the right in this view), must go clockwise
    // Right (Acc on the left in this view), must go counter clockwise
    // Positive power is counter clockwise,

    //position at initial
    int pos; //up and down motor position
    int rot; // left and right rotator position

    //Current state of slide. 0 - idle, 1 - up, 2 - down
    //TODO: consider using an enum
    SlidesState state;
    SlidesRotatorState rotState;

    // limits
    int maxheight = 2700; // 3481
    int minheight = 100;

    // basket heights
    // TODO: tune these values
    int highheight = -2900;

    int lowheight = -1500;

    // rotator limits
    int uprot = 100; // 3481 proviously 25
    int downrot = 100; // 3481 proviously 25

    //assign motors to slide motors and slide rotator motors
    DcMotor slideMotor1;
    DcMotor slideMotor2;
    DcMotor slideRotator;

    public static Telemetry telemetry;

    public Slides(HardwareMap hmap, Telemetry telemetry){
        this.slideMotor1 = hmap.dcMotor.get(CONFIG.slide1);
        this.slideMotor2 = hmap.dcMotor.get(CONFIG.slide2);
        this.slideRotator = hmap.dcMotor.get(CONFIG.slideRot);
        this.pos = 0;
        this.state = SlidesState.LEFT;
        this.telemetry = telemetry;

        slideMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor1.setZeroPowerBehavior(BRAKE);
        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor2.setZeroPowerBehavior(BRAKE);
        slideMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        slideMotor2.setDirection(DcMotorSimple.Direction.FORWARD);


        slideRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideRotator.setZeroPowerBehavior(BRAKE);
    }

    //stop motors
    public void stop(){
        slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);

        pos = getEncoder();

        this.state = SlidesState.STOP;
    }
    public void stopRotator(){
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(0);

        pos = getRotatorEncoder();

        this.rotState = SlidesRotatorState.STOP;
    }

    //is busy
    public boolean isBusy1() {return slideMotor1.isBusy();}
    public boolean isBusy2() {return slideMotor2.isBusy();}
    public boolean rotatorIsBusy() {return slideRotator.isBusy();}
    
    //set target
    public void setTarget1(int target){
        slideMotor1.setTargetPosition(target);
    }
    public void setTarget2(int target){
        slideMotor2.setTargetPosition(target);
    }
    public void setRotTarget(int target){
        slideRotator.setTargetPosition(-target);
    }

    //reset
    public void reset(){
        slideMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        pos = 0;
        this.state = SlidesState.STOP;

        slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    public void resetRotator(){
        slideRotator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        rot = 0;
        this.rotState = SlidesRotatorState.STOP;

        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    //run to position


    public static int slideMaxNum = 2500;
    public static int slideMinNum = 100;

    public void runToPosition(){
        slideMotor1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        slideMotor2.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void runRotToPosition(){
        slideRotator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        setRotPower(0.9);
    }

    public void tozero() {
        setTarget1(slideMinNum);
        setTarget2(slideMinNum);

        runToPosition();
        pos = getEncoder();
    }

    public void slideMaxHeight(){
        setTarget1(slideMaxNum);
        setTarget2(slideMaxNum);

        runToPosition();
        pos = getEncoder();
    }

    public void slideMinHeight(){
        setTarget1(slideMinNum);
        setTarget2(slideMinNum);

        runToPosition();
        pos = getEncoder();
    }

    // BASKET PRESETS

    public void lowBasket(){
        setTarget1(lowheight);
        setTarget2(lowheight);

        runToPosition();
        pos = getEncoder();
    }

    public void highBasket(){
        setTarget1(highheight);
        setTarget2(highheight);
        runToPosition();
        pos = getEncoder();
    }

    // ROTATOR PRESETS

    public void up(){
        setRotTarget(1000);
        setRotPower(0.4);
        while(getRotatorEncoder() < 1000) {
            runRotToPosition();
        }
        stopRotator();
    }

    public void down(){
        setRotTarget(20);
        setRotPower(-0.4);
        while(getRotatorEncoder() > 20) {
            runRotToPosition();
        }
        stopRotator();
    }

    public void smallRetract_auton(){
        setTarget1(350);
        setTarget2(350);
        setPower(-0.5);
        while(getEncoder() > 350) {
            runToPosition();
        }
        stop();
    }

    public int retract_height_auton = 0;
    public void retract_auton(){
        setTarget1(retract_height_auton);
        setTarget2(retract_height_auton);
        setPower(-0.5);
        while(getEncoder() > retract_height_auton) {
            runToPosition();
        }
        stop();
    }

    public int extend_height_auton = 50;
    public void extend_auton(){
        setTarget1(extend_height_auton);
        setTarget2(extend_height_auton);
        setPower(0.3);
        while(getEncoder() < extend_height_auton) {
            runToPosition();
        }
        stop();
    }

    // SLIDES MANUAL

    public void retract(){
        slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        state = SlidesState.LEFT;
        setPower(-0.9);
    }

    public void extend() {

        if (
                (getRotatorEncoder() <= 100 && getEncoder() < 150) || // extend flush DONT USE
                (getRotatorEncoder() >= 900 && getEncoder() < 900))
        {
                slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

                state = SlidesState.RIGHT;
                setPower(0.9);
        }
        else {
            stop();
        }

    }

    // ROTATOR (rotater? rotator.) MANUAL
    public void keepUp() {
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(0.2);

    }
    public void keepDown() {
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setRotPower(-0.2);
    }


    public void rotateLeft(){ //slide rotates down
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotState = SlidesRotatorState.LEFT;
        setRotPower(-0.8);
    }

    //TODO: add rotator limit @ 400
    public void rotateRight() { // slide rotates up
        if (getEncoder() > 500) {
            stopRotator();
        }
        slideRotator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rotState = SlidesRotatorState.RIGHT;
        setRotPower(1);
    }

    public int getEncoder() {
        return slideMotor1.getCurrentPosition();
    }
    public int getRotatorEncoder() {
        return -slideRotator.getCurrentPosition();
    }
    //slide rotator code from now on
    public int getPosition1(){
        return slideRotator.getCurrentPosition();
    }
    //getting target (idk what that is)
    public int getTarget() {
        return slideMotor1.getTargetPosition();
    }
    public int getRotTarget() {
        return slideRotator.getTargetPosition();
    }
    //get runmode?
    public DcMotor.RunMode getMode(){
        return slideMotor1.getMode();
    }

    //set power
    public void setPower(double power){
        slideMotor1.setPower(-power); slideMotor2.setPower(-power); // constant removed
    }

    public void setRotPower(double power){
        slideRotator.setPower(-power); // constant removed
    }



    public void extendForTime(double seconds) {
        slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0.9);
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.seconds() < seconds) {
            // pop out at one in the morning
        }
        stop();
    }

    public void retractForTime(double seconds) {
        slideMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        slideMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(-0.9);
        ElapsedTime timer = new ElapsedTime();
        timer.reset();
        while (timer.seconds() < seconds) {
            // i am 2 weeks ahead of the brainrot curve because oc instagram reels
        }
        stop();
    }
}