//package org.firstinspires.ftc.teamcode.teleop;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.rr.GoBildaPinpointDriver;
//import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
//import org.firstinspires.ftc.teamcode.utilities.Slides;
////import org.firstinspires.ftc.teamcode.utilities.PullUp;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//import org.firstinspires.ftc.teamcode.utilities.PullUp;
//import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
//
//@Config
//@TeleOp(name="JustDrive Teleop")
//public class JustDrive extends OpMode {
//
//    SimpleMecanumDrive drive;
//
//    //Set normal power constant to 1, no point in slowing the robot down
//    final double normalPower = 1;
//
//    // in case of joystick drift, ignore very small values
//    public float stick_margin = 0.7f;
//
//    GoBildaPinpointDriver odo; // Declare OpMode member for the Odometry Computer
//
//    double oldTime = 0;
//
//
//    enum ButtonPressState {
//        PRESSED_GOOD, //the first time we see the button pressed
//        DEPRESSED, //you haven't let go
//        UNPRESSED // its not pressed
//    }
//
//    boolean slowMode = false;
//
//    @Override
//    public void init() {
//        this.drive = new SimpleMecanumDrive(hardwareMap);
//    }
//
//    @Override
//    public void loop() {
//
//        //DRIVE
//        float x = gamepad2.left_stick_x;
//        float y = gamepad2.left_stick_y;
//        float turn = gamepad2.right_stick_x;
//
//    }
//
//    public void move(float x, float y, float turn) {
//        // if the stick movement is negligible, set STICK_MARGIN to 0
//        if (Math.abs(x) <= stick_margin) x = .0f;
//        if (Math.abs(y) <= stick_margin) y = .0f;
//        if (Math.abs(turn) <= stick_margin) turn = .0f;
//
//        //Notation of a ? b : c means if a is true do b, else do c.
//        double multiplier = normalPower;
//        drive.move(-x * multiplier, y * multiplier, turn * multiplier);
//    }
//
//}
