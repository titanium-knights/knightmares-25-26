package org.firstinspires.ftc.teamcode.utilities;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import com.qualcomm.robotcore.util.Range;

public class BetterBetterSwerveDrive {

    // Drive motors (GoBilda 6000 RPM)
    private DcMotor leftFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightFrontDrive;
    private DcMotor rightBackDrive;

    // Rotation servos (Axon)
    private Servo leftFrontRotation;
    private Servo leftBackRotation;
    private Servo rightFrontRotation;
    private Servo rightBackRotation;

    // Pinpoint IMU for odometry
    private GoBildaPinpointDriver pinpoint;

    // Servo calibration offsets (stores the servo position when aligned to 0°)
    private double lfServoOffset = 0;
    private double rfServoOffset = 0;
    private double lbServoOffset = 0;
    private double rbServoOffset = 0;

    // Flag to check if calibration has been done
    private boolean isCalibrated = false;

    // Wheel base dimensions (adjust to your robot)
    private static final double WHEEL_BASE_WIDTH = 12.0;  // inches
    private static final double WHEEL_BASE_LENGTH = 12.0; // inches

    // Current target angles for each module
    private double lfTargetAngle = 0;
    private double lbTargetAngle = 0;
    private double rfTargetAngle = 0;
    private double rbTargetAngle = 0;

    public BetterBetterSwerveDrive(HardwareMap hardwareMap) {
        // Initialize drive motors
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");

        // Set motor directions (adjust based on your robot)
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);

        // Set zero power behavior
        leftFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        leftBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightFrontDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightBackDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Initialize rotation servos
        leftFrontRotation = hardwareMap.get(Servo.class, "leftFrontRotation");
        leftBackRotation = hardwareMap.get(Servo.class, "leftBackRotation");
        rightFrontRotation = hardwareMap.get(Servo.class, "rightFrontRotation");
        rightBackRotation = hardwareMap.get(Servo.class, "rightBackRotation");

