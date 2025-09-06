//package org.firstinspires.ftc.teamcode.backupAuton.rrauton;
//
//import com.acmerobotics.dashboard.config.Config;
//import com.acmerobotics.roadrunner.Action;
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.SequentialAction;
//import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//
//import org.firstinspires.ftc.teamcode.rr.MecanumDrive;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//import org.firstinspires.ftc.teamcode.utilities.PullUp;
//import org.firstinspires.ftc.teamcode.utilities.Slides;
//
//@Config
//@Autonomous(name = "rr_nearBasket", group = "Autonomous")
//public class nearBasket extends LinearOpMode {
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        Pose2d begPose = new Pose2d(-60, -12, 0); // 0,0 is the center of the board TODO: tune
//
//        telemetry.addData("Initialized: ", "");
//        telemetry.update();
//
//        MecanumDrive drivetrain = new MecanumDrive(hardwareMap, begPose);
//
//        //TODO: add 1 arg constructors to these hardware classes to prevent errors
//        Claw claw = new Claw(hardwareMap);
//        Slides slides = new Slides(hardwareMap);
//        PullUp pullup = new PullUp(hardwareMap);
//
//        // these were added to the util classes they basically do the same thing as claw.close but they need to be funky for roadrunner so its a different method
//        Actions.runBlocking(claw.closeAction());
//        // TODO: this function does not exist
//        // Actions.runBlocking(pullup.toInitPosAction());
//
//        // the actual path
//        // each "tab" is like a sequence of x and y movements
//        TrajectoryActionBuilder tab = drivetrain.actionBuilder(begPose)
//                .lineToX(-36)
//                // .setTangent is needed when switching between running x and y
//                .setTangent(Math.toRadians(90)) // might be 270, needs to be tuned
//                .lineToY(-36)
//                .setTangent(Math.toRadians(0))
//                .lineToX(-12)
//                .setTangent(Math.toRadians(90))
//                .lineToY(-48)
//                .setTangent(Math.toRadians(0))
//                .lineToX(-60)
//                .lineToX(-12)
//                .setTangent(Math.toRadians(90))
//                .lineToY(-60)
//                .setTangent(Math.toRadians(0))
//                .lineToX(-60)
//                .lineToX(-12)
//                .setTangent(Math.toRadians(90))
//                .lineToY(-66)
//                .setTangent(Math.toRadians(0))
//                .lineToX(-60);
//
//        // you run stuff that is not drivetrain in between each tab
//
//        TrajectoryActionBuilder specimenTab1 = drivetrain.actionBuilder(begPose)
//                .setTangent(Math.toRadians(90))
//                .lineToY(0)
//                .setTangent(0)
//                .lineToX(-36);
//
//        // move the arm
//        // move the slides
//        // move forward
//
//        TrajectoryActionBuilder specimenTab2 = drivetrain.actionBuilder(new Pose2d(-36, 0, 0))
//                .lineToX(-30);
//
//        // move the slides down
//        // open the claw
//        // move back to initial position
//
//        TrajectoryActionBuilder specimenTab3 = drivetrain.actionBuilder(new Pose2d(-30, 0, 0))
//                .lineToX(-60)
//                .setTangent(Math.toRadians(90))
//                .lineToY(12);
//
//        waitForStart();
//
//        // doesn't build everything on time unless you sleep
//        // start low and build your way up
//        sleep(1000); // adjust if needed.
//
//        if (isStopRequested()) return;
//        Action trajectoryAction = tab.build();
//
//        // the line where you put the tabs and the non-drivetrain commands all together
//        //TODO: this code appears to be taken from the AK code base, which handles some of the hardware classes differently.
//        // Many of the methods used here are not present in the current hardware classes.
//        //SequentialAction specimenPlaceAction = new SequentialAction(specimenTab1.build(), arm.toScoreSpecimenPosAction(), slides.getSlideAction(SlideState.MEDIUM), specimenTab2.build(), slides.getSlideAction(SlideState.MEDIUMSCORE), claw.openAction(), specimenTab3.build());
//
//        Actions.runBlocking(new SequentialAction(
//                // specimenPlaceAction,
//                trajectoryAction
//        ));
//    }
//}
