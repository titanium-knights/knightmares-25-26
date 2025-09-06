package org.firstinspires.ftc.teamcode.pedroauton;


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
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.backupAuton.auton.AutonMethods;




@Autonomous(name = "testPickUp", group = "Examples")
public class testPickUp extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;


    private final Pose pickUpSpecimen = new Pose(22, 42, Math.toRadians(270));

    private final Pose START = new Pose(8, 64, Math.toRadians(270));
    private final Pose TEST_RIGHT  = new Pose(72, 82, Math.toRadians(0));
    private final Pose TEST_LEFT = new Pose(72, 62, Math.toRadians(0));
    private final Pose TEST_UP= new Pose(82, 72, Math.toRadians(0));
    private final Pose TEST_DOWN = new Pose(62, 72, Math.toRadians(0));


    private PathChain UP, DOWN, LEFT, RIGHT, Yummy;

    public void buildPaths() {


        Yummy = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(START.getHeading(), pickUpSpecimen.getHeading())
                .build();

        UP = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_UP)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_UP.getHeading())
                .build();

        DOWN = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_DOWN)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_DOWN.getHeading())
                .build();

        LEFT = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_LEFT)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_LEFT.getHeading())
                .build();

        RIGHT = follower.pathBuilder()
                .addPath(new BezierLine(new Point(START), new Point(TEST_RIGHT)))
                .setLinearHeadingInterpolation(START.getHeading(), TEST_RIGHT.getHeading())
                .build();

    }
    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(Yummy);
                claw.open();
                clawRot.toPick();
                claw.close();

                setPathState(-1);
                break;
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
        follower.setStartingPose(START);

        buildPaths();

        claw = new Claw(hardwareMap, telemetry);
        clawRot = new ClawRotator(hardwareMap, telemetry);


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
