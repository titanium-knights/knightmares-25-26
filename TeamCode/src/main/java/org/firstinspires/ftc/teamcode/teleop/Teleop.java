package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pinpoint.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
import org.firstinspires.ftc.teamcode.utilities.Storer;

@Configurable
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {

    Intake intake;
    Outtake outtake;
    SwerveDrive drive;
    Storer storer;
    Rotator rotator;
    GoBildaPinpointDriver odo;

    final double normalPower = 0.9;

    public float stick_margin = 0.7f;

    public boolean intakeState = false;
    public boolean shootState = false;

    private TelemetryManager telemetryM;

    enum ButtonPressState {
        PRESSED_GOOD,
        DEPRESSED,
        UNPRESSED
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

        this.intake = new Intake(hardwareMap, telemetry);
        this.drive = new SwerveDrive(hardwareMap, telemetry);
        this.storer = new Storer(hardwareMap, telemetry);
        this.outtake = new Outtake(hardwareMap, telemetry);
        this.rotator = new Rotator(hardwareMap, telemetry);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    @Override
    public void loop() {

        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;

        stick_margin = 0.1f;
        move(x, y, turn);



        if (gamepad1.left_bumper) {
            intake.pushBall();
        } else if (gamepad1.right_bumper) {
            intake.pullBall();
        } else {
            telemetryM.addLine("default down");
            telemetryM.update();
            intake.pullBall();
        }

        if (gamepad1.left_trigger > 0.1) {
            intake.run();
        } else if (gamepad1.right_trigger > 0.1) {
            outtake.shoot();
            intake.run();
        } else {
            intake.stop();
            outtake.stopOuttake();
        }



        if (gamepad1.left_stick_x > 0.3) {
            rotator.rotateRight();
        } else if (gamepad1.left_stick_x < -0.3) {
            rotator.rotateLeft();
        } else {
            rotator.stop();
        }



        if (gamepad1.dpad_left) {
            storer.toOne();
        } else if (gamepad1.dpad_up) {
            storer.toTwo();
        } else if (gamepad1.dpad_right) {
            storer.toThree();
        }

        if (gamepad2.a) {

        }
    }

    public void move(float x, float y, float turn) {

        if (Math.abs(x) <= stick_margin) x = 0f;
        if (Math.abs(y) <= stick_margin) y = 0f;
        if (Math.abs(turn) <= stick_margin) turn = 0f;

        double multiplier = normalPower;

        drive.move(-x * multiplier, y * multiplier, -turn * multiplier);
    }
}
