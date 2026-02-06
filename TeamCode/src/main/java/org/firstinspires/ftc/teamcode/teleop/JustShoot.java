package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pinpoint.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utilities.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;
import org.firstinspires.ftc.teamcode.utilities.Storer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Configurable
@TeleOp(name="JustShoot")
public class JustShoot extends OpMode{
    Intake intake;
    Outtake outtake;
    Rotator rotator;
    private TelemetryManager telemetryM;


    @Override
    public void init() {

        this.outtake = new Outtake(hardwareMap, telemetry);
        this.rotator = new Rotator(hardwareMap, telemetry);
        this.intake = new Intake(hardwareMap, telemetry);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

    }

    @Override
    public void loop() {

        if (gamepad1.dpad_left) {
            rotator.rotateRight();
            telemetryM.addLine("dpad left");
            telemetryM.update();
        } else if (gamepad1.dpad_right) {
            rotator.rotateLeft();
            telemetryM.addLine("dpad right");
            telemetryM.update();
        }

        if (gamepad1.left_bumper) {
            outtake.shoot();
            intake.run();
        } else {
            outtake.stopOuttake();
            intake.stopIntake();
        }

        if (gamepad1.right_bumper) {
            intake.pushBall();
        } else {
            intake.pullBall();
        }
    }
}
