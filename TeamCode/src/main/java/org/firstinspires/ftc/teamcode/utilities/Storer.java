package org.firstinspires.ftc.teamcode.utilities;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Storer {

    Servo storerServo;

    static final double SLOT_SPACING = 0.232;
    double inpos1 = 0.22;
    double inpos2 = inpos1 + SLOT_SPACING;
    double inpos3 = inpos2 + SLOT_SPACING;

    // 0 = empty, 1 = green, 2 = purple
    int[] slots = {0, 0, 0};

    private boolean scanComplete = false;
    private int scanStep = 0;
    private ElapsedTime moveTimer = new ElapsedTime();
    private static final double SETTLE_MS = 600;

    private double lastCommandedPos = -1;
    private boolean isMoving = false;

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

    public boolean runScan(int colorAtIntake) {
        if (scanComplete) return false;

        switch (scanStep) {
            case 0:
                toOne();
                moveTimer.reset();
                scanStep = 1;
                break;
            case 1:
                if (moveTimer.milliseconds() >= SETTLE_MS) scanStep = 2;
                break;
            case 2:
                slots[0] = colorAtIntake;
                toTwo();
                moveTimer.reset();
                scanStep = 3;
                break;
            case 3:
                if (moveTimer.milliseconds() >= SETTLE_MS) scanStep = 4;
                break;
            case 4:
                slots[1] = colorAtIntake;
                toThree();
                moveTimer.reset();
                scanStep = 5;
                break;
            case 5:
                if (moveTimer.milliseconds() >= SETTLE_MS) scanStep = 6;
                break;
            case 6:
                slots[2] = colorAtIntake;
                toOne();
                moveTimer.reset();
                scanStep = 7;
                break;
            case 7:
                if (moveTimer.milliseconds() >= SETTLE_MS) {
                    scanComplete = true;
                }
                break;
        }
        return !scanComplete;
    }

    public void track(int colorAtIntake) {
        double currentPos = storerServo.getPosition();

        if (Math.abs(currentPos - lastCommandedPos) > 0.001) {
            lastCommandedPos = currentPos;
            isMoving = true;
            moveTimer.reset();
        } else if (isMoving && moveTimer.milliseconds() >= SETTLE_MS) {
            isMoving = false;
        }

        if (!isMoving) {
            slots[getCurrentSlotIndex()] = colorAtIntake;
        }
    }

    public boolean isMoving() {
        return isMoving;
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
