//package org.firstinspires.ftc.teamcode.pedroauton;
//
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import com.pedropathing.follower.Follower;
//import com.pedropathing.localization.Pose;
//import com.pedropathing.pathgen.BezierCurve;
//import com.pedropathing.pathgen.BezierLine;
//import com.pedropathing.pathgen.Path;
//import com.pedropathing.pathgen.PathChain;
//import com.pedropathing.pathgen.Point;
//import com.pedropathing.util.Timer;
//import com.pedropathing.util.Constants;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//import  com.qualcomm.robotcore.eventloop.opmode.OpMode;
//
//import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
//import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
//import org.firstinspires.ftc.teamcode.utilities.Slides;
//
//
//
//@Autonomous(name = "nearBasket_park_pedro", group = "Examples")
//public class nearBasket_park_pedro extends OpMode{
//    private Follower follower;
//    private Timer pathTimer, actionTimer, opmodeTimer;
//    private int pathState;
//    public Claw claw;
//    public ClawRotator clawRot;
//    public Slides slides;
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
//
//    private Path straightPark_PATH;
//
//    public void buildPaths() {
//
//        straightPark_PATH = new Path(new BezierLine(new Point(startP_BASKET), new Point(straightToParkP_BASKET)));
//        straightPark_PATH.setLinearHeadingInterpolation(startP_BASKET.getHeading(), straightToParkP_BASKET.getHeading());
//
//    }
//    public void autonomousPathUpdate() {
//        switch (pathState) {
//            case 0:
//                follower.followPath(straightPark_PATH);
//                setPathState(-1);
//                break;
//        }
//    }
//
//
//    public void setPathState(int pState) {
//        pathState = pState;
//        pathTimer.resetTimer();
//    }
//
//    @Override
//    public void loop() {
//
//        // These loop the movements of the robot
//        follower.update();
//        autonomousPathUpdate();
//
//        // Feedback to Driver Hub
//        telemetry.addData("path state", pathState);
//        telemetry.addData("x", follower.getPose().getX());
//        telemetry.addData("y", follower.getPose().getY());
//        telemetry.addData("heading", follower.getPose().getHeading());
//        telemetry.update();
//    }
//
//    @Override
//    public void init() {
//        pathTimer = new Timer();
//        Constants.setConstants(FConstants.class, LConstants.class);
//        opmodeTimer = new Timer();
//
//        opmodeTimer.resetTimer();
//
//        follower = new Follower(hardwareMap);
//        follower.setStartingPose(startP_BASKET);
//
//        buildPaths();
//
//        claw = new Claw(hardwareMap, telemetry);
//        clawRot = new ClawRotator(hardwareMap, telemetry);
//
//        // Set the claw to positions for init
//        claw.close();
//        clawRot.toPick();
//    }
//
//    @Override
//    public void init_loop() {}
//
//
//    @Override
//    public void start() {
//        opmodeTimer.resetTimer();
//        setPathState(0);
//    }
//
//    @Override
//    public void stop() {
//    }
//}
