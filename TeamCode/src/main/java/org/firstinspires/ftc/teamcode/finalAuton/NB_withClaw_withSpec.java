package org.firstinspires.ftc.teamcode.finalAuton;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

import com.pedropathing.follower.Follower;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.Path;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.pedropathing.util.Timer;
import com.pedropathing.util.Constants;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import  com.qualcomm.robotcore.eventloop.opmode.OpMode;

// import org.firstinspires.ftc.teamcode.config.subsystem.ClawSubsystem;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;

//TODO ADD ARM STUFF


@Autonomous(name = "NB_withClaw_withSpec", group = "Examples")
public class NB_withClaw_withSpec extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;
    public Slides slides;


    private final Pose startP_BASKET = new Pose(8, 80, Math.toRadians(180));

    //only for basket- for now
    private final Pose scoreP = new Pose(12, 132, Math.toRadians(135));

    private final Pose pickupCloseP_BASKETwCLAW = new Pose(36, 120, Math.toRadians(0));
    private final Pose pickupMiddleP_BASKETwCLAW = new Pose(36, 132, Math.toRadians(0));
    private final Pose pickupFarP_BASKETwCLAW = new Pose(36, 136, Math.toRadians(45));



    private final Pose space = new Pose(32, 80, Math.toRadians(180));
    private final Pose specimenP_BASKET = new Pose(38, 80, Math.toRadians(180));
    private final Pose turn = new Pose(32, 80, Math.toRadians(0));



    private final Pose straightToParkP_BASKET = new Pose(12, 24, Math.toRadians(0));

    private Path startWithSpecimen_PATH, park;
    private PathChain hangSpecimen_PATH, specimenControllA_PATH, specimenControllB_PATH , pickUpClose_PATH, placeClose_PATH,
            pickUpMiddle_PATH, placeMiddle_PATH, pickUpFar_PATH, placeFar_PATH, spaceForth_PATH, spaceBack_PATH;


    public void buildPaths() {

        startWithSpecimen_PATH = new Path(new BezierLine(new Point(startP_BASKET), new Point(space)));
        startWithSpecimen_PATH.setLinearHeadingInterpolation(startP_BASKET.getHeading(), space.getHeading());

        spaceForth_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(space), new Point( specimenP_BASKET )))
                .setLinearHeadingInterpolation(space.getHeading(),  specimenP_BASKET .getHeading())
                .build();

        hangSpecimen_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenP_BASKET), new Point( space )))
                .setLinearHeadingInterpolation(specimenP_BASKET.getHeading(),  space .getHeading())
                .build();

        spaceBack_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(space), new Point( turn )))
                .setLinearHeadingInterpolation(space.getHeading(),  turn .getHeading())
                .build();

        specimenControllA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(turn), new Point( pickupCloseP_BASKETwCLAW )))
                .setLinearHeadingInterpolation(turn.getHeading(),  pickupCloseP_BASKETwCLAW .getHeading())
                .build();

        placeClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_BASKETwCLAW), new Point(scoreP)))
                .setLinearHeadingInterpolation(pickupCloseP_BASKETwCLAW.getHeading(), scoreP.getHeading())
                .build();

        pickUpMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoreP), new Point(pickupMiddleP_BASKETwCLAW)))
                .setLinearHeadingInterpolation(scoreP.getHeading(), pickupMiddleP_BASKETwCLAW.getHeading())
                .build();

        placeMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_BASKETwCLAW), new Point(scoreP)))
                .setLinearHeadingInterpolation(pickupMiddleP_BASKETwCLAW.getHeading(), scoreP.getHeading())
                .build();

        pickUpFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(scoreP), new Point(pickupFarP_BASKETwCLAW)))
                .setLinearHeadingInterpolation(scoreP.getHeading(), pickupFarP_BASKETwCLAW.getHeading())
                .build();

        placeFar_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupFarP_BASKETwCLAW), new Point(scoreP)))
                .setLinearHeadingInterpolation(pickupFarP_BASKETwCLAW.getHeading(), scoreP.getHeading())
                .build();

        //basically park
        park = new Path(new BezierLine(new Point(scoreP), new Point(straightToParkP_BASKET)));
        park.setLinearHeadingInterpolation(scoreP.getHeading(), straightToParkP_BASKET.getHeading());
    }


    private int notCase = 0;
    public void autonomousPathUpdate() {
        if (notCase == 0) {
            telemetry.addLine("case " + notCase);
            telemetry.update();
//            claw.close();
//            clawRot.toNeutral();
            follower.followPath(startWithSpecimen_PATH);
            notCase = 1;
        }

        if (notCase == 1) {
            telemetry.addLine("case " + notCase);
            telemetry.update();

            if((Math.abs(follower.getPose().getX() - space.getX()) < 1) && Math.abs(follower.getPose().getY() - space.getY()) < 1) {
//                clawRot.toPick();
                follower.followPath(spaceForth_PATH,true);
                notCase = 1;
            }
        }
        if (notCase == 1) {
            telemetry.addLine("case " + notCase);
            telemetry.update();

//            slides.up();
//            slides.extend_auton();
//            clawRot.toDrop();
//            slides.smallRetract_auton();
            if((Math.abs(follower.getPose().getX() - specimenP_BASKET.getX()) < 1) && Math.abs(follower.getPose().getY() - specimenP_BASKET.getY()) < 1) {
                follower.followPath(hangSpecimen_PATH,true);
                notCase = 1;
            }
        }

        if (notCase == 2) {
            telemetry.addLine("case " + notCase);
            telemetry.update();

            if((Math.abs(follower.getPose().getX() - space.getX()) < 1) && Math.abs(follower.getPose().getY() - space.getY()) < 1) {
                //slides.retract();
                //clawRot.toNeutral();
                //slides.down();
                follower.followPath(spaceBack_PATH,true);
                notCase = 2;
            }
        }

        if (notCase == 2) {
            telemetry.addLine("case " + notCase);
            telemetry.update();

            if((Math.abs(follower.getPose().getX() - turn.getX()) < 1) && Math.abs(follower.getPose().getY() - turn.getY()) < 1) {
                follower.followPath(specimenControllA_PATH,true);
                notCase = 3;
            }
        }
        if(notCase ==3){
            telemetry.addLine("case " + notCase);
            telemetry.update();

            if((Math.abs(follower.getPose().getX() - pickupCloseP_BASKETwCLAW.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupCloseP_BASKETwCLAW.getY()) < 1) {

//            claw.open();
//            clawRot.toPick();
//            claw.close();
                follower.followPath(placeClose_PATH, true);
                notCase = 4;
            }
        }
        if(notCase ==4){
            telemetry.addLine("case " + notCase);
            telemetry.update();

//            slides.up();
//            slides.extend_auton();
//            clawRot.toDrop();
//            claw.open();

            if((Math.abs(follower.getPose().getX() - scoreP.getX()) < 1) && Math.abs(follower.getPose().getY() - scoreP.getY()) < 1) {

                follower.followPath(pickUpMiddle_PATH, true);
                notCase = 5;
            }
        }
        if(notCase == 5){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            //clawRot.toPick();
//            slides.retract();
//            slides.down();
            //claw.close();

            if((Math.abs(follower.getPose().getX() - pickupMiddleP_BASKETwCLAW.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupMiddleP_BASKETwCLAW.getY()) < 1) {

                follower.followPath(placeMiddle_PATH, true);
                notCase = 6;
            }
        }
        if (notCase ==6){
            telemetry.addLine("case " + notCase);
            telemetry.update();
//
//            slides.up();
//            slides.extend_auton();
//            clawRot.toDrop();
//            claw.open();

            if((Math.abs(follower.getPose().getX() - scoreP.getX()) < 5) && Math.abs(follower.getPose().getY() - scoreP.getY()) < 5) {

                follower.followPath(pickUpFar_PATH, true);
                notCase = 7;
            }
        }
        if(notCase == 7){
            telemetry.addLine("case " + notCase);
            telemetry.update();

            //clawRot.toPick();
//            slides.retract();
//            slides.down();
            //claw.close();

            if((Math.abs(follower.getPose().getX() - pickupFarP_BASKETwCLAW.getX()) < 5) && Math.abs(follower.getPose().getY() - pickupFarP_BASKETwCLAW.getY()) < 5) {

                follower.followPath(placeFar_PATH, true);
                notCase = 8;
            }
        }
        if(notCase == 8){
            telemetry.addLine("case " + notCase);
            telemetry.update();
            //            slides.up();
//            slides.extend_auton();
//            clawRot.toDrop();
//            claw.open();
            if((Math.abs(follower.getPose().getX() - scoreP.getX()) < 5) && Math.abs(follower.getPose().getY() - scoreP.getY()) < 5) {

                follower.followPath(park, true);
                notCase = -1;
            }
        }

    }

    public void setPathState(int pState) {
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop() {

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.update();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startP_BASKET);

        buildPaths();

        claw = new Claw(hardwareMap, telemetry);
        clawRot = new ClawRotator(hardwareMap, telemetry);

        // Set the claw to positions for init
        claw.close();
        clawRot.toPick();
    }

    @Override
    public void init_loop() {}

    @Override
    public void start() {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop() {
    }
}
