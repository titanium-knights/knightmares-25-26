package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Storer;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Autonomous(name = "baddambaddum", group = "Linear OpMode")
public class junhossupercoolsorter extends AutonMethods {

    private TelemetryManager telemetryM;

    Outtake outtake;
    Intake intake;
    SwerveDrive swerveDrive;
    Storer storer;

    int comp = 0;
    int id = 0;
    int pattern = 0;
    @Override
    public void runOpMode() throws InterruptedException {
        // ✅ Put initialization here
        outtake = new Outtake(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        storer = new Storer(hardwareMap, telemetry);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        moveLeft(0.01);

        storer.toOne();
        //TODO: ERR after getting camera vision, assign ID to that value :>
        /*
        every combination is g p p, p g p, p p g
        PICK UP CODE for GPP
        comp is 0.
        GO IN FRONT OF THE SHOOTING BOARD
         */
        sort((id - comp) % 3);
    }

    public void sort(int pattern){
        if(pattern == 0){
            storer.toOne();
            outtake.shoot();
            intake.run();
            intake.pushBall();
            intake.pullBall();
            storer.toTwo();
            intake.pushBall();
            intake.pullBall();
            storer.toThree();
            intake.pushBall();
            intake.pullBall();
        } else if(pattern == 1){
            storer.toTwo();
            outtake.shoot();
            intake.run();
            intake.pushBall();
            intake.pullBall();
            storer.toThree();
            intake.pushBall();
            intake.pullBall();
            storer.toOne();
            intake.pushBall();
            intake.pullBall();
        } else {
            storer.toThree();
            outtake.shoot();
            intake.run();
            intake.pushBall();
            intake.pullBall();
            storer.toOne();
            intake.pushBall();
            intake.pullBall();
            storer.toTwo();
            intake.pushBall();
            intake.pullBall();
        }
    }
}


