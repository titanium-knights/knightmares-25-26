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
import org.firstinspires.ftc.teamcode.utilities.Rotator;
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
    Rotator rotator;

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
        this.ballButton = ButtonPressState.UNPRESSED;

//        this.rotator = new Rotator(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap, telemetry);
        this.drive = new SwerveDrive(hardwareMap, telemetry);
        this.storer = new Storer(hardwareMap, telemetry);
        this.outtake = new Outtake(hardwareMap, telemetry);
        this.rotator = new Rotator(hardwareMap, telemetry);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
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

         lt:intake            rt:outtake
         lb:pusher            rb:puller


         DPAD
                  ^: in two
         <:in one         >: in three
                  v


         XYAB
                  Y:out two
         X:out one        B:out three
                  A

         joystick left: rotate turret left
         joystick right: rotate turret right
         */

        // INTAKE/OUTTAKE

        if((gamepad1.left_bumper)){
            intake.pushBall();
        } else if((gamepad1.right_bumper)){
            intake.pullBall();
        } else {
            telemetryM.addLine("default down");
            telemetryM.update();
            intake.pullBall();
        }

        if (gamepad1.left_trigger > 0.1) {
            intake.run();
        } else if (gamepad1.right_trigger > 0.1) {
            outtake.runOuttake();
        } else {
            intake.stop();
            outtake.stopOuttake();
        }

        // TURRET ROTATION

        if (gamepad1.left_stick_x > 0.3) {
            rotator.rotateRight();
        } else if (gamepad1.left_stick_x < -0.3) {
            rotator.rotateLeft();
        }






        // STORER

        if (gamepad1.dpad_left){
            storer.toInOne();
        } else if (gamepad1.dpad_up){
            storer.toInTwo();
        } else if (gamepad1.dpad_right){
            storer.toInThree();
        }

        if (gamepad1.x){
            storer.toOutOne();
        } else if (gamepad1.y){
            storer.toOutTwo();
        } else if (gamepad1.b){
            storer.toOutThree();
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