//package org.firstinspires.ftc.teamcode.backupAuton.auton;
////this is TIME BASED
//// TODO CHANGE NUMBERS AFTER
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
//@Autonomous(name="time_park", group="Linear OpMode")
//@Config
//public class time_park extends AutonMethods {
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
//        moveBackward(0.5);
//        stopDrive();
//        moveRight(2);
//        stopDrive();
//
//    }
//}