package org.firstinspires.ftc.teamcode.teleop;

import com.bylazar.configurables.annotations.Configurable;
import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.VoltageSensor; // Added for voltage protection
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.pinpoint.GoBildaPinpointDriver;
import org.firstinspires.ftc.teamcode.utilities.Intake;
import org.firstinspires.ftc.teamcode.utilities.MecanumDrive;
import org.firstinspires.ftc.teamcode.utilities.Outtake;
import org.firstinspires.ftc.teamcode.utilities.Rotator;
import org.firstinspires.ftc.teamcode.utilities.Storer;
import org.openftc.apriltag.AprilTagDetection;

import java.util.List;

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
    private ElapsedTime limelightTimer = new ElapsedTime();
    private double VISION_POLL_MS = 50;

    // Tolerance for "centered" (degrees)
    private static final double TARGET_TOLERANCE = 3.0;
    private VoltageSensor voltageSensor;

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

    // Exponential ramp-up fields
    private ElapsedTime moveTimer = new ElapsedTime();
    private boolean wasMoving = false;
    private static final double RAMP_DURATION = 2.0;  // seconds to reach full speed
    private static final double RAMP_K = 3.0;         // exponential curve steepness

    ElapsedTime runtime = new ElapsedTime();
    private LLResult latestResult = null; // Store result to use between checks

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

        limelight.pipelineSwitch(0);
        limelight.start();

        telemetry.setMsTransmissionInterval(50);

        lastTime = System.currentTimeMillis();
        lastError = 0.0;

        voltageSensor = hardwareMap.voltageSensor.iterator().next();

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
            double distance = getAprilTagDistance(); // Get distance in inches

            if (distance > 0) {
                outtake.shootAtDistance(distance);
                telemetry.addData("Distance", "%.1f in", distance);
            } else {
                // No AprilTag detected, use default power
                outtake.shoot(0.60);
                telemetry.addData("Status", "No target");
            }
        } else {
            intake.stopIntake();
            outtake.stopOuttake();
        }

        if (gamepad1.left_stick_x < -0.1) {
            rotator.rotateLeft();
        } else if (gamepad1.left_stick_x > 0.1) {
            rotator.rotateRight();
        }

        if (gamepad1.dpad_left) storer.toOne();
        if (gamepad1.dpad_left) storer.toOne();
        else if (gamepad1.dpad_up) storer.toTwo();
        else if (gamepad1.dpad_right) storer.toThree();
//        else if (gamepad1.right_stick_x < 0.1) storer.rotateLeft();
//        else if (gamepad1.right_stick_x > 0.1) storer.rotateRight();


        // 3. LIMELIGHT OPTIMIZATION
        // Only ask hardware for data every 50ms, not every loop
        if (limelightTimer.milliseconds() > VISION_POLL_MS) {
            latestResult = limelight.getLatestResult();
            limelightTimer.reset();
        }

        // Use the 'latestResult' variable (cached) instead of calling hardware again
        if (latestResult != null && latestResult.isValid() && isTargetTag(latestResult)) {
             Double tx = getTargetTx(latestResult);
             if (tx != null) {
                 if (tx > TARGET_TOLERANCE) {
                     rotator.rotateRight(tx);
                     telemetry.addLine("Rotating Right");
                 } else if (tx < -TARGET_TOLERANCE) {
                     rotator.rotateLeft(tx);
                     telemetry.addLine("Rotating Left");
                 } else {
                     lastError = 0; // Reset error to stop wind-up
                     telemetry.addData("Action", "On Target");
                 }
             }

            // Minimal Telemetry to save bandwidth
            telemetry.addData("LL tx", "%.2f", tx);
        } else {
            lastError = 0.0;
            telemetry.addData("Status", "No AprilTag Detected");
        }

        telemetry.update();
    }

    protected void setAprilTagTargetId(int tagId) {
        aprilTagTargetId = tagId;
        aprilTagTrackingEnabled = true;
    }

    protected void disableAprilTagTracking() {
        aprilTagTrackingEnabled = false;
        aprilTagTargetId = -1;
    }

    private boolean isTargetTag(LLResult result) {
        if (!aprilTagTrackingEnabled || aprilTagTargetId < 0) {
            return true; // Accept any tag if not filtering
        }

        // Check if result contains our target AprilTag ID
        // You'll need to verify the correct method for your Limelight API version
        if (result.getFiducialResults() != null) {
            for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
                if (fiducial.getFiducialId() == aprilTagTargetId) {
                    return true; // Found our target tag
                }
            }
        }

        return false;
    }

    private Double getTargetTx(LLResult result) {
        if (result.getFiducialResults() != null) {
            for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
                if (fiducial.getFiducialId() == aprilTagTargetId) {
                    return fiducial.getTargetXDegrees(); // or getTx() depending on API
                }
            }
        }
        return null; // Target not found
    }

    private double getAprilTagDistance() {
        if (latestResult != null && latestResult.isValid()) {
            List<LLResultTypes.FiducialResult> fiducials = latestResult.getFiducialResults();
            if (fiducials != null && !fiducials.isEmpty()) {
                Pose3D pose = fiducials.get(0).getCameraPoseTargetSpace();
                if (pose != null) {
                    double x = pose.getPosition().x;
                    double y = pose.getPosition().y;
                    double z = pose.getPosition().z;
                    return Math.sqrt(x*x + y*y + z*z); // Total 3D distance
                }
            }
        }
        return -1;
    }

    @Override
    public void stop() {
        limelight.stop();
        super.stop();
    }

    public void move(float x, float y, float turn) {
        if (Math.abs(x) <= stick_margin) x = 0f;
        if (Math.abs(y) <= stick_margin) y = 0f;
        if (Math.abs(turn) <= stick_margin) turn = 0f;

        boolean isMoving = (x != 0f || y != 0f || turn != 0f);

        // Reset the ramp timer when movement begins from a standstill
        if (isMoving && !wasMoving) {
            moveTimer.reset();
        }
        wasMoving = isMoving;

        // Exponential ramp: (e^(k*t/T) - 1) / (e^k - 1), clamped to 1.0 after RAMP_DURATION
        double power;
        double elapsed = moveTimer.seconds();
        if (elapsed >= RAMP_DURATION) {
            power = normalPower;
        } else {
            double rampFraction = (Math.exp(RAMP_K * elapsed / RAMP_DURATION) - 1.0)
                                / (Math.exp(RAMP_K) - 1.0);
            power = normalPower * rampFraction;
        }

        drive.move(-x * power, y * power, -turn * power);
    }
}