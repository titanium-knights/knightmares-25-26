package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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
    double speed = 0.2;

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
//        telemetryM.debug("x", x);
//        telemetryM.debug("y", y);
//        telemetryM.debug("turn", turn);

        // remember to change the letters for this too if you change it for speed
        double fl_init = 120.06; // 1667 microseconds
        double bl_init = 0.00;
        double br_init = 56.34; // 1313 microseconds
        double fr_init = 110.7; // 1615 microseconds (-69.3?)

        double angle = Math.toDegrees(Math.atan2(y, x));

        // motors

        if (Math.abs(turn) <= 0.1) {
            if (x>=0) {
                frDrive.setPower(speed);
                flDrive.setPower(speed);
                blDrive.setPower(speed);
                brDrive.setPower(speed);
            }
            else {
                frDrive.setPower(-speed);
                flDrive.setPower(-speed);
                blDrive.setPower(-speed);
                brDrive.setPower(-speed);
            }
        } else {
            if (turn < -0.01) {
                frDrive.setPower(speed);
                flDrive.setPower(speed);
                blDrive.setPower(speed);
                brDrive.setPower(speed);
            }
            else if (turn > 0.01){
                frDrive.setPower(-speed);
                flDrive.setPower(-speed);
                blDrive.setPower(-speed);
                brDrive.setPower(-speed);
            }
        }

        // servos

        if (Math.abs(turn) > 0.1) {
            setSteerAngle(frSteer, fr_init + 45);
            setSteerAngle(flSteer, fl_init - 45);
            setSteerAngle(blSteer, bl_init + 45);
            setSteerAngle(brSteer, br_init - 45);
        } else {
            setSteerAngle(frSteer, fr_init + angle);
            setSteerAngle(flSteer, fl_init + angle);
            setSteerAngle(blSteer, bl_init + angle);
            setSteerAngle(brSteer, br_init + angle);
        }
    }

    private void setSteerAngle(Servo steerServo, double targetAngle) {

        telemetryM.debug("angle", targetAngle);

        // Convert degrees → [0,1] servo position
        double servoPos = targetAngle / 360; // might be 355 if its inaccurate

        steerServo.setPosition(servoPos);

        telemetryM.debug("servo pos", servoPos);
        telemetryM.debug("set angle", steerServo.getPosition());
        telemetryM.update(telemetry);
    }
}