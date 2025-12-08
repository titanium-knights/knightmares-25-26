package org.firstinspires.ftc.teamcode.utilities;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.AnalogInput;

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

    // Rotation servos (Axon Continuous)
    private CRServo leftFrontRotation;
    private CRServo leftBackRotation;
    private CRServo rightFrontRotation;
    private CRServo rightBackRotation;

    // Axon servo analog feedback (built into the servo)
    private AnalogInput leftFrontEncoder;
    private AnalogInput leftBackEncoder;
    private AnalogInput rightFrontEncoder;
    private AnalogInput rightBackEncoder;

    // Pinpoint IMU for odometry
    private GoBildaPinpointDriver pinpoint;

    // PID coefficients for rotation control - START HERE AND TUNE
    private double kP = 0.012;  // Proportional gain
    private double kI = 0.0;    // Integral gain (usually 0 for swerve)
    private double kD = 0.0008; // Derivative gain

    // Encoder calibration offsets (voltage when pointing at 0°)
    private double lfEncoderOffset = 0;
    private double rfEncoderOffset = 0;
    private double lbEncoderOffset = 0;
    private double rbEncoderOffset = 0;

    // PID state for each module
    private double[] lastError = new double[4];
    private double[] integral = new double[4];

    // Flag to check if calibration has been done
    private boolean isCalibrated = false;

    // Wheel base dimensions (adjust to your robot)
    private static final double WHEEL_BASE_WIDTH = 12.0;  // inches
    private static final double WHEEL_BASE_LENGTH = 12.0; // inches

    // Control parameters
    private static final double ANGLE_TOLERANCE = 3.0; // degrees - drive motors only engage when within this
    private static final double MIN_SERVO_POWER = 0.05; // Minimum power to overcome static friction
    private static final double MAX_SERVO_POWER = 0.8; // Maximum servo speed

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

        // Initialize Axon continuous rotation servos
        leftFrontRotation = hardwareMap.get(CRServo.class, "leftFrontRotation");
        leftBackRotation = hardwareMap.get(CRServo.class, "leftBackRotation");
        rightFrontRotation = hardwareMap.get(CRServo.class, "rightFrontRotation");
        rightBackRotation = hardwareMap.get(CRServo.class, "rightBackRotation");

        // Initialize Axon analog feedback (connect servo analog output to analog input port)
        // Wire: Axon servo analog pin -> Control/Expansion Hub analog input
        leftFrontEncoder = hardwareMap.get(AnalogInput.class, "leftFrontEncoder");
        leftBackEncoder = hardwareMap.get(AnalogInput.class, "leftBackEncoder");
        rightFrontEncoder = hardwareMap.get(AnalogInput.class, "rightFrontEncoder");
        rightBackEncoder = hardwareMap.get(AnalogInput.class, "rightBackEncoder");

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
     * Process:
     * 1. Physically rotate each wheel so it points straight forward
     * 2. Call this method to save the current encoder readings
     * 3. These readings become your "zero" reference point
     */
    public void calibrateModules() {
        // Store current encoder readings as the "zero" position
        lfEncoderOffset = readRawEncoderVoltage(leftFrontEncoder);
        rfEncoderOffset = readRawEncoderVoltage(rightFrontEncoder);
        lbEncoderOffset = readRawEncoderVoltage(leftBackEncoder);
        rbEncoderOffset = readRawEncoderVoltage(rightBackEncoder);

        // Reset PID state
        for (int i = 0; i < 4; i++) {
            lastError[i] = 0;
            integral[i] = 0;
        }

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
        return String.format("LF: %.2fV, RF: %.2fV, LB: %.2fV, RB: %.2fV",
                lfEncoderOffset, rfEncoderOffset, lbEncoderOffset, rbEncoderOffset);
    }

    /**
     * Get current angles for telemetry
     */
    public String getCurrentAngles() {
        return String.format("LF: %.1f°, RF: %.1f°, LB: %.1f°, RB: %.1f°",
                getModuleAngle(0), getModuleAngle(1), getModuleAngle(2), getModuleAngle(3));
    }

    /**
     * Get angle errors for tuning
     */
    public String getAngleErrors() {
        return String.format("LF: %.1f°, RF: %.1f°, LB: %.1f°, RB: %.1f°",
                lastError[0], lastError[1], lastError[2], lastError[3]);
    }

    /**
     * Update PID gains for tuning
     */
    public void setPIDGains(double p, double i, double d) {
        this.kP = p;
        this.kI = i;
        this.kD = d;
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

        // Set module states with PID control
        setModuleState(leftFrontDrive, leftFrontRotation, lfSpeed, lfAngle, 0);
        setModuleState(rightFrontDrive, rightFrontRotation, rfSpeed, rfAngle, 1);
        setModuleState(leftBackDrive, leftBackRotation, lbSpeed, lbAngle, 2);
        setModuleState(rightBackDrive, rightBackRotation, rbSpeed, rbAngle, 3);
    }

    /**
     * Set the state of a swerve module using PID control
     */
    private void setModuleState(DcMotor motor, CRServo servo, double speed,
                                double targetAngle, int moduleIndex) {
        // Normalize angle to -180 to 180
        targetAngle = normalizeAngle(targetAngle);

        // Get current angle from Axon encoder
        double currentAngle = getModuleAngle(moduleIndex);

        // Optimize angle (reverse if more than 90 degrees off)
        double angleDiff = normalizeAngle(targetAngle - currentAngle);
        if (Math.abs(angleDiff) > 90) {
            targetAngle = normalizeAngle(targetAngle + 180);
            speed = -speed;
            angleDiff = normalizeAngle(targetAngle - currentAngle);
        }

        // Calculate PID output for servo
        double error = normalizeAngle(targetAngle - currentAngle);

        // PID calculations
        integral[moduleIndex] += error;
        integral[moduleIndex] = Range.clip(integral[moduleIndex], -100, 100); // Prevent windup

        double derivative = error - lastError[moduleIndex];
        lastError[moduleIndex] = error;

        double servoPower = (kP * error) + (kI * integral[moduleIndex]) + (kD * derivative);

        // Add minimum power to overcome static friction
        if (Math.abs(servoPower) > 0.01) {
            servoPower = Math.signum(servoPower) * Math.max(Math.abs(servoPower), MIN_SERVO_POWER);
        }

        // Clip to maximum safe speed
        servoPower = Range.clip(servoPower, -MAX_SERVO_POWER, MAX_SERVO_POWER);

        // Set servo power (continuous servo spins at this speed)
        servo.setPower(servoPower);

        // Only drive if close to target angle
        if (Math.abs(error) < ANGLE_TOLERANCE) {
            motor.setPower(speed);
        } else {
            motor.setPower(0); // Don't drive until aligned
        }
    }

    /**
     * Read raw voltage from Axon servo analog output
     * Axon servos output 0-3.3V representing 0-360° of rotation
     */
    private double readRawEncoderVoltage(AnalogInput encoder) {
        return encoder.getVoltage();
    }

    /**
     * Convert voltage to angle (0-360°)
     */
    private double voltageToAngle(double voltage) {
        double maxVoltage = 3.3; // Axon servos use 3.3V range
        return (voltage / maxVoltage) * 360.0;
    }

    /**
     * Get current module angle with calibration applied
     */
    private double getModuleAngle(int moduleIndex) {
        double rawVoltage = 0;
        double offsetVoltage = 0;

        switch (moduleIndex) {
            case 0: // Left Front
                rawVoltage = readRawEncoderVoltage(leftFrontEncoder);
                offsetVoltage = lfEncoderOffset;
                break;
            case 1: // Right Front
                rawVoltage = readRawEncoderVoltage(rightFrontEncoder);
                offsetVoltage = rfEncoderOffset;
                break;
            case 2: // Left Back
                rawVoltage = readRawEncoderVoltage(leftBackEncoder);
                offsetVoltage = lbEncoderOffset;
                break;
            case 3: // Right Back
                rawVoltage = readRawEncoderVoltage(rightBackEncoder);
                offsetVoltage = rbEncoderOffset;
                break;
        }

        // Convert voltages to angles
        double rawAngle = voltageToAngle(rawVoltage);
        double offsetAngle = voltageToAngle(offsetVoltage);

        // Apply calibration offset and normalize
        double calibratedAngle = rawAngle - offsetAngle;
        return normalizeAngle(calibratedAngle);
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
     * Stop all motors and servos
     */
    public void stop() {
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);

        leftFrontRotation.setPower(0);
        leftBackRotation.setPower(0);
        rightFrontRotation.setPower(0);
        rightBackRotation.setPower(0);
    }
}