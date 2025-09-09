package org.firstinspires.ftc.teamcode.utilities;

import androidx.annotation.NonNull;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

//:)

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Configurable
public class Rotator {
    Servo rotatorServo;
    public Rotator(HardwareMap hmap, Telemetry telemetry) {
        this.rotatorServo = hmap.servo.get(CONFIG.rotator);
        //rotatorServo.setDirection(Servo.Direction.REVERSE);
        //rotatorServo.setPosition(pickPos);
        this.telemetry = telemetry;
    }
    public static double neutralPos = 1; // TODO: tune value USING DASHBOARD
    public static double dropPos = 0.9;
    public static double pickPos = 0.61;

    public static double floorPos = 0.4;

    public static Telemetry telemetry;


    public void toDrop() {
        //RotatorServo.setDirection(Servo.Direction.REVERSE);
        telemetry.addLine("claw to drop");
        rotatorServo.setPosition(dropPos);
        telemetry.addLine(Double.toString(rotatorServo.getPosition()));
    }
    public void toNeutral() {
        rotatorServo.setPosition(neutralPos);
    }

    public void toPick() {
        //RotatorServo.setDirection(Servo.Direction.REVERSE);
        telemetry.addLine("claw to pick");
        rotatorServo.setPosition(pickPos);
        telemetry.addLine(Double.toString(rotatorServo.getPosition()));
    }

    public void toFloor() {
        //RotatorServo.setDirection(Servo.Direction.REVERSE);
        telemetry.addLine("claw to floor");
        rotatorServo.setPosition(floorPos);
        telemetry.addLine(Double.toString(rotatorServo.getPosition()));
    }
    public double getPosition() {
        return (rotatorServo.getPosition());
    }

    public class toPickAction implements Action {
        @Override
        public boolean run(@NonNull TelemetryPacket packet) {
            toPick();
            return false;
        }
    }

    public Action toPickAction() {  return new Rotator.toPickAction();  }


}
