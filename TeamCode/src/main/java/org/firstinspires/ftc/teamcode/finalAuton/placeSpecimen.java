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
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;
import org.firstinspires.ftc.teamcode.utilities.Claw;
import org.firstinspires.ftc.teamcode.utilities.ClawRotator;
import org.firstinspires.ftc.teamcode.utilities.Slides;
import org.firstinspires.ftc.teamcode.backupAuton.auton.AutonMethods;


@Autonomous(name = "placeSpecimen", group = "Examples")
public class placeSpecimen extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;
    public Slides slides;
    public AutonMethods autonMethods;

    //todo change the place specimen numbers by tuning

    public void buildPaths() {

    }

    public void autonomousPathUpdate() {
         slides.up();
         slides.extendForTime(1.2);
         clawRot.toDrop();
         slides.retractForTime(0.2);
    }

    public void setPathState ( int pState){
        pathState = pState;
        pathTimer.resetTimer();
    }

    @Override
    public void loop () {

        // These loop the movements of the robot
        follower.update();
        autonomousPathUpdate();


        telemetry.addData("slides", slides.getEncoder());

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());

        telemetry.update();
    }

    @Override
    public void init () {

        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(new Pose(0, 0, Math.toRadians(0)));

        buildPaths();

        claw = new Claw(hardwareMap, telemetry);
        clawRot = new ClawRotator(hardwareMap, telemetry);
        slides = new Slides(hardwareMap, telemetry);

        claw.close();
    }

    @Override
    public void init_loop () {
    }

    @Override
    public void start () {
        opmodeTimer.resetTimer();
        setPathState(0);
    }

    @Override
    public void stop () {
    }
}
