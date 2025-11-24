package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Storer;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

import com.bylazar.telemetry.TelemetryManager;

@Autonomous(name = "shoot3", group = "Linear OpMode")
public class shoot3 extends AutonMethods{
    public void runOpMode() throws InterruptedException {
        intake = new Intake(hardwareMap, telemetry);
        swerveDrive = new SwerveDrive(hardwareMap, telemetry);
        storer = new Storer(hardwareMap, telemetry);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        moveLeft(0.2);

        // ✅ Now run movements
        moveBackward(3.0);

//        if in other launch zone
//        ElapsedTime runtime = new ElapsedTime();
//        waitForStart();
//        runtime.reset();
        //moveForward(1.0);
    }
}
