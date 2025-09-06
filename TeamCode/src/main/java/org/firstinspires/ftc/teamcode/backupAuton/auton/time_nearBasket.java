//package org.firstinspires.ftc.teamcode.backupAuton.auton;
////this is TIME BASED
//// TODO CHANGE NUMBERS AFTER
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
//@Autonomous(name="time_nearBasket", group="Linear OpMode")
//@Config
//public class time_nearBasket extends AutonMethods {
//
//@Override
//    public void runOpMode() throws InterruptedException {
//        super.runOpMode();
//        telemetry.addData("Initialized:", "Hopefully");
//        telemetry.update();
//        //start timer
//        ElapsedTime runtime = new ElapsedTime();
//        waitForStart();
//        runtime.reset();
//
//        // scoring preload
//        clawClose();
//        // clawClose(); // twice because nothing ever works the first time, for some reason
//        clawDrop();
//        // clawDrop();
//        // unlatch();
//        // unlatch();
//        moveForward(1);
//        rotateCw(0.15);
//        moveBackward(0.9);
//        sleep(500);
//        clawDrop();
//        // clawDrop();
//        slidesRotateUp();
//        clawPick();
//        // clawPick();
//        // latch();
//        sleep(500);
//        keepup();
//        slidesExtend();
//        moveBackward(0.2);
//        sleep(500);
//        clawDrop();
//        // clawDrop();
//        sleep(500);
//        clawOpen();
//        // clawOpen();
//        clawPick();
//        moveForward(0.2);
//        // unlatch();
//        sleep(500);
//        slidesRetractALot();
//        sleep(500);
//        stopSlidesRot();
//        slidesRotateDown();
//
//        /*
//
//        moveForward(0.2);
//        clawPick();
//        unlatch();
//        slidesRetract();
//        slidesRotateDown();
//        moveForward(0.9 );
//        rotateCcw(0.15);
//
//        // *** past here i didnt tune but it should be close
//        moveBackward(0.4);
//        moveLeft(0.8);
//        moveForward(0.2);
//        clawClose();
//        rotateCw(0.125);
//        moveBackward(0.5);
//        // slidesRotateUp();
//        clawPick();
//        // slidesExtend();
//        moveBackward(0.2);
//        clawDrop();
//        clawOpen();
//        moveForward(0.2);
//        clawPick();
//        // slidesRetract();
//        // slidesRotateDown();
//        */
//
//        telemetry.addLine("Run time:" +  runtime.toString());
//        telemetry.update();
//    }
//}
