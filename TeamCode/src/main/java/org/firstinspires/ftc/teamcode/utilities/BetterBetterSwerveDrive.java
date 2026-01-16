package org.firstinspires.ftc.teamcode.utilities;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;  // Changed from CRServo
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

    // Rotation servos (Axon Analog)
    private Servo leftFrontRotation;
    private Servo leftBackRotation;
    private Servo rightFrontRotation;
    private Servo rightBackRotation;

    // Axon servo analog feedback (built into the servo)
    private AnalogInput leftFrontEncoder;
    private AnalogInput leftBackEncoder;
    private AnalogInput rightFrontEncoder;
    private AnalogInput rightBackEncoder;

    // Pinpoint IMU for odometry
    private GoBildaPinpointDriver pinpoint;

    // Gear ratio: 2:3 means servo rotates 2 turns for every 3 wheel turns
    // So servo rotation range is (2/3) * 360° = 240°
    private static final double GEAR_RATIO = 2.0 / 3.0;  // Servo rotations / Wheel rotations
    private static final double SERVO_RANGE_DEGREES = 240.0;  // Axon servos: 240° range

    // Encoder calibration offsets (angle when pointing at 0°)
    private double lfEncoderOffset = 0;
    private double rfEncoderOffset = 0;
    private double lbEncoderOffset = 0;
    private double rbEncoderOffset = 0;

    // Servo position offsets (position when wheel points forward)
    // These will be calibrated - represents servo position (0.0-1.0) when wheel is at 0°
    private double lfServoOffset = 0.5;
    private double rfServoOffset = 0.5;
    private double lbServoOffset = 0.5;
    private double rbServoOffset = 0.5;

    // Flag to check if calibration has been done
    private boolean isCalibrated = false;

    // Wheel base dimensions (adjust to your robot)
    private static final double WHEEL_BASE_WIDTH = 12.0;  // inches
    private static final double WHEEL_BASE_LENGTH = 12.0; // inches

    // Control parameters
    private static final double ANGLE_TOLERANCE = 5.0; // degrees - drive motors only engage when within this

    public BetterBetterSwerveDrive(HardwareMap hardwareMap) {
        // Initialize drive motors
        leftFrontDrive = hardwareMap.dcMotor.get(CONFIG.FRONT_LEFT);
        leftBackDrive = hardwareMap.dcMotor.get(CONFIG.BACK_LEFT);
        rightFrontDrive = hardwareMap.dcMotor.get(CONFIG.FRONT_RIGHT);
        rightBackDrive = hardwareMap.dcMotor.get(CONFIG.BACK_RIGHT);

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

        // Initialize Axon analog servos (changed from CRServo)
        leftFrontRotation = hardwareMap.servo.get(CONFIG.FL_STEER);
        leftBackRotation = hardwareMap.servo.get(CONFIG.BL_STEER);
        rightFrontRotation = hardwareMap.servo.get(CONFIG.FR_STEER);
        rightBackRotation = hardwareMap.servo.get(CONFIG.BR_STEER);

        // Initialize Axon analog feedback
        leftFrontEncoder = hardwareMap.analogInput.get(CONFIG.FL_EN);
        leftBackEncoder = hardwareMap.analogInput.get(CONFIG.BL_EN);
        rightFrontEncoder = hardwareMap.analogInput.get(CONFIG.FR_EN);
        rightBackEncoder = hardwareMap.analogInput.get(CONFIG.BR_EN);

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
     * Modified calibrateModules that also saves to file
     */
    public void calibrateModules() {
        // Store current encoder readings as the "zero" position
        lfEncoderOffset = voltageToWheelAngle(readRawEncoderVoltage(leftFrontEncoder));
        rfEncoderOffset = voltageToWheelAngle(readRawEncoderVoltage(rightFrontEncoder));
        lbEncoderOffset = voltageToWheelAngle(readRawEncoderVoltage(leftBackEncoder));
        rbEncoderOffset = voltageToWheelAngle(readRawEncoderVoltage(rightBackEncoder));

        // Store current servo positions as the "zero" position
        lfServoOffset = leftFrontRotation.getPosition();
        rfServoOffset = rightFrontRotation.getPosition();
        lbServoOffset = leftBackRotation.getPosition();
        rbServoOffset = rightBackRotation.getPosition();

        isCalibrated = true;

    }
    /**
     * Get current angles for telemetry
     */
    public String getCurrentAngles() {
        return String.format("LF: %.1f°, RF: %.1f°, LB: %.1f°, RB: %.1f°",
                getModuleAngle(0), getModuleAngle(1), getModuleAngle(2), getModuleAngle(3));
    }

    /**
     * Get current servo positions for telemetry
     */
    public String getServoPositions() {
        return String.format("LF: %.3f, RF: %.3f, LB: %.3f, RB: %.3f",
                leftFrontRotation.getPosition(), rightFrontRotation.getPosition(),
                leftBackRotation.getPosition(), rightBackRotation.getPosition());
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

        // Set module states (no PID needed - servos handle position control)
        setModuleState(leftFrontDrive, leftFrontRotation, lfSpeed, lfAngle, 0);
        setModuleState(rightFrontDrive, rightFrontRotation, rfSpeed, rfAngle, 1);
        setModuleState(leftBackDrive, leftBackRotation, lbSpeed, lbAngle, 2);
        setModuleState(rightBackDrive, rightBackRotation, rbSpeed, rbAngle, 3);
    }

    /**
     * Set the state of a swerve module
     * Analog servos handle their own position control, so we just set target position
     */
    private void setModuleState(DcMotor motor, Servo servo, double speed,
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
        }

        // Convert target wheel angle to servo position (0.0 to 1.0)
        double servoPosition = wheelAngleToServoPosition(targetAngle, moduleIndex);

        // Set servo position
        servo.setPosition(servoPosition);

        // Get current error for deciding whether to drive
        double error = normalizeAngle(targetAngle - currentAngle);

        // Only drive if close to target angle
        if (Math.abs(error) < ANGLE_TOLERANCE) {
            motor.setPower(speed);
        } else {
            motor.setPower(0); // Don't drive until aligned
        }
    }

    /**
     * Convert wheel angle to servo position accounting for gear ratio and calibration
     * @param wheelAngle Target angle for the wheel (-180 to 180)
     * @param moduleIndex Which module (0-3)
     * @return Servo position (0.0 to 1.0)
     */
    private double wheelAngleToServoPosition(double wheelAngle, int moduleIndex) {
        // Get the calibration offset for this module
        double offset = 0;
        switch (moduleIndex) {
            case 0: offset = lfServoOffset; break;
            case 1: offset = rfServoOffset; break;
            case 2: offset = lbServoOffset; break;
            case 3: offset = rbServoOffset; break;
        }

        // Normalize wheel angle to 0-360 range for easier calculation
        double normalizedWheelAngle = wheelAngle;
        if (normalizedWheelAngle < 0) {
            normalizedWheelAngle += 360;
        }

        // Apply gear ratio: servo angle = wheel angle * gear ratio
        // With 2:3 ratio, servo rotates 240° for full 360° wheel rotation
        double servoAngle = normalizedWheelAngle * GEAR_RATIO;

        // Convert servo angle to position (0.0 to 1.0)
        // Axon servos: 240° range maps to 0.0-1.0
        double servoPosition = servoAngle / SERVO_RANGE_DEGREES;

        // Apply calibration offset
        servoPosition = servoPosition - offset + 0.5;

        // Wrap to 0.0-1.0 range
        while (servoPosition > 1.0) servoPosition -= 1.0;
        while (servoPosition < 0.0) servoPosition += 1.0;

        return servoPosition;
    }

    /**
     * Read raw voltage from Axon servo analog output
     * Axon servos output 0-3.3V representing 0-360° of servo rotation
     */
    private double readRawEncoderVoltage(AnalogInput encoder) {
        return encoder.getVoltage();
    }

    /**
     * Convert voltage to wheel angle accounting for gear ratio
     * The encoder measures servo angle, we need to convert to wheel angle
     */
    private double voltageToWheelAngle(double voltage) {
        double maxVoltage = 3.3; // Axon servos use 3.3V range

        // Voltage represents servo angle (0-240° for Axon servos)
        double servoAngle = (voltage / maxVoltage) * SERVO_RANGE_DEGREES;

        // Convert servo angle to wheel angle using gear ratio
        // wheel angle = servo angle / gear ratio
        double wheelAngle = servoAngle / GEAR_RATIO;

        return wheelAngle;
    }

    /**
     * Get current module angle with calibration applied
     */
    private double getModuleAngle(int moduleIndex) {
        double rawVoltage = 0;
        double offsetAngle = 0;

        switch (moduleIndex) {
            case 0: // Left Front
                rawVoltage = readRawEncoderVoltage(leftFrontEncoder);
                offsetAngle = lfEncoderOffset;
                break;
            case 1: // Right Front
                rawVoltage = readRawEncoderVoltage(rightFrontEncoder);
                offsetAngle = rfEncoderOffset;
                break;
            case 2: // Left Back
                rawVoltage = readRawEncoderVoltage(leftBackEncoder);
                offsetAngle = lbEncoderOffset;
                break;
            case 3: // Right Back
                rawVoltage = readRawEncoderVoltage(rightBackEncoder);
                offsetAngle = rbEncoderOffset;
                break;
        }

        // Convert voltage to wheel angle
        double currentWheelAngle = voltageToWheelAngle(rawVoltage);

        // Apply calibration offset and normalize
        double calibratedAngle = currentWheelAngle - offsetAngle;
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
     * Stop all motors (servos hold position automatically)
     */
    public void stop() {
        leftFrontDrive.setPower(0);
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        rightBackDrive.setPower(0);

        // Analog servos hold their position, no need to set them to 0
    }
}