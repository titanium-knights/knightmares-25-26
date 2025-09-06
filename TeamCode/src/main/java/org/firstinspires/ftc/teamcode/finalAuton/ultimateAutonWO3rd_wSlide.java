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


@Autonomous(name = "ultimateAutonWO3rd_wSlide", group = "Examples")
public class ultimateAutonWO3rd_wSlide extends OpMode{
    private Follower follower;
    private Timer pathTimer, actionTimer, opmodeTimer;
    private int pathState;
    public Claw claw;
    public ClawRotator clawRot;
    public Slides slides;
    public AutonMethods autonMethods;

    //todo change the place specimen numbers by tuning
    private final Pose pickUpSpecimen = new Pose(18, 40, Math.toRadians(270));
    private final Pose spaceA = new Pose(30, 80, Math.toRadians(180));
    private final Pose placeSpecimenA = new Pose(40, 80, Math.toRadians(180));
    private final Pose spaceB = new Pose(30, 75, Math.toRadians(180));
    private final Pose placeSpecimenB = new Pose(40, 75, Math.toRadians(180));
    private final Pose spaceC = new Pose(30, 70, Math.toRadians(180));
    private final Pose placeSpecimenC = new Pose(40, 70, Math.toRadians(180));
    private final Pose spaceD = new Pose(30, 65, Math.toRadians(180));
    private final Pose placeSpecimenD = new Pose(40, 65, Math.toRadians(180));
    private final Pose spaceE = new Pose(30, 60, Math.toRadians(180));
    private final Pose placeSpecimenE = new Pose(40, 60, Math.toRadians(180));
    private final Pose specimenP_HUMAN = new Pose(38, 64, Math.toRadians(180));
    private final Pose spaceF = new Pose(32, 64, Math.toRadians(180));
    private final Pose turn = new Pose(20, 58, Math.toRadians(180));
    private final Pose turn2 = new Pose(30, 58, Math.toRadians(180));



    private final Pose startP_HUMAN = new Pose(8, 64, Math.toRadians(180));

    private final Pose pickupCloseP_HUMAN = new Pose(62, 26, Math.toRadians(270));
    private final Pose pickupMiddleP_HUMAN = new Pose(62, 20, Math.toRadians(270));
    private final Pose pickupFarP_HUMAN = new Pose(62, 10, Math.toRadians(270));

    //11 cuz 3 inch for sample + 8 inch for robot
    private final Pose placeCloseP_HUMAN = new Pose(24, 26, Math.toRadians(270));
    private final Pose placeMiddleP_HUMAN = new Pose(24, 20, Math.toRadians(270));
    //will also be used as park
    private final Pose placeFarP_HUMAN = new Pose(18, 10, Math.toRadians(270));

    //none for human since placeFarP_HUMAN is the same thing (after tuning)

    private final Pose specimenControllP_HUMAN = new Pose(32, 35, Math.toRadians(180));
    private final Pose controllBeforeCloseP_HUMAN = new Pose(62, 35, Math.toRadians(180));
    private final Pose specimenTurn = new Pose(62, 35, Math.toRadians(270));

    //to not nock off the samples we just put into human zone
    private final Pose X = new Pose(35, 20, Math.toRadians(270));
    private final Pose XYbetween = new Pose(35, 40, Math.toRadians(270));


    private PathChain pickUpClose_PATH, placeClose_PATH,
            pickUpFar_PATH,specimenControllB_PATH,
            moveToFar_PATH, placeMiddle_PATH,
            moveToMiddle_PATH, pickUpMiddle_PATH,
            specimenControllA_PATH, placeFar_PATH,
            pickUpSpecimenA_PATH, placeSpecimenA_PATH,
            pickUpSpecimenB_PATH, placeSpecimenB_PATH,
            pickUpSpecimenC_PATH, placeSpecimenC_PATH,
            pickUpSpecimenD_PATH, placeSpecimenD_PATH,
            pickUpSpecimenE_PATH, placeSpecimenE_PATH,
            specimenSpaceFIRST, specimenSpaceCompleteFIRST,
            concequenceOfMyNaming,specimenSpaceE,
            specimenSpaceCompleteD, specimenSpaceD,
            specimenSpaceCompleteC, specimenSpaceC,
            specimenSpaceCompleteB, specimenSpaceB, turn_PATH,
            specimenSpaceCompleteA,specimenSpaceA, turn2_PATH,
            turn_specimen, FarTOx_PATH, xTOy_PATH, XYbetween_PATH;

