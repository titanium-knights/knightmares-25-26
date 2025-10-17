package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

public class SwerveModule {
    private DcMotor driveMotor;
    private Servo turnServo;

    // Calibration offset for this servo (from imperfect zeroing)
    private double angleOffset = 0;

    public SwerveModule(DcMotor driveMotor, Servo turnServo, double offset) {
        this.driveMotor = driveMotor;
        this.turnServo = turnServo;
        this.angleOffset = offset;
    }

    // Set wheel power
    public void setPower(double power) {
        driveMotor.setPower(power);
    }

    // Rotate wheel to target angle (in degrees 0-360)
    public void setAngle(double targetAngle, double totalDegrees) {

        // Apply calibration offset
        double correctedAngle = (targetAngle + angleOffset) % 360;

        // Convert angle into servo position (0.0 to 1.0)
        double servoPos = correctedAngle / totalDegrees;

        turnServo.setPosition(servoPos);
    }
}
