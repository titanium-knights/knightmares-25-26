package org.firstinspires.ftc.teamcode.utilities;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class BetterSwerveDrive {

    private static final int TICKS_PER_REV = 4096;

    // Drive motors
    private DcMotor frDrive, flDrive, blDrive, brDrive;

    // turning motors/servos
    private Servo frSteer, flSteer, blSteer, brSteer;
    private TelemetryManager telemetryM;
    private Telemetry telemetry;

    // TODO: we might have to take into account the distance of the wheels from the edge of the robot
    double L = 18.0; // robot length in inches
    double W = 18.0; // robot width in inches
    double R = Math.sqrt(L*L + W*W); // diagonal size

    public BetterSwerveDrive(HardwareMap hmap, Telemetry telemetry) {
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
        telemetryM.debug("x", x);
        telemetryM.debug("y", y);
        telemetryM.debug("turn", turn);
        telemetryM.update(telemetry);

        // TODO: Ls and Ws might be switched
        double A = x - turn * (L / R);
        double B = x + turn * (L / R);
        double C = y - turn * (W / R);
        double D = y + turn * (W / R);

        // TODO: the letters might be wrong lowk
        double frSpeed = Math.hypot(A, D); // B, C
        double flSpeed = Math.hypot(A, C); // B, D
        double blSpeed = Math.hypot(B, C); // A, D
        double brSpeed = Math.hypot(B, D); // A, C

        // remember to change the letters for this too if you change it for speed
        double frAngle = Math.atan2(A, D);
        double flAngle = Math.atan2(A, C);
        double blAngle = Math.atan2(B, C);
        double brAngle = Math.atan2(B, D);

        // Normalize speeds (so none > 1)
        double max = Math.max(Math.max(frSpeed, flSpeed), Math.max(blSpeed, brSpeed));
        if (max > 1.0) {
            if (max > 0.8) {
                frSpeed /= max;
                flSpeed /= max;
                blSpeed /= max;
                brSpeed /= max;
            }
            frDrive.setPower(frSpeed);
            flDrive.setPower(flSpeed);
            blDrive.setPower(blSpeed);
            brDrive.setPower(brSpeed);

            setSteerAngle(frSteer, frAngle);
            setSteerAngle(flSteer, flAngle);
            setSteerAngle(blSteer, blAngle);
            setSteerAngle(brSteer, brAngle);
        }
    }

    private void setSteerAngle (Servo steerServo,double targetAngle){
        // Normalize target angle to [0, 2π)
        targetAngle = targetAngle % (2 * Math.PI);
        if (targetAngle < 0) targetAngle += 2 * Math.PI;

        // Convert radians → [0,1] servo position
        double servoPos = targetAngle / (2 * Math.PI);

        steerServo.setPosition(servoPos);
    }
}