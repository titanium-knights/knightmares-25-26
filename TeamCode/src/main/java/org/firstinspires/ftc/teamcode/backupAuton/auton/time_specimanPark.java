//package org.firstinspires.ftc.teamcode.backupAuton.auton;
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//@Autonomous(name="time_specimenPark", group="Linear OpMode")
//@Config
//public class time_specimanPark extends AutonMethods{
//    @Override
//    public void runOpMode() throws InterruptedException {
//        super.runOpMode();
//
//        //start timer
//        ElapsedTime runtime = new ElapsedTime();
//        waitForStart();
//        runtime.reset();
//
//        // scoring specimen
//        clawClose();
//        sleep(100);
//        moveBackward(1.2);
//        sleep(100);
//        clawPick();
//        sleep(500);
//        slidesRotateUp();
//        sleep(500);
//        slidesExtend();
//        sleep(500);
//        clawDrop();
//        sleep(500);
//        slidesRetractLittle();
//        sleep(1000);
//        moveForward(0.5);
//        sleep(1000);
//        slidesRetractMid();
//        sleep(800);
//        clawNeutral();
//        sleep(500);
//        slidesRotateDown();
//        sleep(800);
//        moveForward(0.3);
//        sleep(500);
//        rotateCcw(0.25);
//        moveForward(2);
//
//        telemetry.addLine("Run time:" +  runtime.toString());
//        telemetry.update();
//    }
//}
