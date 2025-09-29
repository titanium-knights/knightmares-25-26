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
    double L = 18.0; // robot length in inches
    double W = 18.0; // robot width in inches
    double R = Math.sqrt(L*L + W*W); // diagonal size
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
        telemetryM.debug("x", x);
        telemetryM.debug("y", y);
        telemetryM.debug("turn", turn);

        // remember to change the letters for this too if you change it for speed
        double angle = Math.atan2(y, -x) + 1.57; // radians

//        if (x>=0) {
//            frDrive.setPower(speed);
//            flDrive.setPower(speed);
//            blDrive.setPower(speed);
//            brDrive.setPower(speed);
//        }
//        else {
//            frDrive.setPower(-speed);
//            flDrive.setPower(-speed);
//            blDrive.setPower(-speed);
//            brDrive.setPower(-speed);
//        }

        double gray = 5.235;
        double pink = 3.142;

        if (x>=0.01 && y>=0.01) {
            setSteerAngle(frSteer, angle, gray);
            setSteerAngle(flSteer, angle, gray);
            setSteerAngle(blSteer, angle, gray);
            setSteerAngle(brSteer, angle, pink);
        }
    }

    private void setSteerAngle(Servo steerServo, double targetAngle, double rotLimit) {
        // so everything is within 1 radian
        if (targetAngle >= 3.14) targetAngle -= 3.14;
        if (targetAngle < 0) targetAngle += 3.14;

        telemetryM.debug("angle", targetAngle);
        telemetryM.update(telemetry);

        // Convert radians → [0,1] servo position
        double servoPos = targetAngle / rotLimit; // because 0 is vertical

        steerServo.setPosition(servoPos);
    }
}