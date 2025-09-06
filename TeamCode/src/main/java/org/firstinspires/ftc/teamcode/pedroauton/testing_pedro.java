//package org.firstinspires.ftc.teamcode.pedroauton;
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
//public class testing_pedro {
//    @Autonomous(name = "pedro_example", group = "Examples")
//    public class pedro_example extends OpMode{
//        private Follower follower;
//        private Timer pathTimer, actionTimer, opmodeTimer;
//
//        /** This is the variable where we store the state of our auto.
//         * It is used by the pathUpdate method. */
//        private int pathState;
//
//        /** Create and Define Poses + Paths
//         * Poses are built with three constructors: x, y, and heading (in Radians).
//         * Pedro uses 0 - 144 for x and y, with 0, 0 being on the bottom left.
//         * (For Into the Deep, this would be Blue Observation Zone (0,0) to Red Observation Zone (144,144).)
//         * Even though Pedro uses a different coordinate system than RR, you can convert any roadrunner pose by adding +72 both the x and y.
//         * This visualizer is very easy to use to find and create paths/pathchains/poses: <https://pedro-path-generator.vercel.app/>
//         * Lets assume our robot is 18 by 18 inches
//         * Lets assume the Robot is facing the human player and we want to score in the bucket */
//
//        /** Start Pose of our robot */
//        private final Pose startPose = new Pose(24, 24, Math.toRadians(270));
//
//        /** Scoring Pose of our robot. It is facing the submersible at a -45 degree (315 degree) angle. */
//        private final Pose right = new Pose(24, 0, Math.toRadians(270));
//        private final Pose up = new Pose(48, 24, Math.toRadians(270));
//        private final Pose left = new Pose(24, 48, Math.toRadians(270));
//        private final Pose back = new Pose(0, 24, Math.toRadians(270));
//
//        /* These are our Paths and PathChains that we will define in buildPaths() */
//        // private Path scorePreload, park;
//        private Path goUp;
//        private PathChain grabPickup1, grabPickup2, grabPickup3, scorePickup1, scorePickup2, scorePickup3;
//
//        /** Build the paths for the auto (adds, for example, constant/linear headings while doing paths)
//         * It is necessary to do this so that all the paths are built before the auto starts. **/
//        public void buildPaths() {
//
//            scorePreload = new Path(new BezierLine(new Point(startPose), new Point(scorePose)));
//            scorePreload.setLinearHeadingInterpolation(startPose.getHeading(), scorePose.getHeading());
//
//            goUp = new Path(new BezierLine(new Point(startPose), new Point(up)));
//
//        /* Here is an example for Constant Interpolation
//        scorePreload.setConstantInterpolation(startPose.getHeading()); */
//
//            /* This is our grabPickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//            grabPickup1 = follower.pathBuilder()
//                    .addPath(new BezierLine(new Point(scorePose), new Point(pickup1Pose)))
//                    .setLinearHeadingInterpolation(scorePose.getHeading(), pickup1Pose.getHeading())
//                    .build();
//
//            /* This is our scorePickup1 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//            scorePickup1 = follower.pathBuilder()
//                    .addPath(new BezierLine(new Point(pickup1Pose), new Point(scorePose)))
//                    .setLinearHeadingInterpolation(pickup1Pose.getHeading(), scorePose.getHeading())
//                    .build();
//
//            /* This is our grabPickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//            grabPickup2 = follower.pathBuilder()
//                    .addPath(new BezierLine(new Point(scorePose), new Point(pickup2Pose)))
//                    .setLinearHeadingInterpolation(scorePose.getHeading(), pickup2Pose.getHeading())
//                    .build();
//
//            /* This is our scorePickup2 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//            scorePickup2 = follower.pathBuilder()
//                    .addPath(new BezierLine(new Point(pickup2Pose), new Point(scorePose)))
//                    .setLinearHeadingInterpolation(pickup2Pose.getHeading(), scorePose.getHeading())
//                    .build();
//
//            /* This is our grabPickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//            grabPickup3 = follower.pathBuilder()
//                    .addPath(new BezierLine(new Point(scorePose), new Point(pickup3Pose)))
//                    .setLinearHeadingInterpolation(scorePose.getHeading(), pickup3Pose.getHeading())
//                    .build();
//
//            /* This is our scorePickup3 PathChain. We are using a single path with a BezierLine, which is a straight line. */
//            scorePickup3 = follower.pathBuilder()
//                    .addPath(new BezierLine(new Point(pickup3Pose), new Point(scorePose)))
//                    .setLinearHeadingInterpolation(pickup3Pose.getHeading(), scorePose.getHeading())
//                    .build();
//
//            /* This is our park path. We are using a BezierCurve with 3 points, which is a curved line that is curved based off of the control point */
//            park = new Path(new BezierCurve(new Point(scorePose), /* Control Point */ new Point(parkControlPose), new Point(parkPose)));
//            park.setLinearHeadingInterpolation(scorePose.getHeading(), parkPose.getHeading());
//        }
//
//        /** This switch is called continuously and runs the pathing, at certain points, it triggers the action state.
//         * Everytime the switch changes case, it will reset the timer. (This is because of the setPathState() method)
//         * The followPath() function sets the follower to run the specific path, but does NOT wait for it to finish before moving on. */
//        public void autonomousPathUpdate() {
//            switch (pathState) {
//                case 0:
//                    follower.followPath(scorePreload);
//                    setPathState(1);
//                    break;
//                case 1:
//
//                /* You could check for
//                - Follower State: "if(!follower.isBusy() {}" (Though, I don't recommend this because it might not return due to holdEnd
//                - Time: "if(pathTimer.getElapsedTimeSeconds() > 1) {}"
//                - Robot Position: "if(follower.getPose().getX() > 36) {}"
//                */
//
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                    if(follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                        /* Score Preload */
//                        clawRot.toDrop();
//                        claw.open();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                        follower.followPath(grabPickup1,true);
//                        setPathState(2);
//                    }
//                    break;
//                case 2:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup1Pose's position */
//                    if(follower.getPose().getX() > (pickup1Pose.getX() - 1) && follower.getPose().getY() > (pickup1Pose.getY() - 1)) {
//                        /* Grab Sample */
//                        clawRot.toPick();
//                        claw.close();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                        follower.followPath(scorePickup1,true);
//                        setPathState(3);
//                    }
//                    break;
//                case 3:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                    if(follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                        /* Score Sample */
//                        clawRot.toDrop();
//                        claw.open();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                        follower.followPath(grabPickup2,true);
//                        setPathState(4);
//                    }
//                    break;
//                case 4:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup2Pose's position */
//                    if(follower.getPose().getX() > (pickup2Pose.getX() - 1) && follower.getPose().getY() > (pickup2Pose.getY() - 1)) {
//                        /* Grab Sample */
//                        clawRot.toPick();
//                        claw.close();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                        follower.followPath(scorePickup2,true);
//                        setPathState(5);
//                    }
//                    break;
//                case 5:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                    if(follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                        /* Score Sample */
//                        clawRot.toDrop();
//                        claw.open();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are grabbing the sample */
//                        follower.followPath(grabPickup3,true);
//                        setPathState(6);
//                    }
//                    break;
//                case 6:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the pickup3Pose's position */
//                    if(follower.getPose().getX() > (pickup3Pose.getX() - 1) && follower.getPose().getY() > (pickup3Pose.getY() - 1)) {
//                        /* Grab Sample */
//                        clawRot.toPick();
//                        claw.close();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are scoring the sample */
//                        follower.followPath(scorePickup3, true);
//                        setPathState(7);
//                    }
//                    break;
//                case 7:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                    if(follower.getPose().getX() > (scorePose.getX() - 1) && follower.getPose().getY() > (scorePose.getY() - 1)) {
//                        /* Score Sample */
//                        clawRot.toDrop();
//                        claw.open();
//                        /* Since this is a pathChain, we can have Pedro hold the end point while we are parked */
//                        follower.followPath(park,true);
//                        setPathState(8);
//                    }
//                    break;
//                case 8:
//                    /* This case checks the robot's position and will wait until the robot position is close (1 inch away) from the scorePose's position */
//                    if(follower.getPose().getX() > (parkPose.getX() - 1) && follower.getPose().getY() > (parkPose.getY() - 1)) {
//                        /* Put the claw in position to get a level 1 ascent */
//                        clawRot.toPick();
//                        claw.close();
//
//                        /* Set the state to a Case we won't use or define, so it just stops running an new paths */
//                        setPathState(-1);
//                    }
//                    break;
//            }
//        }
//
//        /** These change the states of the paths and actions
//         * It will also reset the timers of the individual switches **/
//        public void setPathState(int pState) {
//            pathState = pState;
//            pathTimer.resetTimer();
//        }
//
//        /** This is the main loop of the OpMode, it will run repeatedly after clicking "Play". **/
//        @Override
//        public void loop() {
//
//            // These loop the movements of the robot
//            follower.update();
//            autonomousPathUpdate();
//
//            // Feedback to Driver Hub
//            telemetry.addData("path state", pathState);
//            telemetry.addData("x", follower.getPose().getX());
//            telemetry.addData("y", follower.getPose().getY());
//            telemetry.addData("heading", follower.getPose().getHeading());
//            telemetry.update();
//        }
//
//        /** This method is called once at the init of the OpMode. **/
//        @Override
//        public void init() {
//            pathTimer = new Timer();
//            opmodeTimer = new Timer();
//
//            opmodeTimer.resetTimer();
//
//            follower = new Follower(hardwareMap);
//            follower.setStartingPose(startPose);
//
//            buildPaths();
//
//            claw = new Claw(hardwareMap, telemetry);
//            clawRot = new ClawRotator(hardwareMap, telemetry);
//
//            // Set the claw to positions for init
//            claw.close();
//            clawRot.toPick();
//        }
//
//        /** This method is called continuously after Init while waiting for "play". **/
//        @Override
//        public void init_loop() {}
//
//        /** This method is called once at the start of the OpMode.
//         * It runs all the setup actions, including building paths and starting the path system **/
//        @Override
//        public void start() {
//            opmodeTimer.resetTimer();
//            setPathState(0);
//        }
//
//        /** We do not use this because everything should automatically disable **/
//        @Override
//        public void stop() {
//        }
//}
