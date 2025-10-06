package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.utilities.CONFIG;
import org.firstinspires.ftc.teamcode.utilities.SlidesState;
import org.firstinspires.ftc.teamcode.utilities.SwerveDrive;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

import org.firstinspires.ftc.robotcore.external.Telemetry;

@Configurable
@TeleOp(name="Swerve Drive")
public class SwerveDriveOpMode extends OpMode {

    SwerveDrive drive;

    float stick_margin = 0.1f;
    final double normalPower = 0.9;
    private TelemetryManager telemetryM;

    public void init() {
        this.drive = new SwerveDrive(hardwareMap, telemetry);

        // this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();
    }

    public void loop() {

        //DRIVE
        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;
//        if (gamepad2.a) {
//            telemetryM.debug("gaempad2.a pressed");
//            telemetryM.update(telemetry);
//            drive.move(0,0.5f,0);
//        }
        move(x, y, turn);

    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0

        if (Math.abs(x) <= stick_margin) x = .0f;
        if (Math.abs(y) <= stick_margin) y = .0f;
        if (Math.abs(turn) <= stick_margin) turn = .0f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
        drive.move(-x * multiplier, y * multiplier, 0);
    }

}
