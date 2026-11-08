package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.subsystems.Robot;
import org.firstinspires.ftc.teamcode.util.FancyButton;
import org.firstinspires.ftc.teamcode.util.RobotSide;

/**
 * The one teleop implementation; the Blue and Red OpModes are thin wrappers around it.
 * <p>
 * Trimmed from last season: field-centric aiming, smart-shoot color queueing, the
 * indexer, climb, lights and panic-shoot are gone because that hardware is gone.
 * What remains is drive, intake, flywheel and turret, plus a vision re-zero button.
 */
public class MainTeleop {

    private final Gamepad gamepad1;
    private final Gamepad gamepad2;
    private final Telemetry telemetry;
    private final HardwareMap hardwareMap;
    private final RobotSide robotSide;

    private Robot robot;

    private FancyButton intakeButton;
    private FancyButton shootButton;
    private FancyButton relocalizeButton;
    private FancyButton zeroTurretButton;

    private final ElapsedTime timer = new ElapsedTime();

    public MainTeleop(Gamepad gamepad1, Gamepad gamepad2, Telemetry telemetry,
                      HardwareMap hardwareMap, RobotSide robotSide) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        this.telemetry = telemetry;
        this.hardwareMap = hardwareMap;
        this.robotSide = robotSide;
    }

    public void init() {
        robot = new Robot(hardwareMap, telemetry);

        intakeButton = new FancyButton(FancyButton.PressType.Toggle);
        shootButton = new FancyButton(FancyButton.PressType.Toggle);
        relocalizeButton = new FancyButton(FancyButton.PressType.LongPress);
        zeroTurretButton = new FancyButton(FancyButton.PressType.LongPress);
    }

    public void init_loop() {
        telemetry.addData("Alliance", robotSide);
        telemetry.addData("Status", "Waiting for start");
        telemetry.update();
    }

    public void start() {
        robot.start();
        timer.reset();
    }

    public void loop() {
        intakeButton.checkStatus(gamepad2.left_bumper);
        shootButton.checkStatus(gamepad2.right_bumper);
        relocalizeButton.checkStatus(gamepad1.back);
        zeroTurretButton.checkStatus(gamepad2.y);

        // Robot-centric drive: the same behavior as last season, expressed through
        // Pedro 3's manual mode instead of hand-mixed motor powers.
        // If strafing is inverted on the real robot, flip the left_stick_x sign.
        double forward = -gamepad1.left_stick_y;
        double lateral = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;
        robot.drive(forward, lateral, turn);

        if (intakeButton.isOn) {
            robot.intake.run();
        } else {
            robot.intake.stop();
        }

        if (gamepad2.left_trigger_pressed) {
            robot.intake.spit();
        }

        if (zeroTurretButton.startPress) {
            robot.turret.zero();
        }

        if (shootButton.isOn) {
            robot.spinUpForDistance();
            // Right trigger feeds once the wheel is spinning; holding it too early
            // would just jam the element against a slow flywheel.
            if (gamepad2.right_trigger_pressed) {
                robot.feed();
            } else {
                robot.stopFeed();
            }
        } else if (shootButton.endPress) {
            robot.stopShooter();
            robot.stopFeed();
        }

        if (relocalizeButton.isPressed) {
            robot.relocalizeFromVision();
        } else if (relocalizeButton.endPress) {
            robot.vision.clearSamples();
        }

        robot.update();

        telemetry.addData("pose", "x %.1f  y %.1f  heading %.1f deg",
                robot.getPose().x(), robot.getPose().y(), Math.toDegrees(robot.getPose().heading()));
        telemetry.addData("distance to goal", "%.1f in", robot.distanceToGoal());
        telemetry.addData("flywheel", "%.0f / %.0f rpm", robot.shooter.getRpm(), robot.shooter.getTargetRpm());
        telemetry.addData("turret", "%.1f / %.1f deg", robot.turret.getAngleDeg(), robot.turret.getTargetDeg());
        telemetry.addData("ready to shoot", robot.readyToShoot());
        telemetry.addData("intake", intakeButton.isOn);
        telemetry.addData("shoot mode", shootButton.isOn);
        telemetry.addData("feeding", gamepad2.right_trigger_pressed && shootButton.isOn);
        telemetry.addData("loop time (ms)", "%.1f", timer.milliseconds());
        telemetry.update();
        timer.reset();
    }

    public void stop() {
        robot.stopAll();
    }
}
