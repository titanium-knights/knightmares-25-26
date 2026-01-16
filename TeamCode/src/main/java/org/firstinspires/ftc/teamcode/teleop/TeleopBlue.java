package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="DriveTrain Teleop Blue")
public class TeleopBlue extends Teleop {
    @Override
    public void init() {
        setAprilTagTargetId(20);
        super.init();
    }
}
