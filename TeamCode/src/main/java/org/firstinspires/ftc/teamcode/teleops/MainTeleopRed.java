package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.RobotSide;

/** Red alliance teleop; identical behavior, different side for goal/vision logic. */
@TeleOp(name = "Main Teleop Red", group = "Teleop")
public class MainTeleopRed extends OpMode {

    private MainTeleop teleop;

    @Override
    public void init() {
        teleop = new MainTeleop(gamepad1, gamepad2, telemetry, hardwareMap, RobotSide.Red);
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
