package org.firstinspires.ftc.teamcode.auton;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Storer;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

public abstract class AutonMethods extends LinearOpMode {
    public Storer storer;
    public Outtake outtake;
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



    double conversionVariable = 24;
    public double convert(double num){
        double x = num/conversionVariable;
        return x;
    }
 }





