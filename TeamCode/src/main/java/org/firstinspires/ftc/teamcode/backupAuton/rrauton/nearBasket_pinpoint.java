//package org.firstinspires.ftc.teamcode.backupAuton.rrauton;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.Trajectory;
//import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.rr.GoBildaPinpointDriver;
//import org.firstinspires.ftc.teamcode.rr.PinpointDrive;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//import org.firstinspires.ftc.teamcode.utilities.PullUp;
//import org.firstinspires.ftc.teamcode.utilities.Slides;
//
//@Config
//@Autonomous(name = "pin_nearBasket", group = "Autonomous")
//public class nearBasket_pinpoint extends LinearOpMode {
//
//    GoBildaPinpointDriver odo; // Declare OpMode member for the Odometry Computer
//    double oldTime = 0;
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//
//        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo"); // matches config??
//
//        telemetry.addLine(odo.getConnectionInfo());
//        telemetry.addLine(odo.getDeviceName());
//
//        // length: 406 mm
//        // width: 430 mm
//        odo.setOffsets(58.4, 55.8); //these are tuned for 3110-0002-0001 Product Insight #1
//
//        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
//
//        // i think: the x pod should increase when you move the robot forward, otherwise replace FORWARD with REVERSE
//        //          the y pod should increase when you move the robot left, otherwise replace FORWARD with REVERSE
//        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
//
//        odo.resetPosAndIMU();
//        // maybe recalibrateIMU();
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.addData("X offset", odo.getXOffset());
//        telemetry.addData("Y offset", odo.getYOffset());
//        telemetry.addData("Device Version Number:", odo.getDeviceVersion());
//        telemetry.addData("Device Scalar", odo.getYawScalar());
//
//        Pose2d begPose = new Pose2d(-60, -12, 0); // 0,0 is the center of the board TODO: tune
//
//        telemetry.addData("Initialized: ", "");
//        telemetry.update();
//
//        PinpointDrive drivetrain = new PinpointDrive(hardwareMap, begPose);
//        drivetrain.updatePoseEstimate();
//
//        //TODO: add 1 arg constructors to these hardware classes to prevent errors
//        Claw claw = new Claw(hardwareMap);
//        Slides slides = new Slides(hardwareMap);
//        PullUp pullup = new PullUp(hardwareMap);
//
//        // these were added to the util classes they basically do the same thing as claw.close but they need to be funky for roadrunner so its a different method
//        Actions.runBlocking(claw.closeAction());
//
//        // the actual path
//        // each "tab" is like a sequence of x and y movements
//        //TrajectoryActionBuilder tab = drivetrain.actionBuilder(begPose)
//        //        .lineToX(-36)
//        //        // .setTangent is needed when switching between running x and y
//        //        .setTangent(Math.toRadians(90)) // might be 270, needs to be tuned
//        //        .lineToY(-36)
//        //        .setTangent(Math.toRadians(0))
//        //        .lineToX(-12);
//
//        // you run stuff that is not drivetrain in between each tab
//
//        TrajectoryActionBuilder specimenTab1 = drivetrain.actionBuilder(begPose)
//        //        .setTangent(Math.toRadians(90))
//                .lineToX(100);
//        //        .setTangent(0)
//        //        .lineToX(-36);
//
//        // move the arm
//        // move the slides
//        // move forward
//
//        //TrajectoryActionBuilder specimenTab2 = drivetrain.actionBuilder(new Pose2d(-36, 0, 0))
//        //        .lineToX(-30);
//
//        // move the slides down
//        // open the claw
//        // move back to initial position
//
//        //TrajectoryActionBuilder specimenTab3 = drivetrain.actionBuilder(new Pose2d(-30, 0, 0))
//        //        .lineToX(-60)
//        //        .setTangent(Math.toRadians(90))
//        //        .lineToY(12);
//
//        waitForStart();
//        resetRuntime();
//
//        // doesn't build everything on time unless you sleep
//        // start low and build your way up
//        //sleep(10000); // adjust if needed.
//
//        if (isStopRequested()) return;
//
//        //Action trajectoryAction = tab.build();
//        Action trajectoryAction = specimenTab1.build();
//        sleep(10000); // adjust if needed.
//
//        // the line where you put the tabs and the non-drivetrain commands all together
//        //TODO: this code appears to be taken from the AK code base, which handles some of the hardware classes differently.
//        // Many of the methods used here are not present in the current hardware classes.
//        SequentialAction specimenPlaceAction = new SequentialAction(trajectoryAction, claw.openAction());
//
//        Actions.runBlocking(specimenPlaceAction);
//        //Actions.runBlocking(new SequentialAction(
//                //specimenPlaceAction,
//                //trajectoryAction
//        //));
//        sleep(10000); // adjust if needed.
//    }
//}
