package org.firstinspires.ftc.teamcode.utilities;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Tracks the color of balls in each of the 3 storer slots.
 * Slot indices: 0 = position 1, 1 = position 2, 2 = position 3
 */
public class BallTracker {

    // Array tracking ball color in each slot (index 0-2 = positions 1-3)
    private BallColor[] slots;

    // Current slot at front (0, 1, or 2)
    private int currentSlotIndex;

    private Telemetry telemetry;

    public BallTracker(Telemetry telemetry) {
        this.telemetry = telemetry;
        this.slots = new BallColor[]{BallColor.EMPTY, BallColor.EMPTY, BallColor.EMPTY};
        this.currentSlotIndex = 0; // Start at position 1
    }

    /**
     * Sets the ball color at the current front slot.
     * Call this when the color sensor detects a ball.
     */
    public void setColorAtCurrentSlot(BallColor color) {
        slots[currentSlotIndex] = color;
    }

    /**
     * Sets the ball color at a specific slot index (0, 1, or 2).
     */
    public void setColorAt(int slotIndex, BallColor color) {
        if (slotIndex >= 0 && slotIndex < 3) {
            slots[slotIndex] = color;
        }
    }

    /**
     * Gets the ball color at a specific slot index.
     */
    public BallColor getColorAt(int slotIndex) {
        if (slotIndex >= 0 && slotIndex < 3) {
            return slots[slotIndex];
        }
        return BallColor.EMPTY;
    }

    /**
     * Gets the ball color at the current front slot.
     */
    public BallColor getCurrentSlotColor() {
        return slots[currentSlotIndex];
    }

    /**
     * Finds the first slot index containing the specified color.
     * Returns -1 if no slot contains that color.
     */
    public int findSlotByColor(BallColor color) {
        for (int i = 0; i < 3; i++) {
            if (slots[i] == color) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Checks if any slot contains the specified color.
     */
    public boolean hasColor(BallColor color) {
        return findSlotByColor(color) != -1;
    }

    /**
     * Counts how many slots contain the specified color.
     */
    public int countColor(BallColor color) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (slots[i] == color) {
                count++;
            }
        }
        return count;
    }

    /**
     * Updates the current slot index when storer moves to a new position.
     * Call this when storer.toOne(), toTwo(), or toThree() is called.
     * @param positionNumber 1, 2, or 3
     */
    public void setCurrentPosition(int positionNumber) {
        if (positionNumber >= 1 && positionNumber <= 3) {
            this.currentSlotIndex = positionNumber - 1;
        }
    }

    /**
     * Gets the current slot index (0, 1, or 2).
     */
    public int getCurrentSlotIndex() {
        return currentSlotIndex;
    }

    /**
     * Gets the current position number (1, 2, or 3).
     */
    public int getCurrentPosition() {
        return currentSlotIndex + 1;
    }

    /**
     * Marks the current slot as empty (ball was shot out).
     */
    public void clearCurrentSlot() {
        slots[currentSlotIndex] = BallColor.EMPTY;
    }

    /**
     * Clears all slots (resets to empty).
     */
    public void clearAll() {
        for (int i = 0; i < 3; i++) {
            slots[i] = BallColor.EMPTY;
        }
    }

    /**
     * Returns telemetry-friendly string of current state.
     */
    public String getStatusString() {
        return String.format("Slots: [%s, %s, %s] | Current: %d",
                slots[0], slots[1], slots[2], currentSlotIndex + 1);
    }

    /**
     * Adds tracking info to telemetry.
     */
    public void addTelemetry() {
        if (telemetry != null) {
            telemetry.addData("Ball Tracker", getStatusString());
            telemetry.addData("Green balls", countColor(BallColor.GREEN));
            telemetry.addData("Purple balls", countColor(BallColor.PURPLE));
            telemetry.addData("Empty slots", countColor(BallColor.EMPTY));
        }
    }
}
