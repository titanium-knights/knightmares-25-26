//package org.firstinspires.ftc.teamcode.auton;
//
//import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
//
//import com.bylazar.telemetry.TelemetryManager;
//
//import org.firstinspires.ftc.teamcode.utilities.Intake;
//import org.firstinspires.ftc.teamcode.utilities.Outtake;
//import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
//
//@Autonomous(name = "shoot3_lb")
//public class shootLB extends AutonMethods {
//
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
//
//
//
//      moveForward(4.0);
//      turnLeft(45);
//      outtake();
//      outtake();
//      outtake();
//      turnRight(45);
//    if(ID == 21 && Team == true) {
//        moveBackward(3.0);
//        turnLeft(90); //degrees
//        moveForward(0.8);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveBackward(1.0);
//        moveForward(3.0);
//        turnLeft(45);
//        outtake();
//        outtake();
//        outtake();
//    }
//    else if(ID == 22 && Team==true) {
//        moveBackward(2.0);
//        turnLeft(90);
//        moveForward(0.8);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveBackward(1.0);
//        moveForward(2.0);
//        turnLeft(45);
//        outtake();
//        outtake();
//        outtake();
//    }
//    else if(ID == 23 && Team==true){
//        moveBackward(1.0);
//        turnLeft(90);
//        moveForward(0.8);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveBackward(1.0);
//        moveForward(1.0);
//        turnLeft(45);
//        outtake();
//        outtake();
//        outtake();
//    }
//    else if (ID == 21 && Team==false){
//        moveBackward(3.0);
//        moveRight(1.0);
//        turnRight(90); //degrees
//        moveForward(0.8);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveBackward(1.0);
//        moveForward(3.0);
//        turnRight(45);
//        outtake();
//        outtake();
//        outtake();
//    }
//    else if (ID == 22 && Team == false) {
//        moveBackward(2.0);
//        moveRight(1.0);
//        turnRight(90);
//        moveForward(0.8);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveForward(0.2);
//        intake();
//        moveBackward(1.0);
//        moveForward(2.0);
//        turnRight(45);
//        outtake();
//        outtake();
//        outtake();
//    }
//    else{
//            moveBackward(1.0);
//            moveRight(1.0);
//            turnRight(90);
//            moveForward(0.8);
//            intake();
//            moveForward(0.2);
//            intake();
//            moveForward(0.2);
//            intake();
//            moveBackward(1.0);
//            moveForward(1.0);
//            turnRight(45);
//            outtake();
//            outtake();
//            outtake();
//        }
//
//
//
//
//
//
//    }
//}
//
//
