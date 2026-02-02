package org.firstinspires.ftc.teamcode.auton;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Storer;

@Autonomous(name = "shoot3_rn", group = "Autonomous")
public class shoot3_rn extends OpMode {

    private Follower follower;
    private Timer pathTimer;
    private int pathState;

    Outtake outtake;
    Storer storer;
    Intake intake;

    private PathChain scorePath;
    private final Pose startPose = new Pose(122.000, 125.000, Math.toRadians(37));

    public void buildPaths() {
        scorePath = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(122.000, 125.000), new Pose(84.000, 110.000)))
                .setLinearHeadingInterpolation(Math.toRadians(37), Math.toRadians(37))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to score position
                follower.followPath(scorePath);
                setPathState(1);
                break;

            case 1: // Arrived? Shoot.
                if (!follower.isBusy()) {
                    outtake.shoot();
                    intake.run();
                    setPathState(2);
                }
                break;

            case 2: // Wait 2s for shoot, then Move Storer 1
                if (pathTimer.getElapsedTimeSeconds() > 2) {
                    storer.toOne();
                    setPathState(3); // Wait 0.5s for storer
                }
                break;

            case 3: // Wait 0.5s, then Push 1
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    intake.pushBall();
                    setPathState(4); // Wait 1s for push
                }
                break;

            case 4: // Wait 1s, then Pull 1
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    intake.pullBall();
                    setPathState(5); // Wait 0.5s for pull
                }
                break;

            case 5: // Wait 0.5s, then Move Storer 2
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    storer.toTwo();
                    setPathState(6); // Wait 0.5s for storer
                }
                break;

            case 6: // Wait 0.5s, then Push 2
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    intake.pushBall();
                    setPathState(7); // Wait 1s for push
                }
                break;

            case 7: // Wait 1s, then Pull 2
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    intake.pullBall();
                    setPathState(8); // Wait 0.5s for pull
                }
                break;

            case 8: // Wait 0.5s, then Move Storer 3
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    storer.toThree();
                    setPathState(9); // Wait 0.5s for storer
                }
                break;

            case 9: // Wait 0.5s, then Push 3
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    intake.pushBall();
                    setPathState(10); // Wait 1s for push
                }
                break;

            case 10: // Wait 1s, then Pull 3
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    intake.pullBall();
                    outtake.stopOuttake();
                    intake.stopIntake();
                    setPathState(11); // Wait 0.5s for final pull
                }
                break;

            case 11: // End Auto
                if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }

    @Override
    public void init() {
        pathTimer = new Timer();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(startPose);

        this.outtake = new Outtake(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap, telemetry);
        this.storer = new Storer(hardwareMap, telemetry);

        buildPaths();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        telemetry.addData("State", pathState);
        telemetry.addData("Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}