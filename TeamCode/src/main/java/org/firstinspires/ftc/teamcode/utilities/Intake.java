package org.firstinspires.ftc.teamcode.utilities;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;


import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {
    //DANIEL comment: For this, we don't really care about degrees so, we deal with
    //everything in encoder ticks or number of rotations
    CRServo intakeServo;
    Servo ballServo;

    double pullPos = 0.55 ; // 2000/2000
    double pushPos = 0.95;
    private TelemetryManager telemetryM;
    private Telemetry telemetry;
    double power = 0.9;


    public Intake(HardwareMap hmap, Telemetry telemetry) {
        this.intakeServo = hmap.crservo.get(CONFIG.intake);
        this.ballServo = hmap.servo.get(CONFIG.ball);

        this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }



    public void stopIntake(){ // sets power to 0 - everything stops
         intakeServo.setPower(0);
//        intakeServo.getController().pwmDisable();
    }
    public void run() {
        intakeServo.setPower(-power);
    }

    public void pushBall(){
        ballServo.setPosition(pushPos);
        telemetryM.addLine("pushed ball");
        telemetryM.update();
    }

    public void pullBall(){
        ballServo.setPosition(pullPos);
        telemetryM.addLine("pulled ball");
        telemetryM.update();

    }

}
