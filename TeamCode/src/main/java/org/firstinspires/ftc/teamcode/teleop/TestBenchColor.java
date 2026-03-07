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
        lowboundRedP = 0.2761;
        lowboundGreenP = 0.3759;
        lowboundBlueP = 0.4301;

        double highboundRedP, highboundGreenP, highboundBlueP;
        highboundRedP = 0.3622;
        highboundGreenP = 0.5227;
        highboundBlueP = 0.5170;

        double lowboundRedG, lowboundGreenG, lowboundBlueG;
        lowboundRedG = 0.2031;
        lowboundGreenG = 0.2031;
        lowboundBlueG = 0.3758;

        double highboundRedG, highboundGreenG, highboundBlueG;
        highboundRedG = 0.2809;
        highboundGreenG = 0.5937;
        highboundBlueG = 0.4486;


        /*
        PURPLE = Optimal Range (1 Std): {'red': 0.27610521219442485, 'green': 0.37590743470879934, 'blue': 0.4301736147680556} to {'red': 0.36221330632409354, 'green': 0.522737009735645, 'blue': 0.5170115704171295}
        GREEN = Optimal Range (1 Std): {'red': 0.20317296871737778, 'green': 0.5071065338674015, 'blue': 0.3758602966363657} to {'red': 0.28095295720854824, 'green': 0.5937156883548207, 'blue': 0.4486508144747455}
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