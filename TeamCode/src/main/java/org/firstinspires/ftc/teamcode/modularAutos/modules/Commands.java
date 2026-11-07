package org.firstinspires.ftc.teamcode.modularAutos.modules;

import androidx.annotation.NonNull;

import com.pedropathing.api.Paths;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.pedropathing.utils.Timer;

import org.firstinspires.ftc.teamcode.modularAutos.PathPlanner;
import org.firstinspires.ftc.teamcode.subsystems.Robot;

/**
 * Small reusable auto steps, ported from last season's Commands class.
 * The state-machine shape is unchanged; only the Pedro calls are v3.
 */
public class Commands {

    /** A do-nothing step used as the "previous" step for the first real one. */
    public static class NullPlanner implements PathPlanner {
        private final Pose pose;

        public NullPlanner(Pose pose) {
            this.pose = pose;
        }

        @Override
        public void buildPaths() {
        }

        @Override
        public boolean run() {
            return false;
        }

        @Override
        public Pose getEndPoseEst() {
            return pose;
        }

        @NonNull
        @Override
        public String name() {
            return "Null";
        }
    }

    /** Just waits. Useful for letting a mechanism settle. */
    public static class WaitSeconds implements PathPlanner {
        private final Pose startPose;
        private final double waitSeconds;
        private final Timer timer = new Timer();
        private boolean started = false;
        private boolean finished = false;

        public WaitSeconds(PathPlanner previous, double waitSeconds) {
            this.startPose = previous.getEndPoseEst();
            this.waitSeconds = waitSeconds;
        }

        @Override
        public void buildPaths() {
        }

        @Override
        public boolean run() {
            if (!started) {
                started = true;
                timer.reset();
            }
            if (timer.seconds() >= waitSeconds) {
                finished = true;
            }
            return finished;
        }

        @Override
        public Pose getEndPoseEst() {
            return startPose;
        }

        @NonNull
        @Override
        public String name() {
            return "Wait " + waitSeconds + "s";
        }
    }

    /** Drives a straight line to a pose and waits for the follower to finish. */
    public static class GoToPose implements PathPlanner {
        private final Robot robot;
        private final Pose startPose;
        private final Pose endPose;
        private final Timer settleTimer = new Timer();
        private Path path;
        private int state = 0;

        public GoToPose(Robot robot, PathPlanner previous, Pose endPose) {
            this.robot = robot;
            this.startPose = previous.getEndPoseEst();
            this.endPose = endPose;
        }

        @Override
        public void buildPaths() {
            // Straight line, heading interpolating from the start pose to the end pose.
            path = Paths.line(startPose, endPose).linear(startPose, endPose);
        }

        @Override
        public boolean run() {
            switch (state) {
                case 0:
                    robot.follower.follow(path);
                    state = 1;
                    break;
                case 1:
                    if (!robot.follower.isBusy()) {
                        settleTimer.reset();
                        state = 2;
                    }
                    break;
                case 2:
                    // A short settle avoids the next step starting on residual motion.
                    if (settleTimer.seconds() > 0.15) {
                        return true;
                    }
                    break;
            }
            return false;
        }

        @Override
        public Pose getEndPoseEst() {
            return endPose;
        }

        @NonNull
        @Override
        public String name() {
            return "GoTo " + endPose;
        }
    }

    /** Spins up, feeds for a fixed time, then stops feeding. */
    public static class Shoot implements PathPlanner {
        private final Robot robot;
        private final Pose startPose;
        private final double feedSeconds;
        private final Timer timer = new Timer();
        private final Timer feedTimer = new Timer();
        private boolean started = false;
        private boolean feeding = false;

        public Shoot(Robot robot, PathPlanner previous, double feedSeconds) {
            this.robot = robot;
            this.startPose = previous.getEndPoseEst();
            this.feedSeconds = feedSeconds;
        }

        @Override
        public void buildPaths() {
        }

        @Override
        public boolean run() {
            if (!started) {
                started = true;
                timer.reset();
                robot.spinUpForDistance();
            }

            // Wait for the flywheel (or a 2 second grace period), then feed.
            if (!feeding && (robot.shooter.atSpeed()
                    || timer.seconds() >= org.firstinspires.ftc.teamcode.modularAutos.Common.Timings.SHOOTER_SPIN_UP_SECONDS)) {
                feeding = true;
                feedTimer.reset();
                robot.feed();
            }

            if (feeding && feedTimer.seconds() >= feedSeconds) {
                robot.stopFeed();
                return true;
            }
            return false;
        }

        @Override
        public Pose getEndPoseEst() {
            return startPose;
        }

        @NonNull
        @Override
        public String name() {
            return "Shoot for " + feedSeconds + "s";
        }
    }

    /** Spins the flywheel up and waits until it is ready (or the timeout expires). */
    public static class SpinUpShooter implements PathPlanner {
        private final Robot robot;
        private final Pose startPose;
        private final Timer timer = new Timer();
        private boolean started = false;

        public SpinUpShooter(Robot robot, PathPlanner previous) {
            this.robot = robot;
            this.startPose = previous.getEndPoseEst();
        }

        @Override
        public void buildPaths() {
        }

        @Override
        public boolean run() {
            if (!started) {
                started = true;
                timer.reset();
                robot.spinUpForDistance();
            }

            // Either the wheel is ready, or we gave it long enough that waiting more
            // would only cost auto time.
            return robot.readyToShoot()
                    || timer.seconds() >= org.firstinspires.ftc.teamcode.modularAutos.Common.Timings.SHOOTER_SPIN_UP_SECONDS;
        }

        @Override
        public Pose getEndPoseEst() {
            return startPose;
        }

        @NonNull
        @Override
        public String name() {
            return "Spin up shooter";
        }
    }

}
