package org.firstinspires.ftc.teamcode.modularAutos;

import com.pedropathing.math.Pose;

/**
 * One step of an autonomous routine.
 * <p>
 * This is the interface from last season's modular auto system, kept because the
 * structure is robot-independent: a wrapper OpMode holds a list of these, builds each
 * step's paths when it becomes current, and advances when run() returns true.
 */
public interface PathPlanner {

    /** Build the paths for this step. Called once, just before the step runs. */
    void buildPaths();

    /** Run the step; return true when it is finished. */
    boolean run();

    /** Best estimate of where the robot will be when this step ends. */
    Pose getEndPoseEst();

    /** For telemetry and debugging. */
    String name();
}
