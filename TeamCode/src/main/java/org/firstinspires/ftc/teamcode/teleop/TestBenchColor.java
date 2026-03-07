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

//        telemetry.addData("red", normRed1);
//        telemetry.addData("green", normGreen1);
//        telemetry.addData("blue", normBlue1);
//
//        telemetry.addData("red", normRed2);
//        telemetry.addData("green", normGreen2);
//        telemetry.addData("blue", normBlue2);

        double lowboundRedP, lowboundGreenP, lowboundBlueP;
        lowboundRedP = 0.19324914922263414;
        lowboundGreenP = 0.4933766266789853;
        lowboundBlueP = 0.3658716642056435;

        double highboundRedP, highboundGreenP, highboundBlueP;
        highboundRedP = 0.2783390860714835;
        highboundGreenP = 0.5814939615563087;
        highboundBlueP = 0.4429636299120035;

        double lowboundRedG, lowboundGreenG, lowboundBlueG;
        lowboundRedG = 0.2602375054861798;
        lowboundGreenG = 0.34857312230581816;
        lowboundBlueG = 0.41227083642843665;

        double highboundRedG, highboundGreenG, highboundBlueG;
        highboundRedG = 0.33320864835997405;
        highboundGreenG = 0.46428841615572036;
        highboundBlueG = 0.4816060866484865;


        /*
        PURPLE = {'red': 0.19324914922263414, 'green': 0.4933766266789853, 'blue': 0.3658716642056435} to {'red': 0.2783390860714835, 'green': 0.5814939615563087, 'blue': 0.4429636299120035}
        GREEN = {'red': 0.2602375054861798, 'green': 0.34857312230581816, 'blue': 0.41227083642843665} to {'red': 0.33320864835997405, 'green': 0.46428841615572036, 'blue': 0.4816060866484865}
        nothing = 0.4655, 0.7672, 0.5845
         */

        if ((normRed1 < highboundRedP && normRed1 > lowboundRedP && normGreen1 > lowboundGreenP && normGreen1 < highboundGreenP && normBlue1 < highboundBlueP && normBlue1 > lowboundBlueP) || (normRed2 < highboundRedP && normRed2 > lowboundRedP && normGreen2 > lowboundGreenP && normGreen2 < highboundGreenP && normBlue2 < highboundBlueP && normBlue2 > lowboundBlueP)) {
            return detectedColor.PURPLE;
        }
        else if ((normRed1 < highboundRedG && normRed1 > lowboundRedG && normGreen1 > lowboundGreenG && normGreen1 < highboundGreenG && normBlue1 > lowboundBlueG && normBlue1 < highboundBlueG) || (normRed2 < highboundRedG && normRed2 > lowboundRedG && normGreen2 > lowboundGreenG && normGreen2 < highboundGreenG && normBlue2 > lowboundBlueG && normBlue2 < highboundBlueG)) {
            return detectedColor.GREEN;
        }
        else {
            return detectedColor.UNKNOWN;
        }
    }
}