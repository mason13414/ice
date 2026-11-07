package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.RobotSide;

/**
 * Blue alliance teleop. All the behavior lives in MainTeleop so Red and Blue cannot
 * drift apart — the same layout as last season, which worked well.
 */
@TeleOp(name = "Main Teleop Blue", group = "Teleop")
public class MainTeleopBlue extends OpMode {

    private MainTeleop teleop;

    @Override
    public void init() {
        teleop = new MainTeleop(gamepad1, gamepad2, telemetry, hardwareMap, RobotSide.Blue);
        teleop.init();
    }

    @Override
    public void init_loop() {
        teleop.init_loop();
    }

    @Override
    public void start() {
        teleop.start();
    }

    @Override
    public void loop() {
        teleop.loop();
    }

    @Override
    public void stop() {
        teleop.stop();
    }
}
