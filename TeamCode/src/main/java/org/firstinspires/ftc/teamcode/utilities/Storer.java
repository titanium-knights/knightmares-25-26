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

    double inpos1 = 0; // 2000/2000
    double inpos2 = 0.33;
    double inpos3 = 0.67; //SIX SEVEN

    double outpos1 = 0.5; // 2000/2000
    double outpos2 = 0.83;
    double outpos3 = 0.17; //SIX SEVEN
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

    public void toInOne(){
        storerServo.setPosition(inpos1);
        telemetryM.addLine("in position 1");
        telemetryM.update();
    }
    public void toInTwo(){
        storerServo.setPosition(inpos2);
        telemetryM.addLine("in position 2");
        telemetryM.update();
    }

    public void toInThree(){
        storerServo.setPosition(inpos3);
        telemetryM.addLine("in position 3");
        telemetryM.update();
    }

    public void toOutOne(){
        storerServo.setPosition(outpos1);
        telemetryM.addLine("out position 1");
        telemetryM.update();
    }
    public void toOutTwo(){
        storerServo.setPosition(outpos2);
        telemetryM.addLine("out position 2");
        telemetryM.update();
    }

    public void toOutThree(){
        storerServo.setPosition(outpos3);
        telemetryM.addLine("out position 3");
        telemetryM.update();
    }

}
