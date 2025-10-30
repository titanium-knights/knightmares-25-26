package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.teamcode.utilities.Rotator.telemetry;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.auton.AutonMethods;
import org.firstinspires.ftc.teamcode.teleop.Teleop;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

@Autonomous(name = "Auton Near Basket RED")
public class Complex_nearBasket_RED extends AutonMethods {

    @Override
    public void runOpMode() throws InterruptedException {
        // ✅ Put initialization here
        rotator = new Rotator(hardwareMap, telemetry);
        intake = new Intake(hardwareMap);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ✅ Wait for the game to start
        waitForStart();

        // ✅ Now run movements
        moveForward(10.0);
        moveLeft(5.0);
        moveForward(10.0);
        rotateDown(10.0);
        pullBall(10.0);
        takeIn(10.0);
        moveBackward(10.0);
        moveRight(5.0);
        rotateUp(5.0);
        shoot(10.0);
        pushBall(10.0);
    }
}


