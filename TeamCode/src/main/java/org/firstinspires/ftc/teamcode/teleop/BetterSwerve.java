package org.firstinspires.ftc.teamcode.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.pinpoint.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utilities.BetterSwerveDrive;

@Configurable
@TeleOp(name="BetterSwerve")
public class BetterSwerve extends LinearOpMode {

    BetterSwerveDrive drive;
    GoBildaPinpointDriver odo;

    float stick_margin = 0.1f;
    final double normalPower = 0.9;
    double oldTime = 0;
    private TelemetryManager telemetryM;

    @Override
    public void runOpMode() {

        this.drive = new BetterSwerveDrive(hardwareMap, telemetry);

        // this.telemetry = telemetry;
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;

        move(x, y, turn);
    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set STICK_MARGIN to 0

        if (Math.abs(x) <= stick_margin) x = .15f;
        if (Math.abs(y) <= stick_margin) y = .15f;
        if (Math.abs(turn) <= stick_margin) turn = .15f;

        //Notation of a ? b : c means if a is true do b, else do c.
        double multiplier = normalPower;
        drive.move(-x * multiplier, y * multiplier, -turn * multiplier);
    }
}
