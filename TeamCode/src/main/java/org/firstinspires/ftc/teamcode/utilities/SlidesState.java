package org.firstinspires.ftc.teamcode.utilities;

public enum SlidesState {
    STOP(0),
    LEFT(1),
    RIGHT(2);

    private int slidesState;

    SlidesState(int slidesState) {
        this.slidesState = slidesState;
    }

    public int getSlidesState() {
        return slidesState;
    }
    public void setSlidesState(int slidesState) {
        this.slidesState = slidesState;
    }
}
