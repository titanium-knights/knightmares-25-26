package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

@Autonomous(name = "auton_shoot3")
public class shoot3 extends AutonMethods {

    private TelemetryManager telemetryM;

    Outtake outtake;
    Intake intake;
    SwerveDrive swerveDrive;

    @Override
    public void runOpMode() throws InterruptedException {
        // ✅ Put initialization here
        outtake = new Outtake(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ✅ Wait for the game to start
        waitForStart();

        // ✅ Now run movements
        moveBackward(1.0);
        moveForward(1.0);
//        rotateUp(5.0);
//        shoot(10.0);
        pushBall(10.0);
        turnRight(90);

//      moveForward(1.0);
//      turnLeft(3000);
//      moveForward(1.0);
//      rotateDown(5.0);
//      pullBall();
//      pullBall();
//      pullBall();
//
//

    }
}


