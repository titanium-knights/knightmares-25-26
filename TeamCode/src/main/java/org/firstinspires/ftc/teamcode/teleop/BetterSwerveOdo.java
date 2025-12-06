package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.utilities.BetterBetterSwerveDrive;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;

@Configurable
@TeleOp(name="SwerveDriveOdo")
public class BetterSwerveOdo extends OpMode {
    BetterBetterSwerveDrive drive;

    float stick_margin = 0.1f;
    final double normalPower = 0.9;
    private TelemetryManager telemetryM;

    private boolean lastAState = false;

    @Override
    public void init() {
        this.drive = new BetterBetterSwerveDrive(hardwareMap);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        // Reset IMU heading at initialization
        drive.resetOdometry();

        telemetryM.debug("Swerve Drive Initialized");
        telemetryM.debug("Press A to toggle field-centric mode");
        telemetryM.update(telemetry);
    }

    @Override
    public void loop() {
        // Toggle field-centric mode with A button (with debounce)
//        if (gamepad2.a && !lastAState) {
//            drive.toggleFieldCentric();
//        }
        lastAState = gamepad2.a;

        // Reset heading with B button
//        if (gamepad2.b) {
//            drive.resetHeading();
//            telemetryM.debug("Heading reset!");
//        }

        // DRIVE
        float x = gamepad2.left_stick_x;
        float y = -gamepad2.left_stick_y; // Invert Y for typical controller orientation
        float turn = gamepad2.right_stick_x;

        move(x, y, turn);
    }

    public void move(float x, float y, float turn) {
        // if the stick movement is negligible, set to 0
        if (Math.abs(x) <= stick_margin) x = 0.0f;
        if (Math.abs(y) <= stick_margin) y = 0.0f;
        if (Math.abs(turn) <= stick_margin) turn = 0.0f;

        // Apply multiplier and drive
        double multiplier = normalPower;
        drive.move(x * multiplier, y * multiplier, turn * multiplier, true); //false?
    }
}