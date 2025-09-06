//package org.firstinspires.ftc.teamcode.backupAuton.auton;
////this is TIME BASED
//// TODO CHANGE NUMBERS AFTER
//import com.acmerobotics.dashboard.config.Config;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.util.ElapsedTime;
//
//
//@Autonomous(name="time_specimenNearHumanNEW", group="Linear OpMode")
//@Config
//public class time_specimenNearHumanNEW extends AutonMethods {
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
//        double counter = 0;
//
//        moveForward(convert(48)); // def adjust
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//
//
//        moveRight(convert(24)); // adjust
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//
//        moveForward(convert(48)); // def adjust
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//
//        moveRight(convert(6));//moving 1/4th not with half incase it hits 2nd sample
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveBackward(convert(36));// 12 space , 8 robot, 4 sample
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveForward(convert(36));
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveRight(convert(10)); //guess num
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveBackward(convert(36));
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveForward(convert(36));
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveRight(convert(6)); //guess num (exact would be 8)
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveBackward(convert(36));
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//        moveForward(convert(36));
//        counter++;
//        telemetry.addLine("counter" + counter);
//        telemetry.update();
//
//
//    }
//}