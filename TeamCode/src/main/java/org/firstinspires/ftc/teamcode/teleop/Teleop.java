package org.firstinspires.ftc.teamcode.teleop;

import static java.lang.Thread.sleep;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pinpoint.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.Storer;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Configurable
@TeleOp(name="DriveTrain Teleop")
public class Teleop extends OpMode {

    Intake intake;
    Outtake outtake;
    Storer storer;
    MecanumDrive drive;
    Rotator rotator;
    GoBildaPinpointDriver odo;
    private Limelight3A limelight;

    final double normalPower = 0.99;

    public float stick_margin = 0.1f;

    public boolean intakeState = false;
    public boolean shootState = false;

    private TelemetryManager telemetryM;
    protected int aprilTagTargetId = -1;
    protected boolean aprilTagTrackingEnabled = false;

    // Tolerance for "centered" (degrees)
    private static final double TARGET_TOLERANCE = 2.0;

    // PD control constants (tune if needed)
    private static final double MIN_POWER = 0.15;   // Minimum power to overcome friction
    private static final double MAX_POWER = 0.5;    // Maximum rotation power
    private static final double KP = 0.02;          // Proportional gain
    private static final double KD = 0.0;           // Derivative gain

    // For derivative calculation
    private double lastError = 0.0;
    private double lastTime;

    enum ButtonPressState {
        PRESSED_GOOD,
        DEPRESSED,
        UNPRESSED
    }

    ButtonPressState intakeButton;
    ButtonPressState shootButton;
    ButtonPressState rotatorButton;
    ButtonPressState ballButton;

    boolean slowMode = false;
    double time;
    int autonAction = 0;
    boolean intakeRunning = false;
    boolean ballState = false;

    ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");

        this.rotatorButton = ButtonPressState.UNPRESSED;
        this.intakeButton = ButtonPressState.UNPRESSED;
        this.shootButton = ButtonPressState.UNPRESSED;
        this.ballButton = ButtonPressState.UNPRESSED;
        this.intake = new Intake(hardwareMap, telemetry);
        this.drive = new MecanumDrive(hardwareMap);
        this.storer = new Storer(hardwareMap, telemetry);
        this.outtake = new Outtake(hardwareMap, telemetry);
        this.rotator = new Rotator(hardwareMap, telemetry);

        telemetryM = PanelsTelemetry.INSTANCE.getTelemetry();

        limelight.pipelineSwitch(0);   // Ensure AprilTag pipeline
        limelight.start();

        telemetry.setMsTransmissionInterval(11);

        lastTime = System.currentTimeMillis();
        lastError = 0.0;

        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    @Override
    public void loop() {

        float x = gamepad2.left_stick_x;
        float y = gamepad2.left_stick_y;
        float turn = gamepad2.right_stick_x;

        stick_margin = 0.1f;
        move(x, y, turn);


        if (gamepad1.left_bumper) {
            intake.pushBall();
        } else if (gamepad1.right_bumper) {
            intake.pullBall();
        } else {
            intake.pullBall();
        }

        if (gamepad1.left_trigger > 0.1) {
            intake.run();
        } else if (gamepad1.right_trigger > 0.1) {
            outtake.shoot();
            // intake.run();
        } else {
            intake.stopIntake();
            outtake.stopOuttake();
        }

        if (gamepad1.dpad_left) {
            storer.toOne();
        } else if (gamepad1.dpad_up) {
            storer.toTwo();
        } else if (gamepad1.dpad_right) {
            storer.toThree();
        } else if (gamepad1.right_stick_x < 0.1) {
            storer.rotateLeft();
        } else if (gamepad1.right_stick_x > 0.1) {
            storer.rotateRight();
        }

        // LIMELIGHT STUFF UHUHUHU

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

            // PD power based on tx error
            double power = calculateRotationPower(tx);

            // Apply rotation
            if (Math.abs(tx) > TARGET_TOLERANCE) {
                rotator.setPower(power);
                telemetry.addData("Action", power > 0 ? "Rotating Right" : "Rotating Left");
            } else {
                rotator.stop();
                telemetry.addData("Action", "On Target");
            }

            // Telemetry for tuning
            telemetry.addData("=== AprilTag Data ===", "");
            telemetry.addData("tx (X offset)", "%.2f degrees", tx);
            telemetry.addData("ty (Y offset)", "%.2f degrees", ty);
            telemetry.addData("Distance from Center", "%.2f degrees", Math.abs(tx));
            telemetry.addData("Target Tolerance", "%.2f degrees", TARGET_TOLERANCE);
            telemetry.addData("Calculated Power", "%.3f", power);
            telemetry.addData("On Target?", Math.abs(tx) < TARGET_TOLERANCE ? "YES" : "NO");

            telemetry.addData("=== Position ===", "");
            if (botpose != null) {
                telemetry.addData("Botpose X", "%.2f", botpose.getPosition().x);
                telemetry.addData("Botpose Y", "%.2f", botpose.getPosition().y);
                telemetry.addData("Botpose Z", "%.2f", botpose.getPosition().z);
            } else {
                telemetry.addData("Botpose", "Unavailable");
            }

        } else {
            // No valid target
            rotator.stop();
            lastError = 0.0;
            telemetry.addData("Status", "No AprilTag Detected");
        }

        telemetry.update();
    }

    public void limelightStop() {
        rotator.stop();
        limelight.stop();
    }

    protected void setAprilTagTargetId(int tagId) {
        aprilTagTargetId = tagId;
        aprilTagTrackingEnabled = true;
    }

    public void move(float x, float y, float turn) {

        if (Math.abs(x) <= stick_margin) x = 0f;
        if (Math.abs(y) <= stick_margin) y = 0f;
        if (Math.abs(turn) <= stick_margin) turn = 0f;

        double multiplier = normalPower;

        drive.move(-x * multiplier, y * multiplier, -turn * multiplier);
    }

    /**
     * Calculate rotation power using PD control
     */
    private double calculateRotationPower(double tx) {
        double currentTime = System.currentTimeMillis();
        double dt = (currentTime - lastTime) / 1000.0; // seconds

        if (dt < 0.001) dt = 0.001; // prevent div by 0

        double derivative = (tx - lastError) / dt;

        double pTerm = KP * tx;
        double dTerm = KD * derivative;
        double power = pTerm + dTerm;  // flip sign here if robot turns wrong way

        // Clamp to max
        power = Math.max(-MAX_POWER, Math.min(MAX_POWER, power));

        // Apply minimum power outside tolerance to overcome friction
        if (Math.abs(tx) > TARGET_TOLERANCE) {
            if (power > 0) {
                power = Math.max(power, MIN_POWER);
            } else if (power < 0) {
                power = Math.min(power, -MIN_POWER);
            }
        }

        lastError = tx;
        lastTime = currentTime;

        return power;
    }
}
