//package org.firstinspires.ftc.teamcode.backupAuton.auton;
////this is TIME BASED
//// TODO CHANGE NUMBERS AFTER, THIS IS FOR WHEN WE DONT USE CLAW
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
//
//@Autonomous(name="time_nearBasketPUSHBOT", group="Linear OpMode")
//@Config
//
//public class time_nearBasket_PUSHBOT extends AutonMethods {
//    //check what this does (just next line)
//    public final double POWER = 0.5;
//    public void runOpMode() throws InterruptedException {
//        telemetry.addLine("STARTING! (hopfully)");
//        telemetry.update();
//
//        //start timer
//        ElapsedTime runtime = new ElapsedTime();
//        SimpleMecanumDrive drivetrain = new SimpleMecanumDrive(hardwareMap);
//
//        waitForStart();
//        runtime.reset();
//
//        // start it bordering the middle on the side close to human
//
//        // push INITIAL sample (given, closer to basket)
//        moveRight(1.2);
//        // move up behind the 3 samples
//        moveForward(2);
//        // turn so flat side facing sample
//        rotateCcw(0.25);
//        // move behind the first sample
//        moveBackward(0.3);
//        // push to zone and come back
//        moveLeft(1.8);
//        moveRight(1.8);
//        // move behind the second sample
//        moveBackward(0.4);
//        // push to zone and come back
//        moveLeft(1.8);
//        moveRight(1.8);
//        // move behind the second sample
//        moveBackward(0.4);
//        // push into zone and park
//        moveLeft(2.2);
//    }
//}
