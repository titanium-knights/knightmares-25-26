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

    public detectedColor getDetectedColor(Telemetry telemetry) {
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

        // Purple telemetry: red=[0.284,0.2944] green=[0.3268,0.3754] blue=[0.4273,0.4636]
        double lowboundRedP = 0.25;
        double lowboundGreenP = 0.29;
        double lowboundBlueP = 0.39;

        double highboundRedP = 0.33;
        double highboundGreenP = 0.41;
        double highboundBlueP = 0.50;

        // Green telemetry: red=[0.3736,0.3913] green=[0.6127,0.6272] blue=[0.4064,0.4433]
        double lowboundRedG = 0.34;
        double lowboundGreenG = 0.58;
        double lowboundBlueG = 0.37;

        double highboundRedG = 0.42;
        double highboundGreenG = 0.66;
        double highboundBlueG = 0.48;

        // Empty telemetry: red=[0.3147,0.4539] green=[0.4431,0.5364] blue=[0.2804,0.3301]
        // Empty excluded by: green too low for Green range, blue too low for Purple range

        if ((normRed1 < highboundRedP && normRed1 > lowboundRedP && normGreen1 > lowboundGreenP && normGreen1 < highboundGreenP && normBlue1 < highboundBlueP && normBlue1 > lowboundBlueP) || (normRed2 < highboundRedP && normRed2 > lowboundRedP && normGreen2 > lowboundGreenP && normGreen2 < highboundGreenP && normBlue2 < highboundBlueP && normBlue2 > lowboundBlueP)) {
            telemetry.addData("Detected Color", "Purple");
            return detectedColor.PURPLE;
        }
        else if ((normRed1 < highboundRedG && normRed1 > lowboundRedG && normGreen1 > lowboundGreenG && normGreen1 < highboundGreenG && normBlue1 > lowboundBlueG && normBlue1 < highboundBlueG) || (normRed2 < highboundRedG && normRed2 > lowboundRedG && normGreen2 > lowboundGreenG && normGreen2 < highboundGreenG && normBlue2 > lowboundBlueG && normBlue2 < highboundBlueG)) {
            telemetry.addData("Detected Color", "Green");
            return detectedColor.GREEN;
        }
        else {
            telemetry.addData("Detected Color", "Empty");
            return detectedColor.UNKNOWN;
        }
    }
}