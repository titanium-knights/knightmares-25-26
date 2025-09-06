//package org.firstinspires.ftc.teamcode.backupAuton.auton;
////this is TIME BASED
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
//@Autonomous(name="time_tuner", group="Linear OpMode")
//@Config
//public class time_tuner extends AutonMethods {
//
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        super.runOpMode();
//        telemetry.addData("Initialized:", "Hopefully");
//        telemetry.update();
//        //start timer
//        ElapsedTime runtime = new ElapsedTime();
//        waitForStart();
//        runtime.reset();
//
//        moveForward(2);
//        // moveBackward(2);
//        // moveLeft(2);
//        // moveRight(2);
//
//        stopDrive();
//    }
//}