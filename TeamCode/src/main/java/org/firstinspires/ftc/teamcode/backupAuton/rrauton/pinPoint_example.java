//package org.firstinspires.ftc.teamcode.backupAuton.rrauton;
//
//import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
//import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
//
//import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
//import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
//import org.firstinspires.ftc.teamcode.rr.GoBildaPinpointDriver;
//
//import java.util.Locale;
//
//@TeleOp(name="Park", group="Linear OpMode")
////@Disabled
//
//public class pinPoint_example extends LinearOpMode {
//
//    GoBildaPinpointDriver odo; // Declare OpMode member for the Odometry Computer
//
//    double oldTime = 0;
//
//
//    @Override
//    public void runOpMode() {
//
//        odo = hardwareMap.get(GoBildaPinpointDriver.class,"odo"); // matches config??
//
//        // offset from odometry computer
//        // x pod offset: left of center is positive, right negative
//        // y pod offset: forward of center is positive, backwards negative
//        odo.setOffsets(37.0, 45.0); //these are tuned for 3110-0002-0001 Product Insight #1
//
//        // sets the kind of pods we are using
//        odo.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
//
//        // i think: the x pod should increase when you move the robot forward, otherwise replace FORWARD with REVERSE
//        //          the y pod should increase when you move the robot left, otherwise replace FORWARD with REVERSE
//        odo.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD, GoBildaPinpointDriver.EncoderDirection.FORWARD);
//
//        // resets position to 0,0,0 and recalibrate imu
//        // imu recalibrates when first powered on, but this is good to make sure initial calibration is good
//        odo.resetPosAndIMU();
//
//        telemetry.addData("Status", "Initialized");
//        telemetry.addData("X offset", odo.getXOffset());
//        telemetry.addData("Y offset", odo.getYOffset());
//        telemetry.addData("Device Version Number:", odo.getDeviceVersion());
//        telemetry.addData("Device Scalar", odo.getYawScalar());
//        telemetry.update();
//
//        // Wait for the game to start (driver presses START)
//        waitForStart();
//        resetRuntime();
//
//        // run until the end of the match (driver presses STOP)
//        while (opModeIsActive()) {
//
//            // requests update from the odo computer
//            odo.update();
//
//            // manually resetting stuff
//            if (gamepad1.a){
//                odo.resetPosAndIMU(); // resets the position to 0 and recalibrates the IMU
//            }
//
//            if (gamepad1.b){
//                odo.recalibrateIMU(); // recalibrates the IMU without resetting position
//            }
//
//            // prints the time each cycle takes
//            double newTime = getRuntime();
//            double loopTime = newTime-oldTime;
//            double frequency = 1/loopTime;
//            oldTime = newTime;
//
//            // prints current position of robot
//            Pose2D pos = odo.getPosition();
//            String data = String.format(Locale.US, "{X: %.3f, Y: %.3f, H: %.3f}", pos.getX(DistanceUnit.MM), pos.getY(DistanceUnit.MM), pos.getHeading(AngleUnit.DEGREES));
//            telemetry.addData("Position", data);
//
//            // prints velocity of robot
//            Pose2D vel = odo.getVelocity();
//            String velocity = String.format(Locale.US,"{XVel: %.3f, YVel: %.3f, HVel: %.3f}", vel.getX(DistanceUnit.MM), vel.getY(DistanceUnit.MM), vel.getHeading(AngleUnit.DEGREES));
//            telemetry.addData("Velocity", velocity);
//
//
//            /*
//            Gets the Pinpoint device status. Pinpoint can reflect a few states. But we'll primarily see
//            READY: the device is working as normal
//            CALIBRATING: the device is calibrating and outputs are put on hold
//            NOT_READY: the device is resetting from scratch. This should only happen after a power-cycle
//            FAULT_NO_PODS_DETECTED - the device does not detect any pods plugged in
//            FAULT_X_POD_NOT_DETECTED - The device does not detect an X pod plugged in
//            FAULT_Y_POD_NOT_DETECTED - The device does not detect a Y pod plugged in
//            */
//            telemetry.addData("Status", odo.getDeviceStatus());
//
//            telemetry.addData("Pinpoint Frequency", odo.getFrequency()); //prints/gets the current refresh rate of the Pinpoint
//
//            telemetry.addData("REV Hub Frequency: ", frequency); //prints the control system refresh rate
//            telemetry.update();
//
//        }
//    }}