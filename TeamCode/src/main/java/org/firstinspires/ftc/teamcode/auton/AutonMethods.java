package org.firstinspires.ftc.teamcode.auton;

import static org.firstinspires.ftc.teamcode.utilities.CONFIG.intake;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;


import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
import org.firstinspires.ftc.robotcore.external.Telemetry;

public abstract class AutonMethods extends LinearOpMode {
    public Rotator rotator;
    public Intake intake;
    public SwerveDrive swerveDrive;
    public final double POWER = 0.7;

    public void stopDrive() {
        swerveDrive.move(0, 0, 0);
        telemetry.update();
        sleep(100);
    }


    // Going forward, backward, turning, going left, going right

    public void moveForward(double x){
        double duration = 2000 * x;
        swerveDrive.move(0, -POWER, 0);
        sleep((int)duration);
        stopDrive();
    }

     public void moveBackward(double x){
         double duration = 1080 * x;
         swerveDrive.move(0, POWER, 0);
         sleep((int)duration);
         stopDrive();
     }

     public void moveRight(double x){
         double duration = 1500 * x;
         swerveDrive.move(POWER, 0, 0);
         sleep((int)duration);
         stopDrive();
     }

     public void moveLeft(double x) {
         double duration = 1500 * x;
         swerveDrive.move(-POWER, 0, 0);
         sleep((int) duration);
         stopDrive();
     }
     //Arm Rotator

     public void rotateUp(double x){
        double duration = 8000 * x;
        rotator.rotateUp();
        sleep((int)duration);
        stopDrive();
     }

     public void rotateDown(double x){
        double duration = 8000 * x;
        rotator.rotateDown();
        sleep((int)duration);
        stopDrive();
     }

    public void pullBall(double x){
        double duration = 8000 * x;
        intake.pullBall();
        sleep((int)duration);
        stopDrive();
    }

    public void pushBall(double x){
        double duration = 8000 * x;
        intake.pushBall();
        sleep((int)duration);
        stopDrive();
    }

    public void shoot(double x){
        double duration = 8000 * x;
        intake.shoot();
        sleep((int)duration);
        stopDrive();
    }

    public void takeIn(double x){
        double duration = 8000 * x;
        intake.takeIn();
        sleep((int)duration);
        stopDrive();
    }


    double conversionVariable = 24;
    public double convert(double num){
        double x = num/conversionVariable;
        return x;
    }
 }





