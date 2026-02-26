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
    
    static final double SLOT_SPACING = 0.232; // equal spacing between all slots
    double inpos1 = 0.22;
    double inpos2 = inpos1 + SLOT_SPACING;
    double inpos3 = inpos2 + SLOT_SPACING;

    double INC = 0.0005;

    private TelemetryManager telemetryM;
    private Telemetry telemetry;


    public Storer(HardwareMap hmap, Telemetry telemetry) {
        this.storerServo = hmap.servo.get(CONFIG.storer);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        setInit();
    }

    public void setInit() {
        toOne();
    }

    public void overridePos(double pos) {
        while ((pos - SLOT_SPACING) > 0) pos -= SLOT_SPACING;
        telemetryM.addData("new position: ", pos);
        telemetryM.update();
        inpos1 = pos;
        inpos2 = inpos1 + SLOT_SPACING;
        inpos3 = inpos2 + SLOT_SPACING;

    }

    public void toOne(){
        storerServo.setPosition(inpos1);
    }

    public void toTwo(){
        storerServo.setPosition(inpos2);
    }

    public void toThree(){
        storerServo.setPosition(inpos3);
    }

    public double getPosition() {
        return storerServo.getPosition();
    }

    public void rotateLeft() {
        double currPosition = storerServo.getPosition();
        storerServo.setPosition(currPosition - INC);
    }

    public void rotateRight() {
        double currPosition = storerServo.getPosition();
        storerServo.setPosition(currPosition + INC);
    }

}
