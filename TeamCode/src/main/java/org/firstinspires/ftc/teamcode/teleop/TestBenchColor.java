package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TestBenchColor {
    NormalizedColorSensor colorSensor;

    public enum detectedColor {
        GREEN,
        PURPLE,
        UNKNOWN
    }

    public void init(HardwareMap hwMap) {
        colorSensor = hwMap.get(NormalizedColorSensor.class, "sensor_color_distance");
        colorSensor.setGain(4);
    }

    public detectedColor getDetectedColor(Telemetry telemetry ) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha * 10;
        normBlue = colors.blue / colors.alpha * 10;
        normGreen = colors.green / colors.alpha * 10;

        telemetry.addData("red", normRed);
        telemetry.addData("green", normGreen);
        telemetry.addData("blue", normBlue);

        /*
        PURPLE = <0.35, >0.35, <0.5
        GREEN = <0.2, >0.45, <0.4
         */

        if (normRed < 0.25 && normBlue > normGreen && normBlue > 0.30) {
            return detectedColor.PURPLE;
        }
        else if (normGreen > normRed && normGreen > normBlue && normGreen > 0.40) {
            return detectedColor.GREEN;
        }
        else {
            return detectedColor.UNKNOWN;
        }

    }
}