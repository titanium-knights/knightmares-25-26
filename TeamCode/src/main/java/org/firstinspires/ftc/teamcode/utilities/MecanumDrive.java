package org.firstinspires.ftc.teamcode.utilities;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.HashMap;
import java.util.Objects;

public class MecanumDrive {

    public MecanumDrive(HardwareMap hmap) {
        fl = hmap.get(DcMotor.class, CONFIG.FRONT_LEFT);
        fr = hmap.get(DcMotor.class, CONFIG.FRONT_RIGHT);
        bl = hmap.get(DcMotor.class, CONFIG.BACK_LEFT);
        br = hmap.get(DcMotor.class, CONFIG.BACK_RIGHT);

        fl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        bl.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        fr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        br.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        fl.setDirection(DcMotorSimple.Direction.FORWARD); // FORWARD
        bl.setDirection(DcMotorSimple.Direction.REVERSE);
        fr.setDirection(DcMotorSimple.Direction.FORWARD); // FORWARD
        br.setDirection(DcMotorSimple.Direction.REVERSE);

        directions.put(fl, new double[]{1, 1});
        directions.put(fr, new double[]{1, -1});
        directions.put(bl, new double[]{1, -1});
        directions.put(br, new double[]{1, 1});
    }

    public static DcMotor fl, fr, bl, br;

    public static HashMap<DcMotor, double[]> directions = new HashMap<>();

    public void move(double x, double y, double turn) {

        // dot of fl and br
        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) - turn;
        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) - turn);
        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) + turn;
        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) + turn;

        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
        fl.setPower(dot_fl / max);
        br.setPower(dot_br / max);
        fr.setPower(dot_fr / max);
        bl.setPower(dot_bl / max);
    }

    public void moveFL(double x, double y, double turn) {
        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) - turn;
        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) - turn);
        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) + turn;
        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) + turn;

        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
        fl.setPower(dot_fl / max);
        br.setPower(0);
        fr.setPower(0);
        bl.setPower(0);
    }

    public void moveFR(double x, double y, double turn) {
        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) - turn;
        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) - turn);
        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) + turn;
        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) + turn;

        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
        fl.setPower(0);
        br.setPower(0);
        fr.setPower(dot_fr / max);
        bl.setPower(0);
    }

    public void moveBL(double x, double y, double turn) {
        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) - turn;
        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) - turn);
        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) + turn;
        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) + turn;

        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
        fl.setPower(0);
        br.setPower(0);
        fr.setPower(0);
        bl.setPower(dot_bl / max);
    }

    public void moveBR(double x, double y, double turn) {
        double dot_fl = dot(Objects.requireNonNull(directions.get(fl)), new double[]{x, y}) - turn;
        double dot_fr = (dot(Objects.requireNonNull(directions.get(fr)), new double[]{x, y}) - turn);
        double dot_bl = dot(Objects.requireNonNull(directions.get(bl)), new double[]{x, y}) + turn;
        double dot_br = dot(Objects.requireNonNull(directions.get(br)), new double[]{x, y}) + turn;

        double max = Math.max(1, Math.max(Math.max(Math.abs(dot_fl), Math.abs(dot_fr)), Math.max(Math.abs(dot_bl), Math.abs(dot_br))));
        fl.setPower(0);
        br.setPower(dot_br / max);
        fr.setPower(0);
        bl.setPower(0);
    }

    // Each double[] will be a direction vector of length two
    public double dot(double[] a, double[] b) {
        return a[0] * b[0] + a[1] * b[1];
    }
    public void moveFL(double power) {
        fl.setPower(power);
    }

    public void stopFL() {
        fl.setPower(0);
    }

    public void moveFR(double power) {
        fr.setPower(power);
    }

    public void stopFR() {
        fr.setPower(0);
    }

    public void moveBL(double power) {
        bl.setPower(power);
    }

    public void stopBL() {
        bl.setPower(0);
    }

    public void moveBR(double power) {
        br.setPower(power);
    }

    public void stopBR() {
        br.setPower(0);
    }
}