package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
import org.firstinspires.ftc.teamcode.utilities.Rotator;

//import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Configurable
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Intake intake;
    SwerveDrive drive;
    Rotator rotator;

    final double normalPower = 0.9;

    // in case of joystick drift, ignore very small values
    public float stick_margin = 0.7f;

    public boolean intakeState = false;
    public boolean rotatorState = false;
    public boolean shootState = false;

    private TelemetryManager telemetryM;

    enum ButtonPressState {
        PRESSED_GOOD, //the first time we see the button pressed
        DEPRESSED, //you haven't let go
        UNPRESSED // its not pressed
    }
    ButtonPressState intakeButton;
    ButtonPressState shootButton;
    ButtonPressState rotatorButton;
    ButtonPressState ultimateButton;
    ButtonPressState ballButton;

    boolean slowMode = false;
    double time;
    int autonAction = 0;
    boolean intakeRunning = false;
    boolean ballState = false;


    ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init() {
        this.rotatorButton = ButtonPressState.UNPRESSED;
        this.intakeButton = ButtonPressState.UNPRESSED;
        this.shootButton = ButtonPressState.UNPRESSED;
        this.ultimateButton = ButtonPressState.UNPRESSED;
        this.ballButton = ButtonPressState.UNPRESSED;


        this.rotator = new Rotator(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap);
        this.drive = new SwerveDrive(hardwareMap, telemetry);
    }

    @Override
    public void loop() {

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
        stick_margin = 0.1f;
        move(x, y, turn);

        if((gamepad2.right_trigger > 0.2) && (!ballState)&& (intakeButton == ButtonPressState.UNPRESSED)){
            ballButton = ButtonPressState.PRESSED_GOOD;
            ballState = true;
            intake.pushBall();

        } else if ((gamepad2.right_trigger < 0.2) && (ballState)&& (intakeButton == ButtonPressState.UNPRESSED)){
            ballButton = ButtonPressState.PRESSED_GOOD;
            ballState = false;
            intake.pushBall();
        } else {
            ballButton = ButtonPressState.UNPRESSED;
        }
        if (gamepad2.x && !intakeState && (intakeButton == ButtonPressState.UNPRESSED)) {
            intakeButton = ButtonPressState.PRESSED_GOOD;
            intakeState = true;
            intake.takeIn();
        } else if (gamepad2.x && intakeState && (intakeButton == ButtonPressState.UNPRESSED)) {
            intakeState = false;
            intakeButton = ButtonPressState.PRESSED_GOOD;
            intake.shoot();
        } else {
            intakeButton = ButtonPressState.UNPRESSED;
        }

        if (gamepad2.right_bumper && (rotatorButton == ButtonPressState.PRESSED_GOOD) && (rotatorState == false)) {
            rotator.rotateUp();
            rotatorState = true;
            rotatorButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad2.left_bumper && (rotatorButton == ButtonPressState.PRESSED_GOOD) && (rotatorState == true)) {
            rotator.rotateDown();
            rotatorState = false;

            rotatorButton = ButtonPressState.PRESSED_GOOD;
        } else{
            rotator.stopRotator();
            rotatorButton = ButtonPressState.UNPRESSED;
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