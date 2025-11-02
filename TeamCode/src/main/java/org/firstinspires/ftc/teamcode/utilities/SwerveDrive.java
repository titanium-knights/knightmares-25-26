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
    double MAX_SPEED = 0.7;
    double TURN_SPEED = 0.6;

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
        double fl_init = 90;
        double bl_init = 90;
        double br_init = 90;
        double fr_init = 90;

        double angle = Math.toDegrees(Math.atan2(y, -x)) + 180;

        double speed = Math.hypot(x,y)*MAX_SPEED;
        double turnspeed = turn*TURN_SPEED;

        telemetryM.debug("speed", speed);

        // servos

        if (Math.hypot(x,y) > 0.2){
            setSteerAngle(frSteer, fr_init + angle);
            setSteerAngle(flSteer, fl_init + angle);
            setSteerAngle(blSteer, bl_init + angle);
            setSteerAngle(brSteer, br_init + angle);

            frDrive.setPower(speed);
            flDrive.setPower(-speed);
            blDrive.setPower(-speed);
            brDrive.setPower(speed);
        }
        else if (Math.abs(turn) > 0.1){
            setSteerAngle(frSteer, 135);
            setSteerAngle(flSteer, 225);
            setSteerAngle(blSteer, 315);
            setSteerAngle(brSteer, 45);

            frDrive.setPower(turnspeed);
            flDrive.setPower(turnspeed);
            blDrive.setPower(-turnspeed);
            brDrive.setPower(-turnspeed);
        } else {
            frDrive.setPower(0);
            flDrive.setPower(0);
            blDrive.setPower(0);
            brDrive.setPower(0);
        }
        
    }

    private void setSteerAngle(Servo steerServo, double targetAngle) {

        telemetryM.debug("angle", targetAngle);

        targetAngle %= 360;

        // Convert degrees → [0,1] servo position
        double servoPos = targetAngle / 360; // might be 355 if its inaccurate
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

        telemetryM.debug("servo pos", servoPos);
        telemetryM.debug("set angle", steerServo.getPosition());
        telemetryM.update(telemetry);
    }
}