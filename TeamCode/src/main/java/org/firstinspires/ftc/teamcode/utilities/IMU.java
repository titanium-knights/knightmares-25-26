package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class IMU {
    BNO055IMU imu;

    public IMU(HardwareMap hmap) {
        this.imu = hmap.get(BNO055IMU.class, "imu");
    }
    public String initializeIMU() {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled = true;
        parameters.loggingTag = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu.initialize(parameters);
        return "completed initialization";
    }

    //TODO: test these methods
    public double getZAngle(){
        return (-imu.getAngularOrientation().firstAngle);
    }
    public double getYAngle(){
        return (-imu.getAngularOrientation().secondAngle);
    }
    public double getXAngle(){
        return (-imu.getAngularOrientation().thirdAngle);
    }
}