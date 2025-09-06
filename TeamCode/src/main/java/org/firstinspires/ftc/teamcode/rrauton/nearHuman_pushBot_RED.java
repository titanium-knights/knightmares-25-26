//package org.firstinspires.ftc.teamcode.rrauton;
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
//@Autonomous(name = "nearHuman_pushBot_RED", group = "Autonomous")
//public class nearHuman_pushBot_RED extends LinearOpMode {
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        Pose2d begPose = new Pose2d(60, 12, 0); // 0,0 is the center of the board TODO: tune
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
//        //TODO ALL NUMBERS ARE NOT TUNED
//        // (the radians 100%) BUT the x and y's are approximate assume robot in center of block
//        TrajectoryActionBuilder tab = drivetrain.actionBuilder(begPose)
//                //todo start from y = 60 x = 12
//                .lineToX(36) // up 1 block,
//                .setTangent(Math.toRadians(90)) // might be 270, needs to be tuned
//                .lineToY(12) // 3 blocks left
//                .setTangent(Math.toRadians(0))
//                .lineToX(44) // move up by 1/3 of a block
//                .setTangent(Math.toRadians(90))
//                .lineToY(60) //move right into park
//                .lineToY(12) //move left to the samples
//                .setTangent(Math.toRadians(0))
//                .lineToX(52) //move up to middle sample
//                .setTangent(Math.toRadians(90))
//                .lineToY(60) //move right into park
//                .lineToY(12) //move left to the samples
//                .setTangent(Math.toRadians(0))
//                .lineToX(60) //move up to highest sample
//                .setTangent(Math.toRadians(90))
//                .lineToY(60) //move right into park
//                //FINAL PARK
//        ;
//
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
