package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TestBenchColor {
    NormalizedColorSensor colorSensor1;
    NormalizedColorSensor colorSensor2;

    public enum detectedColor {
        GREEN,
        PURPLE,
        UNKNOWN
    }

    public void init(HardwareMap hwMap) {
        colorSensor1 = hwMap.get(NormalizedColorSensor.class, "sensor_color_distance1");
        colorSensor1.setGain(4);
        colorSensor2 = hwMap.get(NormalizedColorSensor.class, "sensor_color_distance2");
        colorSensor2.setGain(4);
    }

    public detectedColor getDetectedColor(Telemetry telemetry ) {
        NormalizedRGBA colors1 = colorSensor1.getNormalizedColors();
        NormalizedRGBA colors2 = colorSensor2.getNormalizedColors();

        float normRed1, normGreen1, normBlue1;
        normRed1 = colors1.red / colors1.alpha * 10;
        normBlue1 = colors1.blue / colors1.alpha * 10;
        normGreen1 = colors1.green / colors1.alpha * 10;

        float normRed2, normGreen2, normBlue2;
        normRed2 = colors2.red / colors2.alpha * 10;
        normBlue2 = colors2.blue / colors2.alpha * 10;
        normGreen2 = colors2.green / colors2.alpha * 10;

        telemetry.addData("red", normRed1);
        telemetry.addData("green", normGreen1);
        telemetry.addData("blue", normBlue1);

        telemetry.addData("red", normRed2);
        telemetry.addData("green", normGreen2);
        telemetry.addData("blue", normBlue2);

        /*
        PURPLE = <0.35, >0.35, <0.5
        GREEN = 0.35, 0.62, 0.4675
        nothing = 0.4655, 0.7672, 0.5845
         */

        if ((normRed1 < 0.35 && normRed1 > 0.25 && normGreen1 > 0.35 && normGreen1 < 0.50 && normBlue1 < 0.50 && normBlue1 > 0.40) || (normRed2 < 0.35 && normRed2 > 0.25 && normGreen2 > 0.35 && normGreen2 < 0.50 && normBlue2 < 0.50 && normBlue2 > 0.40)) {
            return detectedColor.PURPLE;
        }
        else if ((normRed1 < 0.4 && normRed1 > 0.2 && normGreen1 > 0.50 && normGreen1 < 0.65 && normBlue1 > 0.35 && normBlue1 < 0.50) || (normRed2 < 0.4 && normRed2 > 0.2 && normGreen2 > 0.50 && normGreen2 < 0.65 && normBlue2 > 0.35 && normBlue2 < 0.50)) {
            return detectedColor.GREEN;
        }
        else {
            return detectedColor.UNKNOWN;
        }
    }
}