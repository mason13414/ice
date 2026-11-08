package org.firstinspires.ftc.teamcode.modularAutos.runnableWrappers;

import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.modularAutos.Common;
import org.firstinspires.ftc.teamcode.modularAutos.PathPlanner;
import org.firstinspires.ftc.teamcode.modularAutos.modules.Commands;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

import java.util.ArrayList;
import java.util.List;

/**
 * Structural template, ported from last season's RedClose21.
 * <p>
 * The SHAPE is what was kept: a wrapper OpMode owns a list of steps, builds the
 * current step's paths, runs it, and advances when it reports done, with a hard
 * timeout just before the 30 second mark.
 * <p>
 * The CONTENT could not survive: the old auto queued 21 balls through the indexer
 * and the smart-shoot color logic, and neither exists on this robot. What is left is
 * drive -> spin up -> drive -> spin up, with all poses as TODO placeholders.
 */
@Autonomous(name = "Red Close 21", group = "0Comp", preselectTeleOp = "Main Teleop Red")
public class RedClose21 extends OpMode {

    private final List<PathPlanner> steps = new ArrayList<>();
    private final Timer timer = new Timer();
    private Robot robot;
    private int currentStep = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);

        PathPlanner previous = new Commands.NullPlanner(Common.StartPoses.redCloseOuter);
        previous = add(new Commands.GoToPose(robot, previous, Common.ShootPoses.redMidShoot));
        previous = add(new Commands.Shoot(robot, previous, 1.0));
        previous = add(new Commands.GoToPose(robot, previous, Common.IntakePoses.redSpike1));
        previous = add(new Commands.GoToPose(robot, previous, Common.ShootPoses.redMidShoot));
        add(new Commands.Shoot(robot, previous, 1.0));

        telemetry.addData("auto", "Red Close 21 (template)");
        telemetry.addData("start pose", Common.StartPoses.redCloseOuter);
    }

    private PathPlanner add(PathPlanner step) {
        steps.add(step);
        return step;
    }

    @Override
    public void start() {
        robot.start();
        robot.setPose(Common.StartPoses.redCloseOuter);
        robot.intake.run();
        timer.reset();
        if (!steps.isEmpty()) {
            steps.get(0).buildPaths();
        }
    }

    @Override
    public void loop() {
        if (currentStep >= steps.size() || timer.seconds() > Common.Timings.AUTO_TIMEOUT_SECONDS) {
            robot.stopAll();
            telemetry.addData("done", true);
            telemetry.update();
            return;
        }

        robot.update();

        PathPlanner step = steps.get(currentStep);
        if (step.run()) {
            currentStep++;
            if (currentStep < steps.size()) {
                steps.get(currentStep).buildPaths();
            }
            return;
        }

        telemetry.addData("step", "%d/%d %s", currentStep + 1, steps.size(), step.name());
        telemetry.addData("pose", "x %.1f  y %.1f  heading %.1f deg",
                robot.getPose().x(), robot.getPose().y(), Math.toDegrees(robot.getPose().heading()));
        telemetry.addData("time", "%.1f s", timer.seconds());
        telemetry.update();
    }

    @Override
    public void stop() {
        robot.stopAll();
    }
}
