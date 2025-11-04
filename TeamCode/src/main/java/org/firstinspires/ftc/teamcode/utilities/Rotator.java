package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//:)

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Configurable
public class Rotator {
    DcMotor rotatorMotor;
    public Rotator(HardwareMap hmap, Telemetry telemetry) {
        this.rotatorMotor = hmap.dcMotor.get(CONFIG.rotator);
        //rotatorServo.setDirection(Servo.Direction.REVERSE);
        //rotatorServo.setPosition(pickPos);
        this.telemetry = telemetry;
    }

    // 0 is 1600 microseconds
    //
    double power = 0.9;

    public static Telemetry telemetry;


    public void rotateUp() {
        rotatorMotor.setPower(-power);
    }

    public void rotateDown() {
        rotatorMotor.setPower(power);
    }

    public void stopRotator() {
        rotatorMotor.setPower(0);
    }

    public void killRotator() {
        rotatorMotor.setPower(0);
        rotatorMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    }

    // public double getPosition() {
//        return (rotatorServo.getPosition());
//    }

}
