package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.backupAuton.auton.AutonMethods;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.PullUp;
import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.SlidesState;

@Config
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {
    Slides slides;
    PullUp pullup;
    Claw claw;
    ClawRotator clawRotator;
    SimpleMecanumDrive drive;

    final double normalPower = 0.9;

    // in case of joystick drift, ignore very small values
    public float stick_margin = 0.7f;

    public boolean clawOpen = true;
    public boolean cRotatorAtDrop = false;

    enum ButtonPressState {
        PRESSED_GOOD, //the first time we see the button pressed
        DEPRESSED, //you haven't let go
        UNPRESSED // its not pressed
    }
    ButtonPressState slideButton;
    ButtonPressState rotatorButton;
    ButtonPressState clawButton;
    ButtonPressState cRotatorButton;
    ButtonPressState slideManual;
    ButtonPressState slideManualUp;
    ButtonPressState ultimateButton;

    boolean slowMode = false;
    boolean pullupstate1 = false;
    boolean pullupstate2 = false;
    boolean pulldownstate1 = false;
    boolean pulldownstate2 = false;
    int slidesPos;
    double time;
    int autonAction = 0;

    boolean extended = false;

    ElapsedTime runtime = new ElapsedTime();
    @Override
    public void init() {
        this.drive = new SimpleMecanumDrive(hardwareMap);
        this.slides = new Slides(hardwareMap, telemetry);
        this.pullup = new PullUp(hardwareMap);
        this.clawButton = ButtonPressState.UNPRESSED;
        this.cRotatorButton = ButtonPressState.UNPRESSED;
        this.slideButton = ButtonPressState.UNPRESSED;
        this.slideManual = ButtonPressState.UNPRESSED;
        this.slideManualUp = ButtonPressState.UNPRESSED;
        this.ultimateButton = ButtonPressState.UNPRESSED;

        this.claw = new Claw(hardwareMap, telemetry);
        this.clawRotator = new ClawRotator(hardwareMap, telemetry);
    }

    @Override
    public void loop() {

        if (gamepad1.left_trigger>0.1f && slideButton == ButtonPressState.UNPRESSED) {
            slideButton = ButtonPressState.PRESSED_GOOD;
        } else if (gamepad1.left_trigger>0.1f && slideButton==ButtonPressState.PRESSED_GOOD) {
            slideButton = ButtonPressState.DEPRESSED;
        } else if (gamepad1.left_trigger<0.1f) slideButton = ButtonPressState.UNPRESSED;

        if ((slides.getRotatorEncoder() < 900 && slides.getEncoder() > 200) ) {
            slides.retract();
        } else {

            if (gamepad1.left_bumper) {
                slides.retract();
                extended = false;
                telemetry.addLine("slides rotator: " + String.valueOf(slides.getRotatorEncoder()));
                telemetry.addLine("slides: " + String.valueOf(slides.getEncoder()));
                telemetry.update();
            } else if (gamepad1.right_bumper) {
                if (slides.getRotatorEncoder() < 900 && slides.getRotatorEncoder() > -1000) {
                    stop();
                } else {
                    slides.extend();
                    extended = true;
                }
                telemetry.addLine("slides rotator: " + String.valueOf(slides.getRotatorEncoder()));
                telemetry.addLine("slides: " + String.valueOf(slides.getEncoder()));
                telemetry.update();
            } else {
                slides.stop();
            }
        }

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

        if (gamepad1.right_trigger > 0.5) {
            slides.rotateRight();
            telemetry.addLine("rotating slides: " + String.valueOf(slides.getRotatorEncoder()));

        } else if (gamepad1.left_trigger > 0.5) {
            if (extended) stop();
            else slides.rotateLeft();
            telemetry.addLine("rotating slides: " + String.valueOf(slides.getRotatorEncoder()));
            telemetry.update();
        } else {
            slides.stopRotator();
        }
        if (gamepad2.dpad_down){
            slowMode = true;
            telemetry.addLine("keep up: " + String.valueOf(slides.getRotatorEncoder()));
            telemetry.update();
        } else if (gamepad2.dpad_up){
            slowMode = false;
            telemetry.update();
        }

        // improved code by yours truly:
        if (gamepad1.y && (clawButton == ButtonPressState.UNPRESSED)) {
            if (!clawOpen) {
                claw.open();
                telemetry.addLine("open claw: " + claw.getPosition());
                telemetry.update();
                clawOpen = true;
                clawButton = ButtonPressState.PRESSED_GOOD;
            } else {
                claw.close();
                telemetry.addLine("close claw: " + claw.getPosition());
                telemetry.update();
                clawOpen = false;
                clawButton = ButtonPressState.PRESSED_GOOD;
            }

        } else if (!gamepad1.y && (clawButton == ButtonPressState.PRESSED_GOOD)) {
            clawButton = ButtonPressState.UNPRESSED;
        }







        if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && !cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toDrop();
            telemetry.addLine("claw to drop position: " + clawRotator.getPosition());
            cRotatorAtDrop = true;
        } else if (gamepad1.a && (cRotatorButton == ButtonPressState.UNPRESSED) && cRotatorAtDrop) {
            cRotatorButton = ButtonPressState.PRESSED_GOOD;
            clawRotator.toPick();
            clawRotator.toPick();
            telemetry.addLine("claw to pick position: " + clawRotator.getPosition());
            cRotatorAtDrop = false;
        } else if (!(gamepad1.a) && (cRotatorButton == ButtonPressState.PRESSED_GOOD)){
            cRotatorButton = ButtonPressState.UNPRESSED;
        }

        if ((gamepad2.left_trigger > 0.7f) && (ultimateButton == ButtonPressState.UNPRESSED)){
            try {
                hangSpecimen();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        if (gamepad1.dpad_down) {
            clawRotator.toNeutral();
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

    public void setAutonAction (int action) {
        autonAction = action;
        runtime.reset();

    }
    public void hangSpecimen() throws InterruptedException { //Ultimate button
        while(autonAction!=-1){
            switch (autonAction) {
                case 0: // move back
                    ultimateButton = ButtonPressState.PRESSED_GOOD;
                    while (runtime.seconds() < 3) {
                        telemetry.addLine("case0 " +  runtime.seconds());
                        telemetry.update();
                        drive.move(0, -1, 0);
                    }
                    runtime.reset();
                    setAutonAction(1);
                    telemetry.addLine(Integer.toString(autonAction));
                    break;
                case 1: // move claw
                    telemetry.addLine("case1");
                    telemetry.update();
                    clawRotator.toDrop();
                    runtime.reset();
                    setAutonAction(2);
                    break;
                case 2: // rotate slides
                    telemetry.addLine("case2");
                    telemetry.update();
                    while (runtime.seconds() < 0.8){
                        slides.rotateRight();
                    }
                    runtime.reset();
                    setAutonAction(3);
                    break;
                case 3: // extend slides
                    telemetry.addLine("case3");
                    telemetry.update();
                    while (runtime.seconds() < 0.8){
                        slides.extend();
                    }
                    runtime.reset();
                    setAutonAction(4);
                    break;
                case 4: // move claw down
                    telemetry.addLine("case4");
                    telemetry.update();
                    clawRotator.toPick();
                    runtime.reset();
                    setAutonAction(5);
                    break;
                case 5: // retract slides a bit
                    telemetry.addLine("case5");
                    telemetry.update();

                    if (runtime.seconds() > 0.4) {
                        slides.retract();
                    }
                    runtime.reset();
                    setAutonAction(6);
                    break;
                case 6: // move back?
                    telemetry.addLine("case6");
                    telemetry.update();
                    drive.move(0, 1, 0);
                    while(runtime.seconds() < 0.2){
                        drive.move(0, 1, 0);
                    }
                    runtime.reset();
                    setAutonAction(7);
                    break;
                case 7: // let go of claw
                    telemetry.addLine("case7");
                    telemetry.update();
                    claw.open();
                    runtime.reset();
                    break;


            }
        }
    }

}