package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.teleop.Teleop;

public class SwerveDrive {

    private static final int TICKS_PER_REV = 4096;

    // Drive motors
    private DcMotor frDrive, flDrive, blDrive, brDrive;


    // turning motors/servos
    private Servo frSteer, flSteer, blSteer, brSteer;
    private TelemetryManager telemetryM;    
    private Telemetry telemetry;

    // TODO: we might have to take into account the distance of the wheels from the edge of the robot

    double L = 15.5; // robot length in inches
    double W = 15.5; // robot width in inches
    double R = Math.sqrt(L * L + W * W); // diagonal size
    double MAX_SPEED = 0.8;
    double TURN_SPEED = 0.8;

    public SwerveDrive(HardwareMap hmap, Telemetry telemetry) {
        this.frDrive = hmap.dcMotor.get(CONFIG.FRONT_RIGHT);
        this.flDrive = hmap.dcMotor.get(CONFIG.FRONT_LEFT);
        this.blDrive = hmap.dcMotor.get(CONFIG.BACK_LEFT);
        this.brDrive = hmap.dcMotor.get(CONFIG.BACK_RIGHT);

        this.frSteer = hmap.servo.get(CONFIG.FR_STEER);
        this.flSteer = hmap.servo.get(CONFIG.FL_STEER);
        this.blSteer = hmap.servo.get(CONFIG.BL_STEER);
        this.brSteer = hmap.servo.get(CONFIG.BR_STEER);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        telemetryM.debug("initted swerve");
        telemetryM.update(telemetry);
    }

    public void move(double x, double y, double turn) {

        // remember to change the letters for this too if you change it for speed
        double fl_init = 80;
        double bl_init = 80;
        double br_init = 80;
        double fr_init = 80;

        double angle = Math.toDegrees(Math.atan2(y, -x)) + 180;

        double speed = Math.hypot(x,y)*MAX_SPEED;
        double turnspeed = turn*TURN_SPEED;

        double tol = 25;

        // servos

        if (Math.hypot(x,y) > 0.2){

            double targetAngle = fr_init + angle; // cuz they should all be the same anyway

            findServoPos(frSteer, fr_init + angle);
            findServoPos(flSteer, fl_init + angle);
            findServoPos(blSteer, bl_init + angle);
            findServoPos(brSteer, br_init + angle);

            // if its going backwards (deadzone) just go straight and run motors backwards
            if ((targetAngle > 360-tol && targetAngle <= 360) || (targetAngle >= 0 && targetAngle < tol)) {
                frDrive.setPower(speed);
                flDrive.setPower(speed);
                blDrive.setPower(-speed);
                brDrive.setPower(-speed);
            }
            else {
                frDrive.setPower(-speed);
                flDrive.setPower(-speed);
                blDrive.setPower(speed);
                brDrive.setPower(speed);
            }
        }
        else if (Math.abs(turn) > 0.2){
            findServoPos(frSteer, 135/1.1);
            findServoPos(flSteer, 225/1.1);
            findServoPos(blSteer, 315/1.1);
            findServoPos(brSteer, 45/1.1);

            frDrive.setPower(-turnspeed);
            flDrive.setPower(turnspeed);
            blDrive.setPower(turnspeed);
            brDrive.setPower(-turnspeed);
        } else {
            findServoPos(frSteer, 180);
            findServoPos(flSteer, 180);
            findServoPos(blSteer, 180);
            findServoPos(brSteer, 180);

            frDrive.setPower(0);
            flDrive.setPower(0);
            blDrive.setPower(0);
            brDrive.setPower(0);
        }
    }

    public void killMotors() {
        flDrive.setPower(0);
        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        blDrive.setPower(0);
        blDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        brDrive.setPower(0);
        brDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frDrive.setPower(0);
        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    private void findServoPos(Servo steerServo, double targetAngle) {

        double tol = 25;
        targetAngle %= 360;

        // Convert degrees → [0,1] servo position
        double servoPos = targetAngle / 360 * 1.1; // might be 355 if its inaccurate

        if (targetAngle > 90-tol && targetAngle < 90+tol) {
            servoPos = 0.25;
        }
        else if (targetAngle > 180-tol && targetAngle < 180+tol) {
            servoPos = 0.50;
        }
        else if (targetAngle > 270-tol && targetAngle < 270+tol) {
            servoPos = 0.80;
        }
        else if ((targetAngle > 360-tol && targetAngle <= 360) || (targetAngle >= 0 && targetAngle < tol)) {
            servoPos = 0.50;
        }
        setServoPos(steerServo, servoPos);

        telemetryM.debug("target angle", targetAngle);
        telemetryM.debug("set pos", servoPos);
        telemetryM.debug("real angle", steerServo.getPosition());
        telemetryM.update(telemetry);
    }

    private void setServoPos(Servo steerServo, double servoPos) {

        double currPos = steerServo.getPosition();

        if ((currPos - servoPos) > 0) {
            if ((currPos - servoPos) < 0.5) {
                steerServo.setPosition(servoPos);
            } else {
                double midValue = currPos - 0.01;
                while (midValue > servoPos) {
                    steerServo.setPosition(midValue);
                    midValue -= 0.01;
                }
                steerServo.setPosition(servoPos);
            }
        } else {
            if ((servoPos - currPos) < 0.5) {
                steerServo.setPosition(servoPos);
            } else {
                double midValue = currPos + 0.01;
                while (midValue > servoPos) {
                    steerServo.setPosition(midValue);
                    midValue += 0.01;
                }
                steerServo.setPosition(servoPos);
            }
        }
    }
}