package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="DriveTrain Teleop Red")
public class TeleopRed extends Teleop {
    @Override
    public void init() {
        setAprilTagTargetId(24);
        super.init();
    }
}
