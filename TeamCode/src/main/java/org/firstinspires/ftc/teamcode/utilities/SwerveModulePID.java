package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class SwerveModulePID {
    private DcMotor driveMotor;
    private Servo rotationServo;
    private AnalogInput angleEncoder;

    // PID coefficients for rotation control
    private double kP = 0.01;  // Proportional gain - tune this
    private double kI = 0.0;   // Integral gain - tune this
    private double kD = 0.001; // Derivative gain - tune this

    private double lastError = 0;
    private double integral = 0;
    private ElapsedTime timer = new ElapsedTime();

    private double targetAngle = 0;

    public SwerveModulePID(DcMotor driveMotor, Servo rotationServo, AnalogInput angleEncoder) {
        this.driveMotor = driveMotor;
        this.rotationServo = rotationServo;
        this.angleEncoder = angleEncoder;
    }

    /**
     * Set the target state for this module
     * @param speed Drive motor speed (-1 to 1)
     * @param angle Target angle in degrees (-180 to 180)
     */
    public void setState(double speed, double angle) {
        // Normalize target angle
        angle = normalizeAngle(angle);

        // Get current angle from encoder
        double currentAngle = getCurrentAngle();

        // Optimize angle (reverse if >90° off)
        double error = normalizeAngle(angle - currentAngle);
        if (Math.abs(error) > 90) {
            angle = normalizeAngle(angle + 180);
            speed = -speed;
            error = normalizeAngle(angle - currentAngle);
        }

        targetAngle = angle;

        // Calculate PID for servo position
        double deltaTime = timer.seconds();
        timer.reset();

        // PID calculations
        integral += error * deltaTime;
        integral = Range.clip(integral, -0.5, 0.5); // Prevent integral windup

        double derivative = (error - lastError) / deltaTime;
        lastError = error;

        double correction = (kP * error) + (kI * integral) + (kD * derivative);

        // Apply correction to servo (assuming current position is close to target)
        double currentServoPos = rotationServo.getPosition();
        double newServoPos = currentServoPos + correction;
        newServoPos = Range.clip(newServoPos, 0, 1);

        rotationServo.setPosition(newServoPos);
        driveMotor.setPower(speed);
    }

    /**
     * Read current angle from absolute encoder
     * @return Angle in degrees (-180 to 180)
     */
    public double getCurrentAngle() {
        // Read voltage from analog encoder (typically 0-3.3V for 0-360°)
        double voltage = angleEncoder.getVoltage();
        double maxVoltage = 3.3; // Adjust if your encoder uses different voltage

        // Convert to degrees
        double angle = (voltage / maxVoltage) * 360.0;

        // Normalize to -180 to 180
        return normalizeAngle(angle);
    }

    /**
     * Get the current error from target
     * @return Error in degrees
     */
    public double getError() {
        return normalizeAngle(targetAngle - getCurrentAngle());
    }

    /**
     * Check if module is at target angle
     * @param tolerance Acceptable error in degrees
     * @return True if within tolerance
     */
    public boolean atTarget(double tolerance) {
        return Math.abs(getError()) < tolerance;
    }

    /**
     * Reset PID controller (call when changing targets significantly)
     */
    public void resetPID() {
        integral = 0;
        lastError = 0;
        timer.reset();
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
     * Normalize angle to -180 to 180 degrees
     */
    private double normalizeAngle(double angle) {
        while (angle > 180) angle -= 360;
        while (angle < -180) angle += 360;
        return angle;
    }

    public void stop() {
        driveMotor.setPower(0);
    }
}