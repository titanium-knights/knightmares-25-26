package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class SwerveDrive {

    private static final int TICKS_PER_REV = 4096;

    // 2:3 gear ratio means servo turns 2 rotations, wheel turns 3 rotations
    // So 1 servo rotation = 1.5 wheel rotations
    private static final double GEAR_RATIO = 1.5; // 3/2

    // Servo position constants
    private static final double SERVO_RANGE = 1.1; // Max servo range [0, 1.1]
    private static final double ANGLE_TOLERANCE = 25.0; // degrees

    // Drive motors
    private DcMotor frDrive, flDrive, blDrive, brDrive;

    // Turning servos
    private Servo frSteer, flSteer, blSteer, brSteer;

    private TelemetryManager telemetryM;
    private Telemetry telemetry;

    // Robot dimensions
    private static final double L = 15.5; // robot length in inches
    private static final double W = 15.5; // robot width in inches
    private static final double R = Math.sqrt(L * L + W * W); // diagonal

    // Speed constants
    private static final double MAX_SPEED = 0.8;
    private static final double TURN_SPEED = 0.8;
    private static final double JOYSTICK_DEADZONE = 0.3;
    private static final double TURN_DEADZONE = 0.1;

    // Initial servo offsets (calibration values)
    private static final double FR_OFFSET = 80;
    private static final double FL_OFFSET = 80;
    private static final double BL_OFFSET = 80;
    private static final double BR_OFFSET = 80;

    // Turn mode angles (optimized for your specific setup)
    private static final double FR_TURN_ANGLE = 130;
    private static final double FL_TURN_ANGLE = 39;
    private static final double BL_TURN_ANGLE = 115;
    private static final double BR_TURN_ANGLE = 39;

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

        telemetryM.debug("Swerve Drive Initialized with 2:3 gear ratio");
        telemetryM.update(telemetry);
    }

    public void move(double x, double y, double turn) {
        double magnitude = Math.hypot(x, y);

        // Translation mode (moving in a direction)
        if (magnitude > JOYSTICK_DEADZONE) {
            double angle = Math.toDegrees(Math.atan2(y, -x)) + 180;
            double speed = magnitude * MAX_SPEED;

            // Check if we're in the reverse deadzone
            boolean isReversing = isInReverseZone(angle);

            // Set all servos to the same angle (simplified swerve)
            setAllServos(angle);

            // Set motor power (negative if reversing)
            double power = isReversing ? -speed : speed;
            setAllDrivePower(power);
        }
        // Rotation mode (turning in place)
        else if (Math.abs(turn) > TURN_DEADZONE) {
            setServoAngle(frSteer, FR_TURN_ANGLE);
            setServoAngle(flSteer, FL_TURN_ANGLE);
            setServoAngle(blSteer, BL_TURN_ANGLE);
            setServoAngle(brSteer, BR_TURN_ANGLE);

            double turnPower = turn * TURN_SPEED;
            frDrive.setPower(turnPower);
            flDrive.setPower(turnPower);
            blDrive.setPower(-turnPower);
            brDrive.setPower(-turnPower);
        }
        // Idle mode
        else {
            setAllServos(180); // Neutral position
            setAllDrivePower(0);
        }
    }

    public void killMotors() {
        setAllDrivePower(0);
        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        blDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        brDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    // Helper method to set all servos to the same angle
    public void setAllServos(double angle) {
        setServoAngle(frSteer, FR_OFFSET + angle);
        setServoAngle(flSteer, FL_OFFSET + angle);
        setServoAngle(blSteer, BL_OFFSET + angle);
        setServoAngle(brSteer, BR_OFFSET + angle);
    }

    // Helper method to set all drive motors to the same power
    private void setAllDrivePower(double power) {
        frDrive.setPower(power);
        flDrive.setPower(power);
        blDrive.setPower(power);
        brDrive.setPower(power);
    }

    // Check if angle is in reverse zone
    private boolean isInReverseZone(double angle) {
        double normalizedAngle = (angle + FR_OFFSET) % 360;
        return (normalizedAngle > 360 - ANGLE_TOLERANCE && normalizedAngle <= 360) ||
                (normalizedAngle >= 0 && normalizedAngle < ANGLE_TOLERANCE);
    }

    /**
     * Sets servo to target angle accounting for 2:3 gear ratio
     * With 2:3 ratio: servo rotates 2 turns → output rotates 3 turns
     * So output angle / 1.5 = servo angle
     */
    private void setServoAngle(Servo steerServo, double targetAngle) {
        // Normalize angle to [0, 360)
        targetAngle = targetAngle % 360;
        if (targetAngle < 0) targetAngle += 360;

        // Apply gear ratio: divide by 1.5 because output rotates 1.5x faster
        double servoAngle = targetAngle / GEAR_RATIO;

        // Convert to servo position [0, SERVO_RANGE]
        double servoPos = (servoAngle / 360.0) * SERVO_RANGE;

        // Apply snap-to positions for common angles (optional optimization)
        servoPos = applyAngleSnapping(targetAngle, servoPos);

        // Clamp to valid range
        servoPos = Math.max(0, Math.min(SERVO_RANGE, servoPos));

        steerServo.setPosition(servoPos);

        // Debug telemetry
        telemetryM.debug("Target Angle", targetAngle);
        telemetryM.debug("Servo Angle (with gear ratio)", servoAngle);
        telemetryM.debug("Servo Position", servoPos);
        telemetryM.update(telemetry);
    }

    /**
     * Snaps to precise positions for cardinal angles
     * This helps with consistency and reduces servo jitter
     */
    private double applyAngleSnapping(double targetAngle, double calculatedPos) {
        if (isNear(targetAngle, 90)) {
            return 0.25 / GEAR_RATIO;
        } else if (isNear(targetAngle, 180)) {
            return 0.50 / GEAR_RATIO;
        } else if (isNear(targetAngle, 270)) {
            return 0.75 / GEAR_RATIO;
        } else if (isNear(targetAngle, 0) || isNear(targetAngle, 360)) {
            return 0.0;
        }
        return calculatedPos;
    }

    // Check if angle is within tolerance of target
    private boolean isNear(double angle, double target) {
        double diff = Math.abs(angle - target);
        return diff < ANGLE_TOLERANCE || diff > (360 - ANGLE_TOLERANCE);
    }
}