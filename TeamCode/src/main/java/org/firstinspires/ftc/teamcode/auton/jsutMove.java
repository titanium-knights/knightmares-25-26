package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

import com.bylazar.telemetry.TelemetryManager;

@Autonomous(name = "time_justMove", group = "Linear OpMode")
public class jsutMove extends AutonMethods {

    private TelemetryManager telemetryM;

    @Override
    public void runOpMode() throws InterruptedException {
        // ✅ Put initialization here
//        outtake = new Outtake(hardwareMap, telemetry);
        intake = new Intake(hardwareMap, telemetry);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ✅ Wait for the game to start
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        // ✅ Now run movements
        moveBackward(0.01);
        moveForward(1.0);
    }
}


