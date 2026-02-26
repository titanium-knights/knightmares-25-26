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
    double inpos1 = 0.12;
    double inpos2 = inpos1 + SLOT_SPACING;
    double inpos3 = inpos2 + SLOT_SPACING;

    // 0 = empty, 1 = green, 2 = purple
    int[] slots = {0, 0, 0};

    double INC = 0.01;

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
        // drive servo to home position so it starts aligned every time
        storerServo.setPosition(inpos1);
    }

    public void overridePos(double pos) {
        while ((pos - SLOT_SPACING) > 0) pos -= SLOT_SPACING;
        inpos1 = pos;
    }

    /** Reset spindexer back to slot 1 — call between shooting cycles */
    public void home() {
        storerServo.setPosition(inpos1);
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

    public double getPosition() {
        return storerServo.getPosition();
    }

    public void rotateLeft() {
        double currPosition = storerServo.getPosition();
        storerServo.setPosition(currPosition-INC);
    }

    public void rotateRight() {
        double currPosition = storerServo.getPosition();
        storerServo.setPosition(currPosition+INC);
    }

    public void updateSlot(int slotIndex, int colorValue) {
        if (slotIndex >= 0 && slotIndex < slots.length) {
            slots[slotIndex] = colorValue;
        }
    }

    public int getCurrentSlotIndex() {
        double pos = storerServo.getPosition();
        double dist1 = Math.abs(pos - inpos1);
        double dist2 = Math.abs(pos - inpos2);
        double dist3 = Math.abs(pos - inpos3);

        if (dist1 <= dist2 && dist1 <= dist3) return 0;
        if (dist2 <= dist3) return 1;
        return 2;
    }

    public void goToColor(int colorValue) {
        for (int i = 0; i < slots.length; i++) {
            if (slots[i] == colorValue) {
                switch (i) {
                    case 0: toOne(); break;
                    case 1: toTwo(); break;
                    case 2: toThree(); break;
                }
                return;
            }
        }
    }

    public int[] getSlots() {
        return slots;
    }

}
