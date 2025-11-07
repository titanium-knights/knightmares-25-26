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
    DcMotor intakeMotor;
    DcMotor intakeMotorForShoot;

    Servo ballServo;
    double pullPos = 0.45; // 2000/2000
    double pushPos = 0.83;
    private TelemetryManager telemetryM;
    private Telemetry telemetry;


    public Outtake(HardwareMap hmap, Telemetry telemetry) {
        this.intakeMotor = hmap.dcMotor.get(CONFIG.intake);
        this.intakeMotorForShoot = hmap.dcMotor.get(CONFIG.intake_shoot);
        this.ballServo = hmap.servo.get(CONFIG.ball);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
        setInit();
    }

    public void setInit() {
        // makes it so the motor is not loose when power is 0
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(BRAKE);
    }

    public void stop(){ // sets power to 0 - everything stops
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
//        pullUpMotor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        setPower(0);
    }
    public void setPower(double power) {
        intakeMotor.setPower(power);
//        pullUpMotor2.setPower(power);
    }


    // pullUpMotor1 and 2 are reversed. If you want it to go up, power will be negative. If you want it to go down, power will be positive.
    // update
    public void pushBall() {
        ballServo.setPosition(pushPos);
        telemetryM.addLine("pushed ball");
        telemetryM.update();
    }

    public void motorOutake() {
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setPower(-1);
    }

}
