package org.firstinspires.ftc.teamcode.auton;

import com.bylazar.ftcontrol.panels.plugins.html.primitives.P;
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

@Autonomous(name = "*shoot6_bn", group = "Autonomous")
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
        public PathChain Path1, Path2, Path3, line4, Path5, Path6, Path7, Path8, line9, Path10, Path11;

        public Paths(Follower follower) {
            // reading apriltag
            Path1 = follower.pathBuilder().addPath(new BezierLine(new Pose(26, 130), new Pose(57, 100)))
                    .setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(90)).build();

            // outaking
            Path2 = follower.pathBuilder().addPath(new BezierLine(new Pose(57, 100), new Pose(58, 100)))
                    .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(148)).build();

            // intaking
            Path3 = follower.pathBuilder().addPath(new BezierLine(new Pose(58, 100), new Pose(39, 84)))
                    .setLinearHeadingInterpolation(Math.toRadians(148), Math.toRadians(180)).build();

            line4 = follower.pathBuilder().addPath(new BezierLine(new Pose(39, 84), new Pose(35, 84)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

            Path5 = follower.pathBuilder().addPath(new BezierLine(new Pose(35, 84), new Pose(32, 84)))
                    .setTangentHeadingInterpolation().build();

            Path6 = follower.pathBuilder().addPath(new BezierLine(new Pose(32, 84), new Pose(24, 84)))
                    .setTangentHeadingInterpolation().build();

            // outaking
            Path7 = follower.pathBuilder().addPath(new BezierLine(new Pose(24, 84), new Pose(57, 100)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(148)).build();

            // intaking
            Path8 = follower.pathBuilder().addPath(new BezierLine(new Pose(57, 100), new Pose(39, 60)))
                    .setLinearHeadingInterpolation(Math.toRadians(148), Math.toRadians(180)).build();

            line9 = follower.pathBuilder().addPath(new BezierLine(new Pose(39, 60), new Pose(35, 60)))
                    .setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180)).build();

            Path10 = follower.pathBuilder().addPath(new BezierLine(new Pose(35, 60), new Pose(32, 60)))
                    .setTangentHeadingInterpolation().build();

            Path11 = follower.pathBuilder().addPath(new BezierLine(new Pose(32, 60), new Pose(24, 60)))
                    .setTangentHeadingInterpolation().build();
        }
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start Path 1
                follower.setMaxPower(1.0);
                follower.followPath(paths.Path1);
                setPathState(1);
                break;

            case 1: // Driving Path1, scan AprilTag while turning to 90°
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path2);
                    setPathState(2);
                } else {
                    // Scan while driving Path1
                    LLResult result = limelight.getLatestResult();
                    if (result != null && result.isValid()) {
                        List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                        if (fiducials != null && !fiducials.isEmpty()) {
                            detectedAprilTagId = fiducials.get(0).getFiducialId();
                        }
                    }
                }
                break;

            case 2: // Driving Path2 (turning back to 144°), keep scanning if not yet found
                if (!follower.isBusy()) {
                    // Path2 done, robot is at 144° — start complex sequence
                    outtake.shoot(0.65);

                    setPathState(3);
                } else {
                    // Still turning, keep trying to read tag if not found yet
                    if (detectedAprilTagId == -1) {
                        LLResult result = limelight.getLatestResult();
                        if (result != null && result.isValid()) {
                            List<LLResultTypes.FiducialResult> fiducials = result.getFiducialResults();
                            if (fiducials != null && !fiducials.isEmpty()) {
                                detectedAprilTagId = fiducials.get(0).getFiducialId();
                            }
                        }
                    }
                }
                break;

            case 3: // Shooting Buffer
                if (pathTimer.getElapsedTimeSeconds() > 3) {
                    setPathState(4);
                }
                break;

            case 4: // Complex Sequence
                if (pathTimer.getElapsedTimeSeconds() > 6.6) {
                    storer.toOne();
                    outtake.stopOuttake();
                    intake.run();
                    follower.followPath(paths.Path3);
                    setPathState(5);
                } else if (pathTimer.getElapsedTimeSeconds() > 6.2) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 5.0) {
                    intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 4.4) {
                    if (detectedAprilTagId==21) storer.toOne();
                    else if (detectedAprilTagId==22) storer.toTwo();
                    else if (detectedAprilTagId==23) storer.toThree();
                    else storer.toOne();
                } else if (pathTimer.getElapsedTimeSeconds() > 4.0) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 2.8) {
                    intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 2.2) {
                    if (detectedAprilTagId==23) storer.toOne();
                    else if (detectedAprilTagId==21) storer.toTwo();
                    else if (detectedAprilTagId==22) storer.toThree();
                    else storer.toTwo();
                } else if (pathTimer.getElapsedTimeSeconds() > 1.8) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 0.6) {
                    intake.pushBall();
                } else {
                    if (detectedAprilTagId==22) storer.toOne();
                    else if (detectedAprilTagId==23) storer.toTwo();
                    else if (detectedAprilTagId==21) storer.toThree();
                    else storer.toThree();
                }
                break;

            case 5:
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.35);
                    follower.followPath(paths.line4);
                    setPathState(6);
                }
                break;

            case 6: // Driving line4
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path5);
                    setPathState(7);
                } else {
                    intake.pushBall();
                    intake.pullBall();
                }
                break;

            case 7: // Driving Path5
                if (!follower.isBusy()) {
                    if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                        follower.followPath(paths.Path6);
                        setPathState(8);
                    }
                } else {
                    intake.pushBall();
                    intake.pullBall();
                    storer.toTwo();
                }
                break;

            case 8: // Driving Path6
                if (!follower.isBusy()) {
                    if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                        follower.setMaxPower(1.0);
                        intake.stopIntake();
                        outtake.shoot(0.65);
                        follower.followPath(paths.Path7);
                        setPathState(9);
                    }
                } else {
                    intake.pushBall();
                    intake.pullBall();
                    storer.toThree();
                }
                break;

            case 9: // Shooting Buffer
                if (pathTimer.getElapsedTimeSeconds() > 3) {
                    setPathState(10);
                }
                break;

            case 10: // Complex Sequence
                if (pathTimer.getElapsedTimeSeconds() > 6.8) {
                    storer.toOne();
                    outtake.stopOuttake();
                    intake.run();
                    follower.followPath(paths.Path8);
                    setPathState(11);
                } else if (pathTimer.getElapsedTimeSeconds() > 6.4) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 5.2) {
                    intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 4.6) {
                    if (detectedAprilTagId==21) storer.toOne();
                    else if (detectedAprilTagId==22) storer.toTwo();
                    else if (detectedAprilTagId==23) storer.toThree();
                    else storer.toOne();
                } else if (pathTimer.getElapsedTimeSeconds() > 4.2) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 2.8) {
                    intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 2.2) {
                    if (detectedAprilTagId==23) storer.toOne();
                    else if (detectedAprilTagId==21) storer.toTwo();
                    else if (detectedAprilTagId==22) storer.toThree();
                    else storer.toTwo();
                } else if (pathTimer.getElapsedTimeSeconds() > 1.8) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 0.6) {
                    intake.pushBall();
                } else {
                    if (detectedAprilTagId==22) storer.toOne();
                    else if (detectedAprilTagId==23) storer.toTwo();
                    else if (detectedAprilTagId==21) storer.toThree();
                    else storer.toThree();
                }
                break;

            case 11:
                if (!follower.isBusy()) {
                    follower.setMaxPower(0.4);
                    follower.followPath(paths.line9);
                    setPathState(12);
                }
                break;

            case 12: // Driving line4
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path10);
                    setPathState(13);
                } else {
                    intake.pushBall();
                    intake.pullBall();
                }
                break;

            case 13: // Driving Path5
                if (!follower.isBusy()) {
                    if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                        follower.followPath(paths.Path11);
                        setPathState(14);
                    }
                } else {
                    intake.pushBall();
                    intake.pullBall();
                    storer.toTwo();
                }
                break;

            case 14: // Driving Path6
                if (!follower.isBusy()) {
                    if (pathTimer.getElapsedTimeSeconds() > 1.5) {
                        follower.setMaxPower(1.0);
                        intake.stopIntake();
                        // outtake.shoot(0.6);
                        // follower.followPath(paths.line12);
                        setPathState(15);
                    }
                } else {
                    intake.pushBall();
                    intake.pullBall();
                    storer.toThree();
                }
                break;

            case 15: // Final check
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