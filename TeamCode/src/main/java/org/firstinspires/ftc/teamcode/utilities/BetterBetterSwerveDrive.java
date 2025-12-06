package org.firstinspires.ftc.teamcode.utilities;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;

import com.bylazar.telemetry.PanelsTelemetry;

import org.firstinspires.ftc.robotcore.external.Const;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

// Import for GoBilda Pinpoint Driver
import org.firstinspires.ftc.teamcode.utilities.SwerveDriveConstants;

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

    // Servo positions for angles (will need calibration)
    private static final double SERVO_TICKS_PER_DEGREE = 1.0 / 360.0; // Adjust based on servo specs

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
        pinpoint.setOffsets(50, -87, DistanceUnit.MM);; // Adjust offsets to your robot
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        pinpoint.setEncoderDirections(
                GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD
        );
        pinpoint.resetPosAndIMU();
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

        // Set module states
        setModuleState(leftFrontDrive, leftFrontRotation, lfSpeed, lfAngle, 0);
        setModuleState(rightFrontDrive, rightFrontRotation, rfSpeed, rfAngle, 1);
        setModuleState(leftBackDrive, leftBackRotation, lbSpeed, lbAngle, 2);
        setModuleState(rightBackDrive, rightBackRotation, rbSpeed, rbAngle, 3);
    }

    /**
     * Set the state of a swerve module
     */
    private void setModuleState(DcMotor motor, Servo servo, double speed,
                                double targetAngle, int moduleIndex) {
        // Normalize angle to -180 to 180
        targetAngle = normalizeAngle(targetAngle);

        // Optimize angle (reverse if more than 90 degrees off)
        double currentAngle = getModuleAngle(moduleIndex);
        double angleDiff = normalizeAngle(targetAngle - currentAngle);

        if (Math.abs(angleDiff) > 90) {
            targetAngle = normalizeAngle(targetAngle + 180);
            speed = -speed;
        }

        // Set rotation servo position
        setServoAngle(servo, targetAngle);

        // Set drive motor power
        motor.setPower(speed);

        // Update target angle for this module
        updateModuleTargetAngle(moduleIndex, targetAngle);
    }

    /**
     * Convert angle to servo position (needs calibration)
     */
    private void setServoAngle(Servo servo, double angle) {
        // Convert -180 to 180 range to 0 to 1 servo range
        double position = (angle + 180) / 360.0;
        position = Range.clip(position, 0, 1);
        servo.setPosition(position);
    }

    /**
     * Get current module angle from Pinpoint or servo feedback
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