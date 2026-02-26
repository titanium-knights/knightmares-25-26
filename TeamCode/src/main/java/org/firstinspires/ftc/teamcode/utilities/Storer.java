package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Storer {

    Servo storerServo;

    static final double SLOT_SPACING = 0.232; // equal spacing between all slots
    double inpos1 = 0.22;
    double inpos2 = inpos1 + SLOT_SPACING;
    double inpos3 = inpos2 + SLOT_SPACING;

    // 0 = empty, 1 = green, 2 = purple
    int[] slots = {0, 0, 0};

    private boolean scanComplete = false;
    private int scanStep = 0;
    private ElapsedTime scanTimer = new ElapsedTime();
    private static final double SCAN_SETTLE_MS = 400;

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

    /** Reset spindexer back to slot 1 — call between shooting cycles */
    public void home() {
        storerServo.setPosition(inpos1);
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
        storerServo.setPosition(currPosition-INC);
    }

    public void rotateRight() {
        double currPosition = storerServo.getPosition();
        storerServo.setPosition(currPosition+INC);
    }

    // Scan states:
    // 0 = start, move to slot 1
    // 1 = waiting at slot 1
    // 2 = settled at slot 1, read color, move to slot 2
    // 3 = waiting at slot 2
    // 4 = settled at slot 2, read color, move to slot 3
    // 5 = waiting at slot 3
    // 6 = settled at slot 3, read color, move back to slot 1
    // 7 = waiting to return to slot 1
    // 8 = done
    public boolean runScan(int colorAtIntake) {
        if (scanComplete) return false;

        switch (scanStep) {
            case 0:
                toOne();
                scanTimer.reset();
                scanStep = 1;
                break;
            case 1:
                if (scanTimer.milliseconds() >= SCAN_SETTLE_MS) scanStep = 2;
                break;
            case 2:
                slots[0] = colorAtIntake;
                toTwo();
                scanTimer.reset();
                scanStep = 3;
                break;
            case 3:
                if (scanTimer.milliseconds() >= SCAN_SETTLE_MS) scanStep = 4;
                break;
            case 4:
                slots[1] = colorAtIntake;
                toThree();
                scanTimer.reset();
                scanStep = 5;
                break;
            case 5:
                if (scanTimer.milliseconds() >= SCAN_SETTLE_MS) scanStep = 6;
                break;
            case 6:
                slots[2] = colorAtIntake;
                toOne();
                scanTimer.reset();
                scanStep = 7;
                break;
            case 7:
                if (scanTimer.milliseconds() >= SCAN_SETTLE_MS) {
                    scanComplete = true;
                }
                break;
        }
        return !scanComplete;
    }

    public void updateSlots(int colorAtIntake) {
        int idx = getCurrentSlotIndex();
        slots[idx] = colorAtIntake;
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
