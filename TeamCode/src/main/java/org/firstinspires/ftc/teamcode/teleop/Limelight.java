package org.firstinspires.ftc.teamcode.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.utilities.Rotator;

@Configurable
@TeleOp(name="Limelight")
public class Limelight extends OpMode {

    private Limelight3A limelight;
    private Rotator rotator;
    private TelemetryManager telemetryM;

    // Tolerance for "centered" (degrees)
    private static final double TARGET_TOLERANCE = 2.0;

    // PD control constants
    private static final double MIN_POWER = 0.15;  // Minimum power to overcome friction
    private static final double MAX_POWER = 0.5;   // Maximum rotation power
    private static final double KP = 0.02;         // Proportional gain
    private static final double KD = 0.015;        // Derivative gain - adjust this to tune damping

    // For derivative calculation
    private double lastError = 0.0;
    private double lastTime;

    @Override
    public void init() {
        this.rotator = new Rotator(hardwareMap, telemetry);
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        limelight.pipelineSwitch(0);
        limelight.start();
        telemetry.setMsTransmissionInterval(11);
        lastTime = System.currentTimeMillis();
        lastError = 0.0;
    }

    @Override
    public void loop() {
        LLResult result = limelight.getLatestResult();

        // Detailed debug info
        telemetry.addData("=== Limelight Status ===", "");
        telemetry.addData("Limelight Object", limelight != null ? "Connected" : "NULL");
        telemetry.addData("Result Object", result != null ? "Received" : "NULL");

        if (result != null) {
            telemetry.addData("Result Valid", result.isValid() ? "YES" : "NO");

            // Show raw result data for debugging
            if (result.isValid()) {
                telemetry.addData("Target Count", "Targets detected");
            } else {
                telemetry.addData("Target Count", "0 - No AprilTags");
                telemetry.addData("", "");
                telemetry.addData("Troubleshooting:", "");
                telemetry.addData("1.", "Check Limelight web interface");
                telemetry.addData("2.", "Verify pipeline 0 = AprilTag mode");
                telemetry.addData("3.", "Check AprilTag family = 36h11");
                telemetry.addData("4.", "Ensure tag is visible & well-lit");
            }
        } else {
            telemetry.addData("ERROR", "No result from Limelight - check connection");
        }

        if (result != null && result.isValid()) {
            double tx = result.getTx(); // Horizontal offset
            double ty = result.getTy();
            Pose3D botpose = result.getBotpose();

            telemetry.addData("=== AprilTag DETECTED ===", "");

            // Calculate PD power based on error
            double power = calculateRotationPower(tx);

            // Apply rotation with calculated power
            if (Math.abs(tx) > TARGET_TOLERANCE) {
                rotator.setPower(power);
                telemetry.addData("Action", power > 0 ? "Rotating Right" : "Rotating Left");
            } else {
                rotator.stop();
                telemetry.addData("Action", "On Target");
            }

            // Telemetry
            telemetry.addData("=== AprilTag Data ===", "");
            telemetry.addData("tx (X offset)", "%.2f degrees", tx);
            telemetry.addData("ty (Y offset)", "%.2f degrees", ty);
            telemetry.addData("Distance from Center", "%.2f degrees", Math.abs(tx));
            telemetry.addData("Target Tolerance", "%.2f degrees", TARGET_TOLERANCE);
            telemetry.addData("Calculated Power", "%.3f", power);
            telemetry.addData("On Target?", Math.abs(tx) < TARGET_TOLERANCE ? "YES" : "NO");
            telemetry.addData("=== Position ===", "");
            telemetry.addData("Botpose X", "%.2f", botpose.getPosition().x);
            telemetry.addData("Botpose Y", "%.2f", botpose.getPosition().y);
            telemetry.addData("Botpose Z", "%.2f", botpose.getPosition().z);

        } else {
            // No valid target - stop rotation and reset derivative
            rotator.stop();
            lastError = 0.0;
            telemetry.addData("Status", "No AprilTag Detected");
        }

        telemetry.update();
    }

    /**
     * Calculate rotation power using PD control
     * P term: responds to current error
     * D term: responds to rate of change, damping oscillations
     */
    private double calculateRotationPower(double tx) {
        // Calculate time delta
        double currentTime = System.currentTimeMillis();
        double dt = (currentTime - lastTime) / 1000.0; // Convert to seconds

        // Prevent division by zero
        if (dt < 0.001) dt = 0.001;

        // Calculate derivative (rate of change of error)
        double derivative = (tx - lastError) / dt;

        // PD control: proportional + derivative
        double pTerm = KP * tx;
        double dTerm = KD * derivative;
        double power = pTerm + dTerm;

        // Clamp to max power
        power = Math.max(-MAX_POWER, Math.min(MAX_POWER, power));

        // Add minimum power to overcome friction (if not on target)
        if (Math.abs(tx) > TARGET_TOLERANCE) {
            if (power > 0) {
                power = Math.max(power, MIN_POWER);
            } else if (power < 0) {
                power = Math.min(power, -MIN_POWER);
            }
        }

        // Update tracking variables
        lastError = tx;
        lastTime = currentTime;

        return power;
    }

    @Override
    public void stop() {
        rotator.stop();
        limelight.stop();
    }
}