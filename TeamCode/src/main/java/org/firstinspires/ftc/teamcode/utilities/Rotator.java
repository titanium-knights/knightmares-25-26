package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Configurable
public class Rotator {
    Servo rotator;
    public static final double MAX_POS = 1.0;
    public static final double MIN_POS = 0.0;
    public static final double MAX_INCREMENT = 0.005;
    public static final double MIN_INCREMENT = 0.001;
    public static final double MANUAL_INCREMENT = 0.002;
    public static Telemetry telemetry;

    public Rotator(HardwareMap hmap, Telemetry telemetry) {
        this.rotator = hmap.servo.get(CONFIG.rot);
        //rotatorServo.setDirection(Servo.Direction.REVERSE);
        //rotatorServo.setPosition(pickPos);
        this.telemetry = telemetry;
    }

    public double getPosition() {
        return rotator.getPosition();
    }

    public void rotateRight() {
        double current = rotator.getPosition();
        rotator.setPosition(current - MANUAL_INCREMENT); // Assuming Right is decreasing, swap sign if needed
    }

    public void rotateLeft() {
        double current = rotator.getPosition();
        rotator.setPosition(current + MANUAL_INCREMENT); // Assuming Left is increasing
    }

    public void rotateRight(double tx) {
        double increment = (MAX_INCREMENT-MIN_INCREMENT)/27*tx + MIN_INCREMENT;
        double current = rotator.getPosition();
        telemetry.addLine("rotating right increment: " + increment);
        telemetry.update();
        rotator.setPosition(current - increment);
    }

    public void rotateLeft(double tx) {
        double increment = (MAX_INCREMENT-MIN_INCREMENT)/27*tx - MIN_INCREMENT;
        double current = rotator.getPosition();
        telemetry.addLine("rotating left increment: " + increment);
        telemetry.update();
        rotator.setPosition(current - increment); // Assuming Left is increasing
    }
}
