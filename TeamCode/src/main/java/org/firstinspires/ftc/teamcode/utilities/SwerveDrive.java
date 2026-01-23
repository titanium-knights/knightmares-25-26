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
    private static final double JOYSTICK_DEADZONE = 0.1;
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

    // Timing constants for "Wait then Drive"
    private static final double MS_PER_DEGREE = 3.0;
    private long driveUnlockTime = 0; // Timestamp when driving is allowed
    private double lastTargetAngle = 0; // Tracks the last angle we told the wheels to go to

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

        telemetryM.debug("Swerve Drive Initialized (MOTORS ONLY MODE)");
        telemetryM.update(telemetry);

        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        brDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        blDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        flDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        brDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        blDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // *** SAFETY: FORCE SERVOS OFF ON INIT ***
        killServos();
    }

    public void move(double x, double y, double turn) {
        double magnitude = Math.hypot(x, y);

        // --- TRANSLATION (DRIVING) ---
        if (magnitude > JOYSTICK_DEADZONE) {
            double targetAngle = Math.toDegrees(Math.atan2(y, -x)) + 180;

            // 1. Calculate how much the angle is changing
            double angleDifference = Math.abs(targetAngle - lastTargetAngle);

            if (angleDifference > 180) {
                angleDifference = 360 - angleDifference;
            }

            // 2. If the change is significant, set a delay timer
            // (The delay logic remains so you can test the "Wait" timing via telemetry,
            // even though servos won't move)
            if (angleDifference > 5.0) {
                double servoMovement = angleDifference / GEAR_RATIO;
                long waitTime = (long)(servoMovement * MS_PER_DEGREE);

                driveUnlockTime = System.currentTimeMillis() + waitTime;
                lastTargetAngle = targetAngle;
            }

            // 3. Command the servos to move
            // (Note: The actual hardware write is disabled inside setAllServos -> setServoAngle)
            setAllServos(targetAngle);

            // 4. Drive Logic
            double speed = magnitude * MAX_SPEED;
            boolean isReversing = isInReverseZone(targetAngle);
            double power = isReversing ? -speed : speed;

            if (System.currentTimeMillis() >= driveUnlockTime) {
                setAllDrivePower(power);
            } else {
                setAllDrivePower(0);
            }
        }
        // --- TURNING (ROTATION) ---
        else if (Math.abs(turn) > TURN_DEADZONE) {
            driveUnlockTime = 0;

            // These calls will calculate angles but NOT move hardware
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
        // --- IDLE ---
        else {
            killMotors();
            killServos();
            driveUnlockTime = 0;
        }
    }

    public void killMotors() {
        setAllDrivePower(0);
        flDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        blDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        brDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        frDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    public void killServos() {
        // Disables the PWM signal to the servos (Limp mode)
        flSteer.getController().pwmDisable();
        blSteer.getController().pwmDisable();
        brSteer.getController().pwmDisable();
        frSteer.getController().pwmDisable();
    }

    public void setAllServos(double angle) {
        setServoAngle(frSteer, FR_OFFSET + angle);
        setServoAngle(flSteer, FL_OFFSET + angle);
        setServoAngle(blSteer, BL_OFFSET + angle);
        setServoAngle(brSteer, BR_OFFSET + angle);
    }

    private void setAllDrivePower(double power) {
        frDrive.setPower(power);
        flDrive.setPower(power);
        blDrive.setPower(power);
        brDrive.setPower(power);
    }

    private boolean isInReverseZone(double angle) {
        double normalizedAngle = (angle + FR_OFFSET) % 360;
        return (normalizedAngle > 360 - ANGLE_TOLERANCE && normalizedAngle <= 360) ||
                (normalizedAngle >= 0 && normalizedAngle < ANGLE_TOLERANCE);
    }

    private void setServoAngle(Servo steerServo, double targetAngle) {
        // Normalize angle to [0, 360)
        targetAngle = targetAngle % 360;
        if (targetAngle < 0) targetAngle += 360;

        double servoAngle = targetAngle / GEAR_RATIO;
        double servoPos = (servoAngle / 360.0) * SERVO_RANGE;

        servoPos = applyAngleSnapping(targetAngle, servoPos);
        servoPos = Math.max(0, Math.min(SERVO_RANGE, servoPos));

        // *** CHANGE: COMMENTED OUT TO PREVENT MOVEMENT ***
        // steerServo.setPosition(servoPos);

        // Debug telemetry - You can still see where it "wants" to go
        telemetryM.debug("Target Angle", targetAngle);
        telemetryM.debug("Calculated Pos (Disabled)", servoPos);
        telemetryM.update(telemetry);
    }

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

    private boolean isNear(double angle, double target) {
        double diff = Math.abs(angle - target);
        return diff < ANGLE_TOLERANCE || diff > (360 - ANGLE_TOLERANCE);
    }
}