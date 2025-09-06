package org.firstinspires.ftc.teamcode.backupAuton.auton;
//this is TIME BASED
// TODO CHANGE NUMBERS AFTER
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;


@Autonomous(name="time_nearBasket", group="Linear OpMode")
@Config
public class reset_arm extends AutonMethods {

@Override
    public void runOpMode() throws InterruptedException {
        super.runOpMode();
        telemetry.addData("Initialized:", "Hopefully");
        telemetry.update();
        //start timer
        ElapsedTime runtime = new ElapsedTime();
        waitForStart();
        runtime.reset();

        // scoring preload
        clawClose();
        clawNeutral();

        telemetry.addLine("Run time:" +  runtime.toString());
        telemetry.update();
    }
}
