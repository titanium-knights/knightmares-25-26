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
    }

    public detectedColor getDetectedColor(Telemetry telemetry ) {
        NormalizedRGBA colors = colorSensor.getNormalizedColors();

        float normRed, normGreen, normBlue;
        normRed = colors.red / colors.alpha;
        normBlue = colors.blue / colors.alpha;
        normGreen = colors.green / colors.alpha;

        telemetry.addData("red", normRed);
        telemetry.addData("green", normGreen);
        telemetry.addData("blue", normBlue);

        /*
        RED =
        GREEN =
        BLUE =
         */

        if (normRed < 0.25 && normGreen > 0.45 && normBlue < 0.35) {
            return detectedColor.GREEN;
        }
        else if (normRed > 0.30 && normBlue > 0.30 && normGreen < 0.25
                && Math.abs(normRed - normBlue) < 0.15) {
            return detectedColor.PURPLE;
        }
        else {
            return detectedColor.UNKNOWN;
        }
    }
}
