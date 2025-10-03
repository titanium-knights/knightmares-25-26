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
    private SwerveModule frontLeft, frontRight, backLeft, backRight;
    // private BNO055IMU imu;
    private TelemetryManager telemetryM;
    private Telemetry telemetry;

    // TODO: we might have to take into account the distance of the wheels from the edge of the robot
    double L = 15.5; // robot length in inches
    double W = 15.5; // robot width in inches
    double R = Math.sqrt(L*L + W*W); // diagonal size
    double speed = 0.2;

    public SwerveDrive(HardwareMap hmap, Telemetry telemetry) {

        // Init IMU
//        imu = hardwareMap.get(BNO055IMU.class, "imu");
//        BNO055IMU.Parameters params = new BNO055IMU.Parameters();
//        params.angleUnit = BNO055IMU.AngleUnit.DEGREES;
//        imu.initialize(params);

        // Initialize modules (replace names with config names!)
        this.frontLeft  = new SwerveModule(hmap.dcMotor.get(CONFIG.FRONT_LEFT), hmap.servo.get("FL_STEER"), 0);
        this.backLeft  = new SwerveModule(hmap.dcMotor.get(CONFIG.BACK_LEFT), hmap.servo.get("BL_STEER"), 0);
        this.backRight  = new SwerveModule(hmap.dcMotor.get(CONFIG.BACK_RIGHT), hmap.servo.get("BR_STEER"), 0);
        this.frontRight  = new SwerveModule(hmap.dcMotor.get(CONFIG.FRONT_RIGHT), hmap.servo.get("FR_STEER"), 0);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        telemetryM.debug("initted swerve");
        telemetryM.update(telemetry);
    }

    public void move(double x, double y, double turn) {

        // double headingRad = Math.toRadians(getHeading());

        telemetryM.debug("x", x);
        telemetryM.debug("y", y);
        telemetryM.debug("turn", turn);

        double angle = Math.toDegrees(Math.atan2(y, -x)) + 90.0;

        // motors

//        if (Math.abs(turn) <= 0.1) {
//            if (x>=0) {
//                frDrive.setPower(speed);
//                flDrive.setPower(speed);
//                blDrive.setPower(speed);
//                brDrive.setPower(speed);
//            }
//            else {
//                frDrive.setPower(-speed);
//                flDrive.setPower(-speed);
//                blDrive.setPower(-speed);
//                brDrive.setPower(-speed);
//            }
//        } else {
//            if (turn < -0.01) {
//                frDrive.setPower(speed);
//                flDrive.setPower(speed);
//                blDrive.setPower(speed);
//                brDrive.setPower(speed);
//            }
//            else if (turn > 0.01){
//                frDrive.setPower(-speed);
//                flDrive.setPower(-speed);
//                blDrive.setPower(-speed);
//                brDrive.setPower(-speed);
//            }
//        }

        // servos

        double gray = 300;
        double pink = 180;

        if (Math.abs(turn) > 0.1) {
            frontRight.setAngle(45, gray);
            frontLeft.setAngle(45, gray);
            backLeft.setAngle(45, gray);
            backRight.setAngle(45, pink);
        }
        else if (x>=0.01 && y>=0.01) {
            frontRight.setAngle(angle, gray);
            frontLeft.setAngle(angle, gray);
            backLeft.setAngle(angle, gray);
            backRight.setAngle(angle, gray);
        }
    }

    private void setSteerAngle(Servo steerServo, double targetAngle, double rotLimit) {
        // so everything is within 1 radian
        if (targetAngle >= 180) targetAngle -= 180;
        if (targetAngle < 0) targetAngle += 180;

        telemetryM.debug("angle", targetAngle);
        telemetryM.update(telemetry);

        // Convert degrees → [0,1] servo position
        double servoPos = targetAngle / 180 * rotLimit; // because 0 is vertical

        steerServo.setPosition(servoPos);
    }

    public void getHeading() {
//        Orientation angles = imu.getAngularOrientation();
//        return AngleUnit.DEGREES.normalize(angles.firstAngle - headingOffset);
    }

    public void resetHeading() {
//        headingOffset = imu.getAngularOrientation().firstAngle;
    }

    public void getEncoderFL() {
        // TODO: add IMU
        return;
    }

    public void getEncoderBL() {
        // TODO: add IMU
        return;
    }

    public void getEncoderBR() {
        // TODO: add IMU
        return;
    }

    public void getEncoderFR() {
        // TODO: add IMU
        return;
    }
}