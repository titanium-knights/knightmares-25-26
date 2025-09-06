package org.firstinspires.ftc.teamcode.utilities;

public enum SlidesRotatorState {
    STOP(0),
    LEFT(1),
    RIGHT(2);

    private int rotValue;
    
    SlidesRotatorState(int rotValue) {
        this.rotValue = rotValue;
    }
    
    public int getRotatorValue() {
        return rotValue;
    }
    public void setRotatorValue(int rotValue) {
        this.rotValue = rotValue;
    }
}