    private Path startWithSpecimen_PATH, park;
    public void buildPaths() {

        turn_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceF), new Point( turn )))
                .setLinearHeadingInterpolation(spaceF.getHeading(),  turn .getHeading())
                .build();

        turn2_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(turn), new Point( turn2 )))
                .setLinearHeadingInterpolation(turn.getHeading(),  turn2 .getHeading())
                .build();



        startWithSpecimen_PATH = new Path(new BezierLine(new Point(startP_HUMAN), new Point(spaceF)));
        startWithSpecimen_PATH.setLinearHeadingInterpolation(startP_HUMAN.getHeading(), spaceF.getHeading());

        specimenSpaceFIRST = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceF), new Point( specimenP_HUMAN )))
                .setLinearHeadingInterpolation(spaceF.getHeading(),  specimenP_HUMAN .getHeading())
                .build();

        specimenControllA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenP_HUMAN), new Point( spaceF )))
                .setLinearHeadingInterpolation(specimenP_HUMAN.getHeading(),  spaceF .getHeading())
                .build();

        specimenSpaceCompleteFIRST = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceF), new Point( specimenControllP_HUMAN )))
                .setLinearHeadingInterpolation(spaceF.getHeading(),  specimenControllP_HUMAN .getHeading())
                .build();


