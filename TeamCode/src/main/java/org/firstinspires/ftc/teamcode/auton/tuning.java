//package org.firstinspires.ftc.teamcode.auton;
//
//import com.bylazar.telemetry.TelemetryManager;
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//
//import org.firstinspires.ftc.teamcode.utilities.Intake;
//import org.firstinspires.ftc.teamcode.utilities.Outtake;
//import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
//
//@Autonomous(name = "tuning")
//public class tuning extends AutonMethods{
//    private TelemetryManager telemetryM;
//
//    Outtake outtake;
//    Intake intake;
//    SwerveDrive swerveDrive;
//    Integer ID;
//    Boolean Team; //true = blue; false = red
//
//    @Override
//    public void runOpMode() throws InterruptedException {
//        // ✅ Put initialization here
//        outtake = new Outtake(hardwareMap, telemetry);
//        intake = new Intake(hardwareMap, telemetry);
//        swerveDrive = new SwerveDrive(hardwareMap, telemetry);
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.update();
//
//        moveForward(2.0);
//        moveRight(2.0);
//        moveBackward(2.0);
//        moveLeft(2.0);
//        turnRight(360);
//        turnLeft(360);
//    }
//}
