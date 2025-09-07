package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.Intake;
// import org.firstinspires.ftc.teamcode.utilities.Rotator;

import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Configurable
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Intake intake;
    // Rotator rotator;
    SimpleMecanumDrive drive;

    final double normalPower = 0.9;

    // in case of joystick drift, ignore very small values
    public float stick_margin = 0.7f;

    public boolean intakeState = false;

    enum ButtonPressState {
        PRESSED_GOOD, //the first time we see the button pressed
        DEPRESSED, //you haven't let go
        UNPRESSED // its not pressed
    }
    ButtonPressState intakeButton;
    ButtonPressState rotatorButton;
    ButtonPressState ultimateButton;

    boolean slowMode = false;
    double time;
    int autonAction = 0;


    ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init() {
        this.drive = new SimpleMecanumDrive(hardwareMap);
        this.rotatorButton = ButtonPressState.UNPRESSED;
        this.intakeButton = ButtonPressState.UNPRESSED;
        this.ultimateButton = ButtonPressState.UNPRESSED;

        // this.rotator = new Rotator(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap);
    }

    @Override
    public void loop() {



//        DRIVETRAIN TELEMETRY
//        telemetry.addLine(String.valueOf(drive.getfl()) + "get front left");
//        telemetry.addLine(String.valueOf(drive.getbr()) + "get back right");
//        telemetry.addLine(String.valueOf(drive.getfr()) + "get front right");
//        telemetry.addLine(String.valueOf(drive.getbl()) + "get back left");
//        telemetry.update();

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
        if (slowMode) {
            telemetry.addLine("slowmode");
            float slowx = (float)0.4*x;
            float slowy = (float)0.4*y;
            stick_margin = 0.3f;
            move(slowx, slowy, turn);
        } else {
            stick_margin = 0.1f;
            move(x, y, turn);
        }

        if (gamepad1.x) {
            intake.runMotor();
        }

        if (gamepad1.y) {
            intake.runServo();
        }


    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0

        if (Math.abs(x) <= stick_margin) x = .0f;
        if (Math.abs(y) <= stick_margin) y = .0f;
        if (Math.abs(turn) <= stick_margin) turn = .0f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
        drive.move(-x * multiplier, y * multiplier, -turn * multiplier);
    }

}