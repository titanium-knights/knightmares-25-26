//package org.firstinspires.ftc.teamcode.backupAuton.auton;
////this is TIME BASED
//// TODO CHANGE NUMBERS AFTER
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//import org.firstinspires.ftc.teamcode.utilities.SimpleMecanumDrive;
//
//@Autonomous(name="time_nearHuman", group="Linear OpMode")
//@Config
//
//public class time_nearHuman extends LinearOpMode {
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
//
//        //TODO add this if we can go diagolanly and not hit stuff (to save time)
//        //go diagonally towards the first sample,
//        // a little more x so we behind the samplet
////        drivetrain.move(10, 8, 0);
////        telemetry.addLine("did it make it behind the first sample");
////        telemetry.update();
//
//        // rightOne
//        drivetrain.move(-POWER, 0, 0);
//        telemetry.addLine("Push INITIAL sample in human zone");
//        telemetry.update();
//        sleep(1200);
//
//        // forwardTwo
//        drivetrain.move(0, POWER, 0);
//        telemetry.addLine("move up");
//        telemetry.update();
//        sleep(2200);
//
//        // do a 90 degree turn, so the side with nothing pushes the sample
////        drivetrain.move(0, 0, -POWER);
////        telemetry.addLine("turn 90 Degree");
////        telemetry.update();
////        sleep(2700);
////        // 2000 - 45 degrees
////        // 3000 - 100 degrees
////        // 4000 - 135 degrees
//
//        // rightHalf
//        drivetrain.move(-POWER, 0, 0);
//        telemetry.addLine("move beside first sample");
//        telemetry.update();
//        sleep(500);
//
//        // backOneAndHalf
//        drivetrain.move(0, -POWER, 0);
//        telemetry.addLine("push first sample in human");
//        telemetry.update();
//        sleep(1900);
//
//        // forwardOneAndHalf
//        drivetrain.move(0, POWER, 0);
//        telemetry.addLine("Go back to samples (up)");
//        telemetry.update();
//        sleep(1900);
//
//        // rightHalf
//        drivetrain.move(-POWER, 0, 0);
//        telemetry.addLine("move beside second sample");
//        telemetry.update();
//        sleep(700);
//
//        // backOneAndHalf
//        drivetrain.move(0, -0.4, 0);
//        telemetry.addLine("push SECOND sample in human");
//        telemetry.update();
//        sleep(2000);
//
//        // forwardOneAndHalf
//        drivetrain.move(0, 0.4, 0);
//        telemetry.addLine("Go back to samples (up)");
//        telemetry.update();
//        sleep(2000);
//
//        // rightHalf
//        drivetrain.move(-POWER, 0, 0);
//        telemetry.addLine("move beside second sample");
//        telemetry.update();
//        sleep(600);
//
//        // forwardTwo
//        drivetrain.move(0, -0.4, 0);
//        telemetry.addLine("push THIRD sample in human");
//        telemetry.update();
//        sleep(2000);
//    }
//}
