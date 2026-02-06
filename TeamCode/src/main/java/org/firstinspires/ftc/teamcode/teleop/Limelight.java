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

    // PD control constants (start with D = 0, tune P first)
    private static final double MIN_POWER = 0.15;   // Minimum power to overcome friction
    private static final double MAX_POWER = 0.5;    // Maximum rotation power
    private static final double KP = 0.02;          // Proportional gain (tune this first)
    private static final double KD = 0.0;           // Derivative gain (add later to damp)

    // For derivative calculation
    private double lastError = 0.0;
    private double lastTime;

    @Override
    public void init() {
        // Your Rotator wrapper for the turning motor
        this.rotator = new Rotator(hardwareMap, telemetry);

        // If your FTControl version uses a different method, adjust this line
        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void start() {
        limelight.pipelineSwitch(0);   // Ensure AprilTag pipeline
        limelight.start();

        telemetry.setMsTransmissionInterval(11);

        lastTime = System.currentTimeMillis();
        lastError = 0.0;
    }

    @Override
    public void loop() {
        LLResult result = limelight.getLatestResult();

        // Debug info
        telemetry.addData("=== Limelight Status ===", "");
        telemetry.addData("Limelight Object", limelight != null ? "Connected" : "NULL");
        telemetry.addData("Result Object", result != null ? "Received" : "NULL");

        if (result != null) {
            telemetry.addData("Result Valid", result.isValid() ? "YES" : "NO");

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
            double tx = result.getTx(); // Horizontal offset (deg)
            double ty = result.getTy();
            Pose3D botpose = result.getBotpose(); // May be null

            telemetry.addData("=== AprilTag DETECTED ===", "");

            // Apply rotation
            if (tx > TARGET_TOLERANCE) {
                rotator.rotateRight();
                telemetry.addLine("Rotating Right");
            } else if (tx < TARGET_TOLERANCE) {
                rotator.rotateLeft();
                telemetry.addLine("Rotating Left");
            }

            // Telemetry for tuning
            telemetry.addData("=== AprilTag Data ===", "");
            telemetry.addData("tx (X offset)", "%.2f degrees", tx);
            telemetry.addData("ty (Y offset)", "%.2f degrees", ty);
            telemetry.addData("Distance from Center", "%.2f degrees", Math.abs(tx));
            telemetry.addData("Target Tolerance", "%.2f degrees", TARGET_TOLERANCE);
            telemetry.addData("On Target?", Math.abs(tx) < TARGET_TOLERANCE ? "YES" : "NO");

            telemetry.addData("=== Position ===", "");
            if (botpose != null) {
                telemetry.addData("Botpose X", "%.2f", botpose.getPosition().x);
                telemetry.addData("Botpose Y", "%.2f", botpose.getPosition().y);
                telemetry.addData("Botpose Z", "%.2f", botpose.getPosition().z);
            } else {
                telemetry.addData("Botpose", "Unavailable");
            }

        }

        telemetry.update();
    }

    @Override
    public void stop() {
        limelight.stop();
    }
}
