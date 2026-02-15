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

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.follower.Follower;
import com.pedropathing.paths.PathChain;
import com.pedropathing.geometry.Pose;

@Autonomous(name = "shoot6_bn", group = "Autonomous")
@Configurable
public class shoot6_bn extends OpMode{
    private TelemetryManager panelsTelemetry; // Panels Telemetry instance
    private Timer pathTimer;
    public Follower follower; // Pedro Pathing follower instance
    private int pathState; // Current autonomous path state (state machine)
    private Paths paths; // Paths defined in the Paths class

    Outtake outtake;
    Storer storer;
    Intake intake;

    @Override
    public void init() {
        pathTimer = new Timer();
        panelsTelemetry = PanelsTelemetry.INSTANCE.getTelemetry();

        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(72, 8, Math.toRadians(90)));

        paths = new Paths(follower); // Build paths

        panelsTelemetry.debug("Status", "Initialized");
        panelsTelemetry.update(telemetry);

        this.outtake = new Outtake(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap, telemetry);
        this.storer = new Storer(hardwareMap, telemetry);
    }

    @Override
    public void loop() {
        follower.update(); // Update Pedro Pathing
        autonomousPathUpdate(); // Update autonomous state machine

        // Log values to Panels and Driver Station
        panelsTelemetry.debug("Path State", pathState);
        panelsTelemetry.debug("X", follower.getPose().getX());
        panelsTelemetry.debug("Y", follower.getPose().getY());
        panelsTelemetry.debug("Heading", follower.getPose().getHeading());
        panelsTelemetry.update(telemetry);
    }


    public static class Paths {
        public PathChain Path1;
        public PathChain Path3;
        public PathChain Path4;
        public PathChain Path5;
        public PathChain line5;

        public Paths(Follower follower) {
            Path1 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(26.000, 130.000),

                                    new Pose(50.000, 110.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(144), Math.toRadians(148))

                    .build();

            Path3 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(50.000, 110.000),

                                    new Pose(50.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(148), Math.toRadians(180))

                    .build();

            Path4 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(50.000, 84.000),

                                    new Pose(25.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            Path5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(25.000, 84.000),

                                    new Pose(50.000, 84.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(180))

                    .build();

            line5 = follower.pathBuilder().addPath(
                            new BezierLine(
                                    new Pose(50.000, 84.000),

                                    new Pose(50.000, 110.000)
                            )
                    ).setLinearHeadingInterpolation(Math.toRadians(180), Math.toRadians(144))

                    .build();
        }
    }

    public void autonomousPathUpdate() {
        switch (pathState) {
            case 0: // Start Path 1
                follower.followPath(paths.Path1);
                setPathState(1);
                break;

            case 1: // Wait for Path 1, then start Path 3
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path3);
                    setPathState(2);
                }
                break;

            case 2: // Wait for Path 3, then start Path 4
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path4);
                    setPathState(3);
                }
                break;

            case 3: // Wait for Path 4, then start Path 5
                if (!follower.isBusy()) {
                    follower.followPath(paths.Path5);
                    setPathState(4);
                }
                break;

            case 4: // Wait for Path 5, then start line5
                if (!follower.isBusy()) {
                    follower.followPath(paths.line5);
                    setPathState(5);
                }
                break;

            case 5: // Final check
                if (!follower.isBusy()) {
                    setPathState(-1); // Autonomous Complete
                }
                break;
        }
    }

    public void setPathState(int state) {
        pathState = state;
        pathTimer.resetTimer();
    }
}
