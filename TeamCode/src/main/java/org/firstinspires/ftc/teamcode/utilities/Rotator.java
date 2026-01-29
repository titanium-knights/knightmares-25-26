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
    CRServo rotator;
    public Rotator(HardwareMap hmap, Telemetry telemetry) {
        this.rotator = hmap.crservo.get(CONFIG.rot);
        //rotatorServo.setDirection(Servo.Direction.REVERSE);
        //rotatorServo.setPosition(pickPos);
        this.telemetry = telemetry;
    }

    // 0 is 1600 microseconds
    //
    double power = 0.5;

    public static Telemetry telemetry;

    public void setPower(double newPower) {
        rotator.setPower(-newPower);
    }
    public void rotateRight() {
        rotator.setPower(-power);
    }

    public void rotateLeft() {
        rotator.setPower(power);
    }

    public void stop() {
         rotator.setPower(0);
//        rotator.getController().pwmDisable();
    }
}
