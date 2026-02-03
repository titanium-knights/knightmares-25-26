package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

public class ColorSensorTest extends OpMode {

    TestBenchColor bench = new TestBenchColor();

    TestBenchColor.detectedColor detectedColor;

    @Override
    public void init() {
        bench.init(hardwareMap);
    }

    @Override
    public void loop() {
        detectedColor = bench.getDetectedColor(telemetry);
        telemetry.addData("Color Detected", detectedColor);
    }
}