        // Initialize Pinpoint IMU
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setOffsets(50, -87, DistanceUnit.MM);
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        pinpoint.resetPosAndIMU();
    }

    /**
     * Calibrate all swerve modules to their current position as 0°
     * IMPORTANT: Manually align all wheels to point straight forward before calling this!
     *
     * This should be called during initialization after physically aligning the wheels.
     * The calibration offsets will be stored and used for all subsequent movements.
     */
    public void calibrateModules() {
        // Store current servo positions as the "zero" position
        lfServoOffset = leftFrontRotation.getPosition();
        rfServoOffset = rightFrontRotation.getPosition();
        lbServoOffset = leftBackRotation.getPosition();
        rbServoOffset = rightBackRotation.getPosition();

        isCalibrated = true;
    }

    /**
     * Check if the modules have been calibrated
     */
    public boolean isCalibrated() {
        return isCalibrated;
    }

    /**
     * Get calibration offsets for telemetry/debugging
     */
    public String getCalibrationInfo() {
        return String.format("LF: %.3f, RF: %.3f, LB: %.3f, RB: %.3f",
                lfServoOffset, rfServoOffset, lbServoOffset, rbServoOffset);
    }

    /**
     * Drive the robot using field-centric swerve drive
     * @param strafeX Strafe speed (-1 to 1, left negative, right positive)
     * @param strafeY Forward speed (-1 to 1, backward negative, forward positive)
     * @param rotation Rotation speed (-1 to 1, CCW positive)
     * @param fieldCentric Whether to use field-centric drive
     */
    public void move(double strafeX, double strafeY, double rotation, boolean fieldCentric) {
        // Update Pinpoint odometry
        pinpoint.update();

        // Get robot heading from Pinpoint
        double robotHeading = pinpoint.getHeading(AngleUnit.DEGREES);

        // Apply field-centric transformation if enabled
        double x = strafeX;
        double y = strafeY;

        if (fieldCentric) {
            double headingRad = Math.toRadians(robotHeading);
            x = strafeX * Math.cos(-headingRad) - strafeY * Math.sin(-headingRad);
            y = strafeX * Math.sin(-headingRad) + strafeY * Math.cos(-headingRad);
        }

        // Calculate swerve module states
        double r = Math.hypot(WHEEL_BASE_LENGTH, WHEEL_BASE_WIDTH);

        // Front left
        double lfSpeed = Math.hypot(x - rotation * (WHEEL_BASE_LENGTH / r),
                y + rotation * (WHEEL_BASE_WIDTH / r));
        double lfAngle = Math.toDegrees(Math.atan2(y + rotation * (WHEEL_BASE_WIDTH / r),
                x - rotation * (WHEEL_BASE_LENGTH / r)));

        // Front right
        double rfSpeed = Math.hypot(x + rotation * (WHEEL_BASE_LENGTH / r),
                y + rotation * (WHEEL_BASE_WIDTH / r));
        double rfAngle = Math.toDegrees(Math.atan2(y + rotation * (WHEEL_BASE_WIDTH / r),
                x + rotation * (WHEEL_BASE_LENGTH / r)));

        // Back left
        double lbSpeed = Math.hypot(x - rotation * (WHEEL_BASE_LENGTH / r),
                y - rotation * (WHEEL_BASE_WIDTH / r));
        double lbAngle = Math.toDegrees(Math.atan2(y - rotation * (WHEEL_BASE_WIDTH / r),
                x - rotation * (WHEEL_BASE_LENGTH / r)));

        // Back right
        double rbSpeed = Math.hypot(x + rotation * (WHEEL_BASE_LENGTH / r),
                y - rotation * (WHEEL_BASE_WIDTH / r));
        double rbAngle = Math.toDegrees(Math.atan2(y - rotation * (WHEEL_BASE_WIDTH / r),
                x + rotation * (WHEEL_BASE_LENGTH / r)));

        // Normalize speeds
        double maxSpeed = Math.max(Math.max(lfSpeed, rfSpeed), Math.max(lbSpeed, rbSpeed));
        if (maxSpeed > 1.0) {
            lfSpeed /= maxSpeed;
            rfSpeed /= maxSpeed;
            lbSpeed /= maxSpeed;
            rbSpeed /= maxSpeed;
        }

        // Set module states with calibration offsets
        setModuleState(leftFrontDrive, leftFrontRotation, lfSpeed, lfAngle, 0, lfServoOffset);
        setModuleState(rightFrontDrive, rightFrontRotation, rfSpeed, rfAngle, 1, rfServoOffset);
        setModuleState(leftBackDrive, leftBackRotation, lbSpeed, lbAngle, 2, lbServoOffset);
        setModuleState(rightBackDrive, rightBackRotation, rbSpeed, rbAngle, 3, rbServoOffset);
    }

    /**
     * Set the state of a swerve module
     */
    private void setModuleState(DcMotor motor, Servo servo, double speed,
                                double targetAngle, int moduleIndex, double servoOffset) {
        // Normalize angle to -180 to 180
        targetAngle = normalizeAngle(targetAngle);

        // Optimize angle (reverse if more than 90 degrees off)
        double currentAngle = getModuleAngle(moduleIndex);
        double angleDiff = normalizeAngle(targetAngle - currentAngle);

        if (Math.abs(angleDiff) > 90) {
            targetAngle = normalizeAngle(targetAngle + 180);
            speed = -speed;
        }

        // Set rotation servo position with calibration offset
        setServoAngle(servo, targetAngle, servoOffset);

        // Set drive motor power
        motor.setPower(speed);

        // Update target angle for this module
        updateModuleTargetAngle(moduleIndex, targetAngle);
    }

    /**
     * Convert angle to servo position with calibration offset
     */
    private void setServoAngle(Servo servo, double angle, double offset) {
        // Convert -180 to 180 range to 0 to 1 servo range
        double position = (angle + 180) / 360.0;

        // Apply calibration offset
        // The offset represents where the servo was when the wheel was at 0°
        // We need to calculate relative position from that point
        double calibratedPosition = position + offset;

        // Handle wraparound
        if (calibratedPosition > 1.0) {
            calibratedPosition -= 1.0;
        } else if (calibratedPosition < 0.0) {
            calibratedPosition += 1.0;
        }

        calibratedPosition = Range.clip(calibratedPosition, 0, 1);
        servo.setPosition(calibratedPosition);
    }

    /**
     * Get current module angle from stored target
     */
    private double getModuleAngle(int moduleIndex) {
        switch (moduleIndex) {
            case 0: return lfTargetAngle;
            case 1: return rfTargetAngle;
            case 2: return lbTargetAngle;
            case 3: return rbTargetAngle;
            default: return 0;
        }
    }

    /**
     * Update stored target angle
     */
    private void updateModuleTargetAngle(int moduleIndex, double angle) {
        switch (moduleIndex) {
            case 0: lfTargetAngle = angle; break;
            case 1: rfTargetAngle = angle; break;
            case 2: lbTargetAngle = angle; break;
            case 3: rbTargetAngle = angle; break;
        }
    }

    /**
     * Normalize angle to -180 to 180 degrees
     */
    private double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    /**
     * Get current robot pose from Pinpoint
     */
    public Pose2D getPose() {
        return pinpoint.getPosition();
    }

    /**
     * Reset odometry
     */
    public void resetOdometry() {
        pinpoint.resetPosAndIMU();
    }

    /**
     * Stop all motors
     */
    public void stop() {
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);
    }
}