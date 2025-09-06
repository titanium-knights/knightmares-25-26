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
//// import org.firstinspires.ftc.teamcode.config.subsystem.ClawSubsystem;
//import org.firstinspires.ftc.robotcore.external.Telemetry;
//import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
//import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
//import org.firstinspires.ftc.teamcode.utilities.Claw;
//import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
//import org.firstinspires.ftc.teamcode.utilities.Slides;
//
//
//@Autonomous(name = "nearHuman_pushBot_NoSpecimen_pedro", group = "Examples")
//public class nearHuman_pushBot_NoSpecimen_pedro extends OpMode{
//    private Follower follower;
//    private Timer pathTimer, actionTimer, opmodeTimer;
//    private int pathState;
//    public Claw claw;
//    public ClawRotator clawRot;
//    public Slides slides;
//
//    private final Pose startP_HUMAN = new Pose(10, 64, Math.toRadians(0));
//
//    //the y will need a change during tuning
//    private final Pose pickupCloseP_HUMAN = new Pose(60, 24, Math.toRadians(270));
//    private final Pose pickupMiddleP_HUMAN = new Pose(60, 18, Math.toRadians(270));
//    private final Pose pickupFarP_HUMAN = new Pose(60, 8, Math.toRadians(270));
//
//    //11 cuz 3 inch for sample + 8 inch for robot
//    private final Pose placeCloseP_HUMAN = new Pose(24, 24, Math.toRadians(270));
//    private final Pose placeMiddleP_HUMAN = new Pose(24, 18, Math.toRadians(270));
//    //will also be used as park
//    private final Pose placeFarP_HUMAN = new Pose(24, 8, Math.toRadians(270));
//
//    //its hundred(angel) not by mistake, there is a chance the block would end up outside the line so angel it + same reasoning for 124 instead of 120
//    private final Pose startControllP_HUMAN = new Pose(10, 36, Math.toRadians(0));
//    private final Pose controllBeforeCloseP_HUMAN = new Pose(60, 36, Math.toRadians(0));
//
//    private PathChain startControll_PATH, pickUpClose_PATH, placeClose_PATH, pickUpFar_PATH,
//            moveToFar_PATH, placeMiddle_PATH, moveToMiddle_PATH, pickUpMiddle_PATH, start_PATH, placeFar_PATH;
//
//
//    public void buildPaths() {
//
//        start_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(startP_HUMAN), new Point(startControllP_HUMAN)))
//                .setLinearHeadingInterpolation(startP_HUMAN.getHeading(), startControllP_HUMAN.getHeading())
//                .build();
//
//        startControll_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(startControllP_HUMAN), new Point( controllBeforeCloseP_HUMAN )))
//                .setLinearHeadingInterpolation(startControllP_HUMAN.getHeading(),  controllBeforeCloseP_HUMAN .getHeading())
//                .build();
//
//        pickUpClose_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(controllBeforeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
//                .setLinearHeadingInterpolation(controllBeforeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
//                .build();
//
//        placeClose_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(placeCloseP_HUMAN)))
//                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), placeCloseP_HUMAN.getHeading())
//                .build();
//
//        moveToMiddle_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(placeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
//                .setLinearHeadingInterpolation(placeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
//                .build();
//
//        pickUpMiddle_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(pickupMiddleP_HUMAN)))
//                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
//                .build();
//
//        placeMiddle_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(placeMiddleP_HUMAN)))
//                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), placeMiddleP_HUMAN.getHeading())
//                .build();
//
//        moveToFar_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(placeMiddleP_HUMAN), new Point(pickupMiddleP_HUMAN)))
//                .setLinearHeadingInterpolation(placeMiddleP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
//                .build();
//
//        pickUpFar_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(pickupFarP_HUMAN)))
//                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), pickupFarP_HUMAN.getHeading())
//                .build();
//
//        //basically park
//        placeFar_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pickupFarP_HUMAN), new Point(placeFarP_HUMAN)))
//                .setLinearHeadingInterpolation(pickupFarP_HUMAN.getHeading(), placeFarP_HUMAN.getHeading())
//                .build();
//    }
//
//
//    public void autonomousPathUpdate() {
//        switch (pathState) {
//            case 0:
//                telemetry.addLine("case 0");
//                telemetry.update();
//                follower.followPath(start_PATH,0.6, true);
//                setPathState(1);
//                break;
//            case 1:
//                telemetry.addLine("case 1");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - startControllP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - startControllP_HUMAN.getY()) < 1) {
//                    follower.followPath(startControll_PATH, 0.6, true);
//                    setPathState(2);
//                }
//                break;
//            case 2:
//                telemetry.addLine("case 2");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - controllBeforeCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - controllBeforeCloseP_HUMAN.getY()) < 1) {
//                    follower.followPath(pickUpClose_PATH, 0.6, true);
//                    setPathState(3);
//                }
//                break;
//            case 3:
//                telemetry.addLine("case 3");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - pickupCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupCloseP_HUMAN.getY()) < 1) {
//                    follower.followPath(placeClose_PATH, 0.6, true);
//                    setPathState(4);
//                }
//                break;
//            case 4:
//                telemetry.addLine("case 4");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - placeCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - placeCloseP_HUMAN.getY()) < 1) {
//                    follower.followPath(moveToMiddle_PATH,0.6, true);
//                    setPathState(5);
//                }
//                follower.followPath(moveToMiddle_PATH,true);
//                setPathState(5);
//                break;
//            case 5:
//                telemetry.addLine("case 5");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - pickupCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupCloseP_HUMAN.getY()) < 1) {
//                    follower.followPath(pickUpMiddle_PATH,0.6, true);
//                    setPathState(6);
//                }
//                break;
//            case 6:
//                telemetry.addLine("case 6");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - pickupMiddleP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupMiddleP_HUMAN.getY()) < 1) {
//                    follower.followPath(placeMiddle_PATH, 0.6, true);
//                    setPathState(7);
//                }
//                break;
//            case 7:
//                telemetry.addLine("case 7");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - placeMiddleP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - placeMiddleP_HUMAN.getY()) < 1) {
//                    follower.followPath(moveToFar_PATH, 0.6, true);
//                    setPathState(8);
//                }
//                break;
//            case 8:
//                telemetry.addLine("case 8");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - pickupMiddleP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupMiddleP_HUMAN.getY()) < 1) {
//                    follower.followPath(pickUpFar_PATH, 0.6, true);
//                    setPathState(9);
//                }
//                break;
//            case 9:
//                telemetry.addLine("case 9");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - pickupFarP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupFarP_HUMAN.getY()) < 1) {
//                    follower.followPath(placeFar_PATH, 0.6, true);
//                    setPathState(10);
//                }
//                break;
//            case 10:
//                telemetry.addLine("case 10");
//                telemetry.update();
//                if((Math.abs(follower.getPose().getX() - placeFarP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - placeFarP_HUMAN.getY()) < 1) {
//                    setPathState(-1);
//                }
//                break;
//        }
//    }
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
//        follower.update();        telemetry.addData("path state", pathState);
//
//        autonomousPathUpdate();
//
//
//        // Feedback to Driver Hub
//        telemetry.addData("x", follower.getPose().getX());
//        telemetry.addData("y", follower.getPose().getY());
//        telemetry.addData("heading", follower.getPose().getHeading());
//
//        follower.telemetryDebug(telemetry);
//
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
//        follower.setStartingPose(startP_HUMAN);
//
//        buildPaths();
//
////        claw = new Claw(hardwareMap, telemetry);
////        clawRot = new ClawRotator(hardwareMap, telemetry);
////
////        // Set the claw to positions for init
////        claw.close();
////        clawRot.toPick();
//    }
//
//    @Override
//    public void init_loop() {}
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
