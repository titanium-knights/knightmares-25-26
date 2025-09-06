//package org.firstinspires.ftc.teamcode.backupAuton.rrauton;
//
//import com.acmerobotics.roadrunner.Pose2d;
//import com.acmerobotics.roadrunner.Vector2d;
//import com.acmerobotics.roadrunner.ftc.Actions;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.rr.PinpointDrive;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//
//@Autonomous
//public class testing_pinpoint extends OpMode {
//    Claw claw;
//    Pose2d beginPose;
//    PinpointDrive drive;
//
//    public void init() {
//        claw = new Claw(hardwareMap);
//        beginPose = new Pose2d(-17.5, 66, -Math.PI / 2);
//        drive = new PinpointDrive(hardwareMap, beginPose);
//    }
//
//    public void start() {
//        Actions.runBlocking(claw.closeAction());
//        Actions.runBlocking(
//                drive.actionBuilder(beginPose)
//                        .strafeTo(new Vector2d(-4, 42))
//                        .strafeTo(new Vector2d(-4, 35))
//                        .build()
//        );
//        Actions.runBlocking(claw.openAction());
//        drive.updatePoseEstimate();
//        Actions.runBlocking(
//                drive.actionBuilder(drive.pose)
//                        //.setTangent(Math.PI/2)
//                        .strafeTo(new Vector2d(-6, 48))
//                        .strafeTo(new Vector2d(-60.5, 64))
//                        //.splineToConstantHeading(new Vector2d(-24, 48), Math.PI)
//                        //.splineToConstantHeading(new Vector2d(-42, 28), Math.PI)
//                        .build()
//        );
//        Actions.runBlocking(claw.closeAction());
//    }
//
//    public void loop() {}
//}