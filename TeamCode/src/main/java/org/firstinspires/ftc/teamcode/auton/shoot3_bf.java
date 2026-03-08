package org.firstinspires.ftc.teamcode.auton;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.paths.PathChain;
import com.pedropathing.util.Timer;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Storer;

import java.util.List;

@Autonomous(name = "*shoot3_bf", group = "Autonomous")
public class shoot3_bf extends OpMode {

    private TelemetryManager panelsTelemetry;
    private Timer pathTimer;
    public Follower follower;
    private int pathState;
    Outtake outtake;
    Storer storer;
    Intake intake;

    private Limelight3A limelight;
    private int detectedAprilTagId = -1;
    private boolean aprilTagRead = false;
    private PathChain path1;
    private PathChain path2;
    private final Pose startPose = new Pose(60.000, 8.000, Math.toRadians(90));

    public void buildPaths() {
        path1 = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(60.000, 8.000), new Pose(60.000, 20.000)))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(90))
                .build();
        path2 = follower.pathBuilder()
                .addPath(new BezierLine(new Pose(60.000, 20.000), new Pose(60.000, 100.000)))
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(147))
                .build();
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Drive to score position
                follower.followPath(path1);
                setPathState(1);
                break;

            case 1: // Driving Path1, scan AprilTag while turning to 90°
                if (!follower.isBusy()) {
                    follower.followPath(path2);
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

            case 2: // Complex Sequence
                if (pathTimer.getElapsedTimeSeconds() > 6.0) {
                    storer.toOne();
                    outtake.stopOuttake();
                    // intake.run();
                    // follower.followPath(paths.Path3);
                    setPathState(3);
                } else if (pathTimer.getElapsedTimeSeconds() > 5.6) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 4.4) {
                    intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 4.0) {
                    if (detectedAprilTagId==21) storer.toOne();
                    else if (detectedAprilTagId==22) storer.toTwo();
                    else if (detectedAprilTagId==23) storer.toThree();
                    else storer.toOne();
                } else if (pathTimer.getElapsedTimeSeconds() > 3.6) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 2.4) {
                    intake.pushBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 2.0) {
                    if (detectedAprilTagId==23) storer.toOne();
                    else if (detectedAprilTagId==21) storer.toTwo();
                    else if (detectedAprilTagId==22) storer.toThree();
                    else storer.toTwo();
                } else if (pathTimer.getElapsedTimeSeconds() > 1.6) {
                    intake.pullBall();
                } else if (pathTimer.getElapsedTimeSeconds() > 0.4) {
                    intake.pushBall();
                } else {
                    if (detectedAprilTagId==22) storer.toOne();
                    else if (detectedAprilTagId==23) storer.toTwo();
                    else if (detectedAprilTagId==21) storer.toThree();
                    else storer.toThree();
                }
                break;

            case 3: // Final check
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

    @Override
    public void init() {
        pathTimer = new Timer();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(60, 8, Math.toRadians(90)));

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

        telemetry.addData("State", pathState);
        telemetry.addData("Timer", pathTimer.getElapsedTimeSeconds());
        telemetry.update();
    }
}