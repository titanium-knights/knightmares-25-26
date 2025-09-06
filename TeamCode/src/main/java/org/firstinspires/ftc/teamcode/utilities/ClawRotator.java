package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.Servo.Direction;
import com.qualcomm.robotcore.hardware.DistanceSensor;

//:)

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
    @Config
    public class ClawRotator {
        Servo cRotatorServo;
        public ClawRotator(HardwareMap hmap, Telemetry telemetry) {
            this.cRotatorServo = hmap.servo.get(CONFIG.clawRotator);
            //cRotatorServo.setDirection(Servo.Direction.REVERSE);
            //cRotatorServo.setPosition(pickPos);
            this.telemetry = telemetry;
        }
        public static double neutralPos = 1; // TODO: tune value USING DASHBOARD
        public static double dropPos = 0.9;
        public static double pickPos = 0.61;

        public static double floorPos = 0.4;

        public static Telemetry telemetry;


        public void toDrop() {
            //cRotatorServo.setDirection(Servo.Direction.REVERSE);
            telemetry.addLine("claw to drop");
            cRotatorServo.setPosition(dropPos);
            telemetry.addLine(Double.toString(cRotatorServo.getPosition()));
        }
        public class toDropAction implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                toDrop();
                return false;
            }
        }
        public Action toDropAction() {  return new ClawRotator.toDropAction();  }

        public void toNeutral() {
            cRotatorServo.setPosition(neutralPos);
        }

        public void toPick() {
            //cRotatorServo.setDirection(Servo.Direction.REVERSE);
            telemetry.addLine("claw to pick");
            cRotatorServo.setPosition(pickPos);
            telemetry.addLine(Double.toString(cRotatorServo.getPosition()));
        }

        public void toFloor() {
            //cRotatorServo.setDirection(Servo.Direction.REVERSE);
            telemetry.addLine("claw to floor");
            cRotatorServo.setPosition(floorPos);
            telemetry.addLine(Double.toString(cRotatorServo.getPosition()));
        }
        public double getPosition() {
            return (cRotatorServo.getPosition());
        }

        public class toPickAction implements Action {
            @Override
            public boolean run(@NonNull TelemetryPacket packet) {
                toPick();
                return false;
            }
        }

        public Action toPickAction() {  return new ClawRotator.toPickAction();  }


    }
