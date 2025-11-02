package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

@Autonomous(name = "time_justMove", group = "Linear OpMode")
public class jsutMove extends AutonMethods {

    @Override
    public void runOpMode() throws InterruptedException {
        // ✅ Put initialization here
        rotator = new Rotator(hardwareMap, telemetry);
        intake = new Intake(hardwareMap);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // ✅ Wait for the game to start
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        // ✅ Now run movements
        moveForward(1.0);
    }
}


