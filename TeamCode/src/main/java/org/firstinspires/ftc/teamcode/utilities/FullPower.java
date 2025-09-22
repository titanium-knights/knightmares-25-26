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
public class FullPower {
    DcMotor fullMotorfl;
    DcMotor fullMotorfr;
    DcMotor fullMotorbl;
    DcMotor fullMotorbr;
    public FullPower(HardwareMap hmap, Telemetry telemetry) {
        this.fullMotorfl = hmap.dcMotor.get(CONFIG.dcMotorfl);
        this.fullMotorfr = hmap.dcMotor.get(CONFIG.dcMotorfr);
        this.fullMotorbr = hmap.dcMotor.get(CONFIG.dcMotorbr);
        this.fullMotorbl = hmap.dcMotor.get(CONFIG.dcMotorbl);
        //rotatorServo.setDirection(Servo.Direction.REVERSE);
        //rotatorServo.setPosition(pickPos);
        this.telemetry = telemetry;
    }


    public static Telemetry telemetry;

    public void setPower(double power) {
        DcMotor.setPower(power);
//        pullUpMotor2.setPower(power);
    }


    public void fullPower(double p){

        dcMotorfl.setPower(1);
    }
        dcMotorfr.setPower(1);
        dcMotorbr.setPower(1);
        dcMotorbl.setPower(1);
}
