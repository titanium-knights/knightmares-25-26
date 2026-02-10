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

    double inpos1 = 0.25; // 2000/2000
    double inpos2 = 0.49;
    double inpos3 = 0.73; //SIX SEVEN

    private TelemetryManager telemetryM;
    private Telemetry telemetry;

    // Ball tracker reference for color-based navigation
    private BallTracker ballTracker;

    // Current position (1, 2, or 3)
    private int currentPosition = 1;


    public Storer(HardwareMap hmap, Telemetry telemetry) {
        this.storerServo = hmap.servo.get(CONFIG.storer);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        setInit();
    }

    public void setInit() {
        // makes it so the motor is not loose when power is 0
        currentPosition = 1;
    }

    /**
     * Sets the ball tracker reference for color-based navigation.
     */
    public void setBallTracker(BallTracker tracker) {
        this.ballTracker = tracker;
    }

    /**
     * Gets the current position (1, 2, or 3).
     */
    public int getCurrentPosition() {
        return currentPosition;
    }

    public void toOne(){
        storerServo.setPosition(inpos1);
        currentPosition = 1;
        if (ballTracker != null) {
            ballTracker.setCurrentPosition(1);
        }
        telemetryM.addLine("in position 1");
        telemetryM.update();
    }

    public void toTwo(){
        storerServo.setPosition(inpos2);
        currentPosition = 2;
        if (ballTracker != null) {
            ballTracker.setCurrentPosition(2);
        }
        telemetryM.addLine("in position 2");
        telemetryM.update();
    }

    public void toThree(){
        storerServo.setPosition(inpos3);
        currentPosition = 3;
        if (ballTracker != null) {
            ballTracker.setCurrentPosition(3);
        }
        telemetryM.addLine("in position 3");
        telemetryM.update();
    }

    /**
     * Goes to a specific position by number (1, 2, or 3).
     */
    public void toPosition(int position) {
        switch (position) {
            case 1:
                toOne();
                break;
            case 2:
                toTwo();
                break;
            case 3:
                toThree();
                break;
        }
    }

    /**
     * Goes to the first slot containing a GREEN ball.
     * @return true if a green ball was found and moved to, false otherwise
     */
    public boolean goToGreen() {
        if (ballTracker == null) return false;

        int slot = ballTracker.findSlotByColor(BallColor.GREEN);
        if (slot != -1) {
            toPosition(slot + 1); // Convert slot index (0-2) to position (1-3)
            telemetryM.addLine("Going to GREEN at position " + (slot + 1));
            telemetryM.update();
            return true;
        }
        telemetryM.addLine("No GREEN ball found");
        telemetryM.update();
        return false;
    }

    /**
     * Goes to the first slot containing a PURPLE ball.
     * @return true if a purple ball was found and moved to, false otherwise
     */
    public boolean goToPurple() {
        if (ballTracker == null) return false;

        int slot = ballTracker.findSlotByColor(BallColor.PURPLE);
        if (slot != -1) {
            toPosition(slot + 1); // Convert slot index (0-2) to position (1-3)
            telemetryM.addLine("Going to PURPLE at position " + (slot + 1));
            telemetryM.update();
            return true;
        }
        telemetryM.addLine("No PURPLE ball found");
        telemetryM.update();
        return false;
    }

    /**
     * Goes to the first EMPTY slot (for intake).
     * @return true if an empty slot was found and moved to, false otherwise
     */
    public boolean goToEmpty() {
        if (ballTracker == null) return false;

        int slot = ballTracker.findSlotByColor(BallColor.EMPTY);
        if (slot != -1) {
            toPosition(slot + 1); // Convert slot index (0-2) to position (1-3)
            telemetryM.addLine("Going to EMPTY at position " + (slot + 1));
            telemetryM.update();
            return true;
        }
        telemetryM.addLine("No EMPTY slot found");
        telemetryM.update();
        return false;
    }

}
