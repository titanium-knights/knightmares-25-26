package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
import org.firstinspires.ftc.teamcode.utilities.Storer;
//import org.firstinspires.ftc.teamcode.utilities.Rotator;

@Configurable
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Intake intake;
    Outtake outtake;
    SwerveDrive drive;
    Storer storer;
//    Rotator rotator;

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

//        this.rotator = new Rotator(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap, telemetry);
        this.drive = new SwerveDrive(hardwareMap, telemetry);
        this.storer = new Storer(hardwareMap, telemetry);
        this.outtake = new Outtake(hardwareMap, telemetry);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        intake.pullBall();
    }

    @Override
    public void loop() {

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
        stick_margin = 0.1f;
        move(x, y, turn);
        //Subsystems
        /*
         Okay so these r the buttons

         lt                   rt
         lb:Intake            rb:Outtake


         DPAD
               ^: ballPush
         <:         >:
               v: ballPull(passive)


         XYAB
               Y:two
         X:one      B:three
               A
         */

        if((gamepad1.dpad_up)){
//            ballButton = ButtonPressState.PRESSED_GOOD;
//            ballState = true;
            intake.pushBall();
            telemetryM.addLine("dpad up");
            telemetryM.update();

        } else if((gamepad1.dpad_down)){
//            ballButton = ButtonPressState.PRESSED_GOOD;
//            ballState = true;
            intake.pullBall();
            telemetryM.addLine("dpad down");
            telemetryM.update();

        } else {
//            ballButton = ButtonPressState.PRESSED_GOOD;
//            ballState = false;
            telemetryM.addLine("default down");
            telemetryM.update();
            intake.pullBall();
        }

        if (gamepad1.left_bumper) {
            intake.takeIn();
        } else if (gamepad1.right_bumper) {
            outtake.shoot();
        } else {
            intake.stopIntake();
            outtake.stopOuttake();
        }

        if (gamepad1.x){
            storer.toOne();
        } else if (gamepad1.y){
            storer.toTwo();
        } else if (gamepad1.b){
            storer.toThree();
        }

//        if (gamepad1.right_bumper) {
//            rotator.rotateUp();
//        } else if (gamepad1.left_bumper) {
//            rotator.rotateDown();
//        } else{
//            rotator.stopRotator();
//        }




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