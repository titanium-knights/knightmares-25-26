package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Outtake {
    //DANIEL comment: For this, we don't really care about degrees so, we deal with
    //everything in encoder ticks or number of rotations
    DcMotor outtakeMotor1;


    private TelemetryManager telemetryM;
    private Telemetry telemetry;


    public Outtake(HardwareMap hmap, Telemetry telemetry) {
        this.outtakeMotor1 = hmap.dcMotor.get(CONFIG.outtake1);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        setInit();
    }

    public void setInit() {
        // makes it so the motor is not loose when power is 0
        outtakeMotor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        outtakeMotor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        outtakeMotor1.setZeroPowerBehavior(BRAKE);
    }

    public void stopOuttake(){ // sets power to 0 - everything stops
        outtakeMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        setPower(0);
    }
    public void setPower(double power) {
        outtakeMotor1.setPower(power);
    }


    public void shoot() {
        outtakeMotor1.setPower(-0.7);
    }
    public void shoot(double power, double voltage) {
        double nominalVoltage = 12.58; // The voltage you tuned your base power at
        double compensatedPower = power * (nominalVoltage / voltage);
        // Clamp to safe limits
        outtakeMotor1.setPower(-compensatedPower);
    }

}