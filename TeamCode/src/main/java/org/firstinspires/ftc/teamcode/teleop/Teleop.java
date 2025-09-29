package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
// import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
// import org.firstinspires.ftc.teamcode.utilities.Rotator;

//import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;

@Configurable
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    // Intake intake;
    SwerveDrive drive;
    // Rotator rotator;
//    SimpleMecanumDrive drive;

    final double normalPower = 0.9;

    // in case of joystick drift, ignore very small values
    public float stick_margin = 0.7f;

    public boolean intakeState = false;
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

    boolean slowMode = false;
    double time;
    int autonAction = 0;
    boolean intakeRunning = false;


    ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init() {
//        this.drive = new SimpleMecanumDrive(hardwareMap);
        this.rotatorButton = ButtonPressState.UNPRESSED;
        this.intakeButton = ButtonPressState.UNPRESSED;
        this.shootButton = ButtonPressState.UNPRESSED;
        this.ultimateButton = ButtonPressState.UNPRESSED;

        // this.rotator = new Rotator(hardwareMap, telemetry);
        // this.intake = new Intake(hardwareMap);
        // this.drive = new SwerveDrive(hardwareMap);
    }

    @Override
    public void loop() {

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
        stick_margin = 0.1f;
        move(x, y, turn);
// INTAKE STUFF

//        if (gamepad1.a == true){
//
//        }
//
//        if (gamepad1.x && !intakeState && (intakeButton == ButtonPressState.UNPRESSED)) {
//            intakeButton = ButtonPressState.PRESSED_GOOD;
//            intakeState = true;
//            intake.takeIn();
//        } else if (gamepad1.x && intakeState && (intakeButton == ButtonPressState.UNPRESSED)) {
//            intakeState = false;
//            intakeButton = ButtonPressState.PRESSED_GOOD;
//            intake.stopIntake();
//        } else {
//            intakeButton = ButtonPressState.UNPRESSED;
//        }

        // shoot
//        if (gamepad1.b && !shootState && (shootButton == ButtonPressState.UNPRESSED)) {
//            shootButton = ButtonPressState.PRESSED_GOOD;
//            shootState = true;
//            if (!intakeRunning) {
//                intake.closeClaw();
//                intakeRunning = true;
//            }
//            intake.takeIn();
//        } else if (gamepad1.b && shootState && (shootButton == ButtonPressState.UNPRESSED)) {
//            shootState = false;
//            shootButton = ButtonPressState.PRESSED_GOOD;
//            intakeRunning = false;
//            intake.stopIntake();
//        } else {
//            shootButton = ButtonPressState.UNPRESSED;
//        }

        // rotator
        if (gamepad2.right_bumper){

        }




    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0

        if (Math.abs(x) <= stick_margin) x = .0f;
        if (Math.abs(y) <= stick_margin) y = .0f;
        if (Math.abs(turn) <= stick_margin) turn = .0f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
//        drive.move(-x * multiplier, y * multiplier, -turn * multiplier);
    }
    public void moveRotator(){
        if(this.rotatorButton == ButtonPressState.PRESSED_GOOD) {
            if (gamepad2.right_bumper) {
                this.intakeButton = ButtonPressState.PRESSED_GOOD;
            }
            if (gamepad2.left_bumper) {
                this.shootButton = ButtonPressState.PRESSED_GOOD;
            }
        }else{
            System.out.println("The rotator button is not pressed.")
        }

    }
}