//        specimenControllB_PATH = new Path(new BezierLine(new Point(specimenControllP_HUMAN), new Point(controllBeforeCloseP_HUMAN)));
//        specimenControllB_PATH.setLinearHeadingInterpolation(specimenControllP_HUMAN.getHeading(), controllBeforeCloseP_HUMAN.getHeading());

        specimenControllB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenControllP_HUMAN), new Point( controllBeforeCloseP_HUMAN )))
                .setLinearHeadingInterpolation(specimenControllP_HUMAN.getHeading(),  controllBeforeCloseP_HUMAN .getHeading())
                .build();

        turn_specimen = follower.pathBuilder()
                .addPath(new BezierLine(new Point(controllBeforeCloseP_HUMAN), new Point( specimenTurn )))
                .setLinearHeadingInterpolation(controllBeforeCloseP_HUMAN.getHeading(),  specimenTurn .getHeading())
                .build();

        pickUpClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(specimenTurn), new Point(pickupCloseP_HUMAN)))
                .setLinearHeadingInterpolation(specimenTurn.getHeading(), pickupCloseP_HUMAN.getHeading())
                .build();

        placeClose_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(placeCloseP_HUMAN)))
                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), placeCloseP_HUMAN.getHeading())
                .build();

        moveToMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeCloseP_HUMAN), new Point(pickupCloseP_HUMAN)))
                .setLinearHeadingInterpolation(placeCloseP_HUMAN.getHeading(), pickupCloseP_HUMAN.getHeading())
                .build();

        pickUpMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupCloseP_HUMAN), new Point(pickupMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(pickupCloseP_HUMAN.getHeading(), pickupMiddleP_HUMAN.getHeading())
                .build();

        placeMiddle_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickupMiddleP_HUMAN), new Point(placeMiddleP_HUMAN)))
                .setLinearHeadingInterpolation(pickupMiddleP_HUMAN.getHeading(), placeMiddleP_HUMAN.getHeading())
                .build();

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
//        placeFar_PATH = follower.pathBuilder()
//                .addPath(new BezierLine(new Point(pickupFarP_HUMAN), new Point(placeFarP_HUMAN)))
//                .setLinearHeadingInterpolation(pickupFarP_HUMAN.getHeading(), placeFarP_HUMAN.getHeading())
//                .build();

        FarTOx_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeMiddleP_HUMAN), new Point(X)))
                .setLinearHeadingInterpolation(placeMiddleP_HUMAN.getHeading(), X.getHeading())
                .build();

        xTOy_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(X), new Point(XYbetween)))
                .setLinearHeadingInterpolation(X.getHeading(), XYbetween.getHeading())
                .build();


        pickUpSpecimenA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(XYbetween), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(XYbetween.getHeading(), pickUpSpecimen.getHeading())
                .build();


        placeSpecimenA_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(spaceA)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), spaceA.getHeading())
                .build();

        specimenSpaceA= follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceA), new Point(placeSpecimenA)))
                .setLinearHeadingInterpolation(spaceA.getHeading(), placeSpecimenA.getHeading())
                .build();

        pickUpSpecimenB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenA), new Point(spaceA)))
                .setLinearHeadingInterpolation(placeSpecimenA.getHeading(), spaceA.getHeading())
                .build();

        specimenSpaceCompleteA = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceA), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(spaceA.getHeading(), pickUpSpecimen.getHeading())
                .build();


        placeSpecimenB_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(spaceB)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), spaceB.getHeading())
                .build();

        specimenSpaceB= follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceB), new Point(placeSpecimenB)))
                .setLinearHeadingInterpolation(spaceB.getHeading(), placeSpecimenB.getHeading())
                .build();

        pickUpSpecimenC_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenB), new Point(spaceB)))
                .setLinearHeadingInterpolation(placeSpecimenB.getHeading(), spaceB.getHeading())
                .build();

        specimenSpaceCompleteB = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceB), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(spaceB.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenC_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(spaceC)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), spaceC.getHeading())
                .build();

        specimenSpaceC= follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceC), new Point(placeSpecimenC)))
                .setLinearHeadingInterpolation(spaceC.getHeading(), placeSpecimenC.getHeading())
                .build();

        pickUpSpecimenD_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenC), new Point(spaceC)))
                .setLinearHeadingInterpolation(placeSpecimenC.getHeading(), spaceC.getHeading())
                .build();

        specimenSpaceCompleteC = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceC), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(spaceC.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenD_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(spaceD)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), spaceD.getHeading())
                .build();

        specimenSpaceD= follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceD), new Point(placeSpecimenD)))
                .setLinearHeadingInterpolation(spaceD.getHeading(), placeSpecimenD.getHeading())
                .build();

        pickUpSpecimenE_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenD), new Point(spaceD)))
                .setLinearHeadingInterpolation(placeSpecimenD.getHeading(), spaceD.getHeading())
                .build();

        specimenSpaceCompleteD = follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceD), new Point(pickUpSpecimen)))
                .setLinearHeadingInterpolation(spaceD.getHeading(), pickUpSpecimen.getHeading())
                .build();

        placeSpecimenE_PATH = follower.pathBuilder()
                .addPath(new BezierLine(new Point(pickUpSpecimen), new Point(spaceE)))
                .setLinearHeadingInterpolation(pickUpSpecimen.getHeading(), spaceE.getHeading())
                .build();

        specimenSpaceE= follower.pathBuilder()
                .addPath(new BezierLine(new Point(spaceE), new Point(placeSpecimenE)))
                .setLinearHeadingInterpolation(spaceE.getHeading(), placeSpecimenE.getHeading())
                .build();

        concequenceOfMyNaming = follower.pathBuilder()
                .addPath(new BezierLine(new Point(placeSpecimenE), new Point(spaceE)))
                .setLinearHeadingInterpolation(placeSpecimenE.getHeading(), spaceE.getHeading())
                .build();

        park = new Path(new BezierLine(new Point(spaceE), new Point(placeFarP_HUMAN)));
        park.setLinearHeadingInterpolation(spaceE.getHeading(), placeFarP_HUMAN.getHeading());
    }

    private int notCase = 0;
    public void autonomousPathUpdate() {

        if (notCase == 0) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            follower.followPath(startWithSpecimen_PATH);
            notCase = 1;
        }
        if (notCase == 1) {
            telemetry.addLine("case" + notCase);
            telemetry.update();

//                clawRot.toPick();

            if ((Math.abs(follower.getPose().getX() - spaceF.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceF.getY()) < 1) {
                follower.followPath(specimenSpaceFIRST, true);
                notCase = 2;
            }
        }

        if (notCase == 2) {
            telemetry.addLine("case" + notCase);
            telemetry.update();

//                slides.up();
//                // slides.extend_auton();
//                clawRot.toDrop();
//                // slides.smallRetract_auton();

            if ((Math.abs(follower.getPose().getX() - specimenP_HUMAN.getX()) < 3) && Math.abs(follower.getPose().getY() - specimenP_HUMAN.getY()) < 3) {
                follower.followPath(specimenControllA_PATH, 0.6, true);
                notCase = 3;
            }
        }

        if (notCase == 3) {
            telemetry.addLine("case" + notCase);
            telemetry.update();

//            // slides.retract_auton();
//            slides.down();

            if ((Math.abs(follower.getPose().getX() - spaceF.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceF.getY()) < 1) {
                follower.followPath(specimenSpaceCompleteFIRST, 0.6, true);
                notCase = 4;
            }
        }

        if (notCase == 4) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - specimenControllP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - specimenControllP_HUMAN.getY()) < 1) {
                follower.followPath(specimenControllB_PATH, 0.6, true);
                notCase = 5;
            }
        }

        if (notCase == 4) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - controllBeforeCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - specimenControllP_HUMAN.getY()) < 1) {
                follower.followPath(turn_specimen, 0.6, true);
                notCase = 5;
            }
        }
        if (notCase == 5) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - specimenTurn.getX()) < 1) && Math.abs(follower.getPose().getY() - specimenTurn.getY()) < 1) {
                follower.followPath(pickUpClose_PATH, true);
                notCase = 6;
            }
        }
        if (notCase == 6) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickupCloseP_HUMAN.getX()) < 3) && Math.abs(follower.getPose().getY() - pickupCloseP_HUMAN.getY()) < 3) {
                follower.followPath(placeClose_PATH, true);
                notCase = 7;
            }
        }
        if (notCase == 7) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - placeCloseP_HUMAN.getY()) < 1) {
                follower.followPath(moveToMiddle_PATH, true);
                notCase = 8;
            }
        }
        if (notCase == 8) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickupCloseP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupCloseP_HUMAN.getY()) < 1) {
                follower.followPath(pickUpMiddle_PATH, true);
                notCase = 9;
            }
        }
        if (notCase == 9) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickupMiddleP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupMiddleP_HUMAN.getY()) < 1) {
                follower.followPath(placeMiddle_PATH, true);
                notCase = 12;
            }
        }
