package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

@Autonomous(name = "auton_shoot3")
public class shoot3 extends AutonMethods {

    private TelemetryManager telemetryM;

    @Override
    public void runOpMode() throws InterruptedException {
        // ✅ Put initialization here
        rotator = new Rotator(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ✅ Wait for the game to start
        waitForStart();

        // ✅ Now run movements
        moveBackward(0.01);
        moveForward(1.0);
        rotateUp(5.0);
        shoot(10.0);
        pushBall(10.0);
    }
}


