package org.firstinspires.ftc.teamcode;

import com.pedropathing.math.Pose;

/**
 * Robot-specific numbers that are NOT part of Pedro Pathing.
 * <p>
 * Every value here is a clean default for the brand-new BioBuzz robot. None of last
 * season's tuned numbers were copied over, because the robot, motors and mechanisms
 * are all different. Anything that must be measured on the real robot is marked TODO.
 */
public class Constants {

    public static class Drivetrain {
        /**
         * Four 6000 RPM bare motors, 9T -> 108T (12:1), 104 mm wheels.
         * 500 RPM output * (pi * 104 mm) = about 107 in/s of free speed.
         * This is only documentation: Pedro 3 measures the real value during AutoTune.
         */
        public static final double OUTPUT_RPM = 500.0;
        public static final double GEAR_RATIO = 108.0 / 9.0;
        public static final double WHEEL_DIAMETER_MM = 104.0;
        public static final double FREE_SPEED_IN_PER_SEC = 107.0; // TODO: confirm with AutoTune

        /** Driver slow mode. 1.0 is full speed. */
        public static final double NOT_TURBO_POWER = 0.6;
        public static final double TURBO_POWER = 1.0;
    }

    public static class Intake {
        /** 1.0 runs the intake in, negative spits out. TODO: tune on the robot. */
        public static final double INTAKE_POWER = 1.0;
        public static final double SPIT_POWER = -0.7;
    }

    public static class Feed {
        /** Pushes the game element into the flywheel. TODO: tune on the robot. */
        public static final double FEED_POWER = 1.0;
        public static final double SPIT_POWER = -1.0;
    }

    public static class Shooter {
        /**
         * Encoder counts per revolution of the flywheel motor shaft.
         * 28 is the goBILDA 5203 value; TODO: confirm which motor is on the flywheel.
         */
        public static final double TICKS_PER_REV = 28.0;

        /** Flywheel speed while shooting. TODO: build the distance -> RPM table on the robot. */
        public static final double NEAR_RPM = 0.0;
        public static final double FAR_RPM = 0.0;

        /** How close the flywheel must be to its target before we trust a shot. */
        public static final double RPM_TOLERANCE = 75.0;

        /** The hood is fixed this year (no hood servo), so this is only documentation. */
        public static final double FIXED_HOOD_ANGLE_DEG = 0.0; // TODO: measure the real hood angle
    }

    public static class Turret {
        /** Encoder counts per revolution of the turret motor shaft. TODO: confirm motor. */
        public static final double TICKS_PER_REV = 28.0;
        /** Motor revolutions per turret revolution. TODO: measure the real gear ratio. */
        public static final double GEAR_RATIO = 1.0;

        /** Simple proportional gain. TODO: tune on the robot. */
        public static final double KP = 0.02;
        /** Minimum power to break friction. TODO: tune on the robot. */
        public static final double KS = 0.0;

        public static final double MAX_POWER = 0.5;
        /** Stop correcting inside this many degrees. */
        public static final double TOLERANCE_DEG = 0.75;

        /** Software limit so a mis-aimed target cannot wrap the turret. TODO: measure limits. */
        public static final double MIN_ANGLE_DEG = -180.0;
        public static final double MAX_ANGLE_DEG = 180.0;
    }

    public static class Field {
        /**
         * Where the turret should aim, in Pedro field coordinates (inches, blue-origin).
         * TODO: set the real BioBuzz goal position; aiming is skipped until then.
         */
        public static final Pose GOAL = Pose.zero();
        public static final boolean GOAL_CONFIGURED = false;
    }

    public static class Vision {
        /**
         * Extra offset applied to the AprilTag pose if the camera is not where last
         * season's camera was. TODO: measure on the new robot.
         */
        public static final Pose TAG_POSE_OFFSET = new Pose(0, 0);
        /** How many camera frames to average when re-localizing. */
        public static final int RELOCALIZE_SAMPLES = 10;
    }
}
