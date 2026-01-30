package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Storer {

    Servo storerServo;

    double inpos1 = 0.26; // 2000/2000
    double inpos2 = 0.50;
    double inpos3 = 0.74; //SIX SEVEN

    private TelemetryManager telemetryM;
    private Telemetry telemetry;


    public Storer(HardwareMap hmap, Telemetry telemetry) {
        this.storerServo = hmap.servo.get(CONFIG.storer);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        setInit();
    }

    public void setInit() {
        // makes it so the motor is not loose when power is 0
    }

    public void toOne(){
        storerServo.setPosition(inpos1);
        telemetryM.addLine("in position 1");
        telemetryM.update();
    }

    public void toTwo(){
        storerServo.setPosition(inpos2);
        telemetryM.addLine("in position 2");
        telemetryM.update();
    }

    public void toThree(){
        storerServo.setPosition(inpos3);
        telemetryM.addLine("in position 3");
        telemetryM.update();
    }

}
