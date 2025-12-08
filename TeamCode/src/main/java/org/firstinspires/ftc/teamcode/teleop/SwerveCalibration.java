package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.utilities.BetterBetterSwerveDrive;

@TeleOp(name = "Swerve Drive with Calibration", group = "TeleOp")

public class SwerveCalibration extends LinearOpMode{
    private BetterBetterSwerveDrive swerveDrive;

    @Override
    public void runOpMode() {
        // Initialize swerve drive
        swerveDrive = new BetterBetterSwerveDrive(hardwareMap);

        // Display calibration instructions
        telemetry.addLine("=== SWERVE CALIBRATION ===");
        telemetry.addLine("1. Manually align all wheels straight forward");
        telemetry.addLine("2. Press [A] to calibrate");
        telemetry.addLine("3. Press [START] to begin driving");
        telemetry.addData("Status", swerveDrive.isCalibrated() ? "CALIBRATED ✓" : "NOT CALIBRATED");
        telemetry.update();

        // Wait for calibration during init
        while (!opModeIsActive() && !isStopRequested()) {
            // Press A to calibrate
            if (gamepad1.a) {
                swerveDrive.calibrateModules();
                telemetry.addLine("✓ Calibration Complete!");
                telemetry.addData("Offsets", swerveDrive.getCalibrationInfo());
                telemetry.update();
                sleep(500); // Debounce
            }

            // Update telemetry
            telemetry.addData("Status", swerveDrive.isCalibrated() ? "CALIBRATED ✓" : "NOT CALIBRATED");
            telemetry.addLine();
            telemetry.addLine("Press [A] to calibrate");
            telemetry.addLine("Press [START] when ready");
            telemetry.update();
        }

        // Main driving loop
        while (opModeIsActive()) {
            // Get gamepad inputs
            double strafeX = -gamepad1.left_stick_x;  // Strafe (negative = left)
            double strafeY = -gamepad1.left_stick_y;  // Forward (negative = backward)
            double rotation = -gamepad1.right_stick_x; // Rotation (negative = CCW)

            // Toggle field-centric mode with B button
            boolean fieldCentric = !gamepad1.b;

            // Allow recalibration during operation (press X)
            if (gamepad1.x) {
                swerveDrive.calibrateModules();
                telemetry.addLine("✓ Recalibrated!");
                sleep(500); // Debounce
            }

            // Drive the robot
            swerveDrive.move(strafeX, strafeY, rotation, fieldCentric);

            // Telemetry
            telemetry.addData("Mode", fieldCentric ? "Field-Centric" : "Robot-Centric");
            telemetry.addData("Calibrated", swerveDrive.isCalibrated() ? "✓" : "✗");
            telemetry.addData("Offsets", swerveDrive.getCalibrationInfo());
            telemetry.addLine();
            telemetry.addData("Strafe X", "%.2f", strafeX);
            telemetry.addData("Strafe Y", "%.2f", strafeY);
            telemetry.addData("Rotation", "%.2f", rotation);
            telemetry.addLine();
            telemetry.addLine("Controls:");
            telemetry.addLine("  Left Stick: Strafe");
            telemetry.addLine("  Right Stick X: Rotate");
            telemetry.addLine("  [B] Hold: Robot-Centric");
            telemetry.addLine("  [X]: Recalibrate");
            telemetry.update();
        }

        // Stop motors when done
        swerveDrive.stop();
    }
}
