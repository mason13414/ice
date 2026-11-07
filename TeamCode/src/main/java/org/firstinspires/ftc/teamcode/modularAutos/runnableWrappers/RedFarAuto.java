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
 * Far-side auto: the same step list as RedClose21, but starting from the far
 * starting position and using the far shooting/intake poses.
 * <p>
 * This is deliberately a copy of the RedClose21 skeleton rather than a shared base
 * class: each auto will grow its own sequence this season, and a shared base class
 * would have to be unpicked the first time one of them diverges. All poses are
 * TODO placeholders until they are measured on the BioBuzz field.
 */
@Autonomous(name = "Red Far", group = "0Comp", preselectTeleOp = "Main Teleop Red")
public class RedFarAuto extends OpMode {

    private final List<PathPlanner> steps = new ArrayList<>();
    private final Timer timer = new Timer();
    private Robot robot;
    private int currentStep = 0;

    @Override
    public void init() {
        robot = new Robot(hardwareMap, telemetry);

        PathPlanner previous = new Commands.NullPlanner(Common.StartPoses.redFarOuter);
        previous = add(new Commands.GoToPose(robot, previous, Common.ShootPoses.redFarShoot));
        previous = add(new Commands.Shoot(robot, previous, 1.0));
        previous = add(new Commands.GoToPose(robot, previous, Common.IntakePoses.redFarSpike));
        previous = add(new Commands.GoToPose(robot, previous, Common.ShootPoses.redFarShoot));
        previous = add(new Commands.Shoot(robot, previous, 1.0));
        previous = add(new Commands.GoToPose(robot, previous, Common.IntakePoses.redFarSpike));
        add(new Commands.GoToPose(robot, previous, Common.ShootPoses.redFarShoot));

        telemetry.addData("auto", "Red Far");
        telemetry.addData("start pose", Common.StartPoses.redFarOuter);
    }

    private PathPlanner add(PathPlanner step) {
        steps.add(step);
        return step;
    }

    @Override
    public void start() {
        robot.start();
        robot.setPose(Common.StartPoses.redFarOuter);
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
