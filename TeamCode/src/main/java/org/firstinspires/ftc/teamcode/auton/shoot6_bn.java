package org.firstinspires.ftc.teamcode.auton;

import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.bylazar.telemetry.PanelsTelemetry;
import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Storer;

import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;

import java.util.List;

@Autonomous(name = "shoot6_bn_FIXED", group = "Autonomous")
@Configurable
public class shoot6_bn extends OpMode {
    private TelemetryManager panelsTelemetry;
    private Timer pathTimer;
    public Follower follower;
    private int pathState;
    private Paths paths;

    Outtake outtake;
    Storer storer;
    Intake intake;

    private Limelight3A limelight;
    private int detectedAprilTagId = -1;
    private boolean aprilTagRead = false;

    @Override
    public void init() {
        pathTimer = new Timer();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(26, 130, Math.toRadians(144)));

        paths = new Paths(follower);

        this.outtake = new Outtake(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap, telemetry);
        this.storer = new Storer(hardwareMap, telemetry);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);
        limelight.start();
    }

    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("Timer", pathTimer.getElapsedTimeSeconds());
        panelsTelemetry.debug("AprilTag ID", detectedAprilTagId);
        panelsTelemetry.update(telemetry);
    }

    @Override
    public void stop() {
        limelight.stop();
        super.stop();
    }

    public static class Paths {
        public PathChain Path1, Path2, Path3, line4, Path5, Path6, Path7;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(new BezierLine(new Pose(26, 130), new Pose(60, 100)))
                    .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(90)).build();

            Path2 = follower.pathBuilder().addPath(new BezierLine(new Pose(60, 100), new Pose(61, 100)))
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(144)).build();

            Path3 = follower.pathBuilder().addPath(new BezierLine(new Pose(61, 100), new Pose(39, 84)))
                    .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(180)).build();

            line4 = follower.pathBuilder().addPath(new BezierLine(new Pose(39, 84), new Pose(34, 84)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

            Path5 = follower.pathBuilder().addPath(new BezierLine(new Pose(34, 84), new Pose(29.5, 84)))
                    .setTangentHeadingInterpolation().build();

            Path6 = follower.pathBuilder().addPath(new BezierLine(new Pose(29.5, 84), new Pose(24, 84)))
                    .setTangentHeadingInterpolation().build();

            Path7 = follower.pathBuilder().addPath(new BezierLine(new Pose(24, 84), new Pose(60, 100)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(142)).build();
        }
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start Path 1
                follower.followPath(paths.Path1);
                setPathState(1);
                break;

            case 1: // Wait for Path 1, then start Path 2
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path2);
                    setPathState(2);
                }
                break;

            case 2: // Wait for Path 2, then scan AprilTag
                if (!follower.isBusy()) {
                    setPathState(3);
                }
                break;

            case 3: // Read AprilTag
                LLResult result = limelight.getLatestResult();
                boolean tagFound = false;

                if (result != null && result.isValid()) {
                    List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                    if (fiducials != null && !fiducials.isEmpty()) {
                        detectedAprilTagId = fiducials.get(0).getFiducialId();
                        tagFound = true;
                    }
                }

                // If tag found OR 2 seconds passed
                if (tagFound || pathTimer.getElapsedTimeSeconds() > 2.0) {
                    follower.followPath(paths.Path3);
                    setPathState(4);
                }
                break;

            case 4: // Drive to Shooting Position
                if (!follower.isBusy()) {
                    setPathState(5);
                }
                break;

            case 5: // Shooting Buffer
                outtake.shoot();
                if (pathTimer.getElapsedTimeSeconds() > 0.8) {
                    setPathState(6);
                }
                break;

            case 6: // Complex Sequence
                if (pathTimer.getElapsedTimeSeconds() > 2.5) {
                    outtake.stopOuttake();
                    follower.followPath(paths.line4);
                    setPathState(7);
                } else if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    // intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 1.7) {
                    // intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 1.4) {
                    storer.toThree();
                } else if (pathTimer.getElapsedTimeSeconds() > 1.1) {
                    // intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 0.8) {
                    // intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 0.5) {
                    storer.toTwo();
                } else {
                    storer.toOne();
                }
                break;

            case 7: // Driving line4
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path5);
                    setPathState(8);
                } else {
                    storer.toOne();
                    intake.run();
                }
                break;

            case 8: // Driving Path5
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path6);
                    setPathState(9);
                } else {
                    storer.toTwo();
                }
                break;

            case 9: // Driving Path6
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path7);
                    setPathState(10);
                }
                break;

            case 10: // Final check
                if (!follower.isBusy()) {
                    setPathState(-1);
                }
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }
}