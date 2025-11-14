package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Storer {
    //DANIEL comment: For this, we don't really care about degrees so, we deal with
    //everything in encoder ticks or number of rotations
    Servo storerServo;

    double pos1 = 0; // 2000/2000
    double pos2 = 0.33;
    double pos3 = 0.67; //SIX SEVEN
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
        storerServo.setPosition(pos1);
        telemetryM.addLine("position 1");
        telemetryM.update();


    }
    public void toTwo(){
        storerServo.setPosition(pos2);
        telemetryM.addLine("position 2");
        telemetryM.update();

    }

    public void toThree(){
        storerServo.setPosition(pos3);
        telemetryM.addLine("position 3");
        telemetryM.update();

    }

}
