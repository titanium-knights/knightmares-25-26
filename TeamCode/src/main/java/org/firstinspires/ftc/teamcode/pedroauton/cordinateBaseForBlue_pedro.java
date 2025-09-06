//package org.firstinspires.ftc.teamcode.pedroauton;
//
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.pedroPathing.follower.*;
//import org.firstinspires.ftc.teamcode.pedroPathing.localization.Pose;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierCurve;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.BezierLine;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Path;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.PathChain;
//import org.firstinspires.ftc.teamcode.pedroPathing.pathGeneration.Point;
//import org.firstinspires.ftc.teamcode.pedroPathing.util.Timer;
//// import org.firstinspires.ftc.teamcode.config.subsystem.ClawSubsystem;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
//
//@Autonomous(name = "cordinateBaseForBlue_pedro", group = "Examples")
//public class cordinateBaseForBlue_pedro extends OpMode {
//    private Follower follower;
//    private Timer pathTimer, actionTimer, opmodeTimer;
//    private int pathState;
//    public Claw claw;
//    public ClawRotator clawRot;
//
//
//    private final Pose startP_HUMAN = new Pose(8, 64, Math.toRadians(0));
//    private final Pose startP_BASKET = new Pose(8, 80, Math.toRadians(0));
//
//    //only for basket- for now
//    private final Pose scoreP = new Pose(12, 132, Math.toRadians(135));
//
//    private final Pose pickupCloseP_BASKET = new Pose(56, 120, Math.toRadians(90));
//    private final Pose pickupMiddleP_BASKET = new Pose(56, 132, Math.toRadians(90));
//    private final Pose pickupFarP_BASKET = new Pose(56, 136, Math.toRadians(90));
//
//    //the y will need a change during tuning
//    private final Pose pickupCloseP_HUMAN = new Pose(56, 24, Math.toRadians(270));
//    private final Pose pickupMiddleP_HUMAN = new Pose(56, 12, Math.toRadians(270));
//    private final Pose pickupFarP_HUMAN = new Pose(56, 8, Math.toRadians(270));
//
//    //11 cuz 3 inch for sample + 8 inch for robot
//    private final Pose placeCloseP_HUMAN = new Pose(11, 24, Math.toRadians(270));
//    private final Pose placeMiddleP_HUMAN = new Pose(11, 12, Math.toRadians(270));
//    //will also be used as park
//    private final Pose placeFarP_HUMAN = new Pose(11, 8, Math.toRadians(270));
//
//    //its hundred(angel) not by mistake, there is a chance the block would end up outside the line so angel it + same reasoning for 124 instead of 120
//    private final Pose placeCloseP_BASKET = new Pose(14, 124, Math.toRadians(100));
//    private final Pose placeMiddleP_BASKET = new Pose(16, 132, Math.toRadians(90));
//    private final Pose placeFarP_BASKET = new Pose(20, 136, Math.toRadians(90));
//
//    private final Pose placeCloseP_BASKETwCLAW = new Pose(36, 120, Math.toRadians(0));
//    private final Pose laceMiddleP_BASKETwCLAW = new Pose(36, 132, Math.toRadians(0));
//    //the other two, the robot just picks up while facing forward, bc the robot cant go out the bounds of the field
//    // it will just be turning slightly to pick up the sample, this number will probably be changed
//    private final Pose placeFarP_BASKETwCLAW = new Pose(36, 136, Math.toRadians(45));
//
//
//    //none for human since placeFarP_HUMAN is the same thing (after tuning)
//    private final Pose parkP_BASKET = new Pose(32, 8, Math.toRadians(90));
//
//    private final Pose specimenP_HUMAN = new Pose(40, 64, Math.toRadians(0));
//    private final Pose specimenControllP_HUMAN = new Pose(40, 36, Math.toRadians(0));
//    private final Pose startControllP_HUMAN = new Pose(8, 32, Math.toRadians(0));
//    private final Pose controllBeforeCloseP_HUMAN = new Pose(56, 36, Math.toRadians(0));
//
//
//    private final Pose specimenP_BASKET = new Pose(40, 80, Math.toRadians(0));
//    private final Pose specimenControllP_BASKET = new Pose(40, 112, Math.toRadians(0));
//    private final Pose startControllP_BASKET = new Pose(8, 112, Math.toRadians(0));
//    private final Pose controllBeforeCloseP_BASKET = new Pose(56, 112, Math.toRadians(0));
//
//    //only for nearHuman_park
//    private final Pose straightToParkP_HUMAN = new Pose(8, 8, Math.toRadians(0));
//    private final Pose straightToParkP_BASKET = new Pose(8, 32, Math.toRadians(0));
//
//}