//        if (notCase == 10) {
//            telemetry.addLine("case" + notCase);
//            telemetry.update();
//            if ((Math.abs(follower.getPose().getX() - placeMiddleP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - placeMiddleP_HUMAN.getY()) < 1) {
//                follower.followPath(moveToFar_PATH, true);
//                notCase = 11;
//            }
//        }
//        if (notCase == 11) {
//            telemetry.addLine("case" + notCase);
//            telemetry.update();
//            if ((Math.abs(follower.getPose().getX() - pickupMiddleP_HUMAN.getX()) < 1) && Math.abs(follower.getPose().getY() - pickupMiddleP_HUMAN.getY()) < 1) {
//                follower.followPath(pickUpFar_PATH, true);
//                notCase = 12;
//            }
//        }
//        if (notCase == 12) {
//            telemetry.addLine("case" + notCase);
//            telemetry.update();
//            if ((Math.abs(follower.getPose().getX() - pickupFarP_HUMAN.getX()) < 3) && Math.abs(follower.getPose().getY() - pickupFarP_HUMAN.getY()) < 3) {
//                follower.followPath(placeFar_PATH, true);
//                notCase = 12;
//            }
//        }
//
        if (notCase == 12) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeMiddleP_HUMAN.getX()) < 5) && Math.abs(follower.getPose().getY() - placeMiddleP_HUMAN.getY()) < 5) {
                follower.followPath(FarTOx_PATH, 0.6, true);
                notCase = 12;
            }
        }


        if (notCase == 12) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - X.getX()) < 5) && Math.abs(follower.getPose().getY() - X.getY()) < 5) {
                follower.followPath(xTOy_PATH, 0.6, true);
                notCase = 13;
            }
        }

        if (notCase == 13) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - XYbetween.getX()) < 1) && Math.abs(follower.getPose().getY() - XYbetween.getY()) < 1) {
                follower.followPath(pickUpSpecimenA_PATH, true);
                notCase = 14;
            }
        }
        if (notCase == 14) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickUpSpecimen.getX()) < 1) && Math.abs(follower.getPose().getY() - pickUpSpecimen.getY()) < 1) {
                follower.followPath(placeSpecimenA_PATH, true);
                follower.turnToDegrees(180);
                notCase = 15;
            }
        }

        if (notCase == 15) {
            telemetry.addLine("case" + notCase);
            telemetry.update();

            if ((Math.abs(follower.getPose().getX() - spaceA.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceA.getY()) < 1) {
                follower.followPath(specimenSpaceA, true);
                notCase = 16;
            }
        }

        if (notCase == 16) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeSpecimenA.getX()) < 1) && Math.abs(follower.getPose().getY() - placeSpecimenA.getY()) < 1) {
                follower.followPath(pickUpSpecimenB_PATH, true);
                notCase = 17;
            }
        }

        if (notCase == 17) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceA.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceA.getY()) < 1) {
                follower.turnToDegrees(270);
                follower.followPath(specimenSpaceCompleteA, true);
                notCase = 18;
            }
        }

        if (notCase == 18) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickUpSpecimen.getX()) < 1) && Math.abs(follower.getPose().getY() - pickUpSpecimen.getY()) < 1) {
                follower.followPath(placeSpecimenB_PATH, true);
                follower.turnToDegrees(180);
                notCase = 19;
            }
        }

        if (notCase == 19) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceB.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceB.getY()) < 1) {
                follower.followPath(specimenSpaceB, true);
                notCase = 18;
            }
        }
        if (notCase == 18) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeSpecimenB.getX()) < 1) && Math.abs(follower.getPose().getY() - placeSpecimenB.getY()) < 1) {
                follower.followPath(pickUpSpecimenC_PATH, true);
                notCase = 19;
            }
        }
        if (notCase == 19) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceB.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceB.getY()) < 1) {
                follower.turnToDegrees(270);
                follower.followPath(specimenSpaceCompleteB, true);
                notCase = 20;
            }
        }
        if (notCase == 20) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickUpSpecimen.getX()) < 1) && Math.abs(follower.getPose().getY() - pickUpSpecimen.getY()) < 1) {
                follower.followPath(placeSpecimenC_PATH, true);
                follower.turnToDegrees(180);
                notCase = 21;
            }
        }
        if (notCase == 21) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceC.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceC.getY()) < 1) {
                follower.followPath(specimenSpaceC, true);
                notCase = 22;
            }
        }
        if (notCase == 22) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeSpecimenC.getX()) < 1) && Math.abs(follower.getPose().getY() - placeSpecimenC.getY()) < 1) {
                follower.followPath(pickUpSpecimenD_PATH, true);
                notCase = 23;
            }
        }
        if (notCase == 23) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceC.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceC.getY()) < 1) {
                follower.turnToDegrees(270);
                follower.followPath(specimenSpaceCompleteC, true);
                notCase = 24;
            }
        }

        if (notCase == 24) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickUpSpecimen.getX()) < 1) && Math.abs(follower.getPose().getY() - pickUpSpecimen.getY()) < 1) {
                follower.followPath(placeSpecimenD_PATH, true);
                follower.turnToDegrees(180);
                notCase = 25;
            }
        }
        if (notCase == 25) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceD.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceD.getY()) < 1) {
                follower.followPath(specimenSpaceD, true);
                notCase = 26;
            }
        }
        if (notCase == 26) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeSpecimenD.getX()) < 1) && Math.abs(follower.getPose().getY() - placeSpecimenD.getY()) < 1) {
                follower.followPath(pickUpSpecimenE_PATH, true);
                notCase = 27;
            }
        }
        if (notCase == 27) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceD.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceD.getY()) < 1) {
                follower.turnToDegrees(270);
                follower.followPath(specimenSpaceCompleteD, true);
                notCase = 28;
            }
        }
        if (notCase == 28) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - pickUpSpecimen.getX()) < 1) && Math.abs(follower.getPose().getY() - pickUpSpecimen.getY()) < 1) {
                follower.followPath(placeSpecimenE_PATH, true);
                follower.turnToDegrees(180);
                notCase = 29;
            }
        }
        if (notCase == 29) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceE.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceE.getY()) < 1) {
                follower.followPath(specimenSpaceE, true);
                notCase = 30;
            }
        }
        if (notCase == 30) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - placeSpecimenE.getX()) < 1) && Math.abs(follower.getPose().getY() - placeSpecimenE.getY()) < 1) {
                follower.followPath(concequenceOfMyNaming, true);
                notCase = 31;
            }
        }
        if (notCase == 31) {
            telemetry.addLine("case" + notCase);
            telemetry.update();
            if ((Math.abs(follower.getPose().getX() - spaceE.getX()) < 1) && Math.abs(follower.getPose().getY() - spaceE.getY()) < 1) {
                follower.followPath(park, true);
                notCase = 32;
            }
        }
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

        follower.telemetryDebug(telemetry);
//            if (claw != null) {
//                telemetry.addLine("claw exists");
//            }
//            if (clawRot != null) {
//                telemetry.addLine("claw rotator exists");
//            }
//            if (slides != null) {
//                telemetry.addLine("slides exists");
//            }
        telemetry.update();
    }

    @Override
    public void init () {

        pathTimer = new Timer();
        Constants.setConstants(FConstants.class, LConstants.class);
        opmodeTimer = new Timer();

        opmodeTimer.resetTimer();

        follower = new Follower(hardwareMap);
        follower.setStartingPose(startP_HUMAN);

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
