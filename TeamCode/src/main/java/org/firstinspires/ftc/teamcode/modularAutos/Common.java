package org.firstinspires.ftc.teamcode.modularAutos;

import com.pedropathing.math.Pose;

/**
 * Shared poses and timings for the modular autos.
 * <p>
 * ALL of these are placeholders for the BioBuzz field and the new robot. They are
 * written on the blue side and mirrored for red once the real positions are known.
 * Do not trust any of them until they are measured on the field.
 */
public class Common {

    public static class StartPoses {
        // TODO: real BioBuzz starting positions, in inches, blue-origin.
        public static final Pose blueCloseOuter = new Pose(0, 0, 0);
        public static final Pose blueFarOuter = new Pose(0, 0, 0);
        public static final Pose redCloseOuter = new Pose(0, 0, 0);
        public static final Pose redFarOuter = new Pose(0, 0, 0);
    }

    public static class ShootPoses {
        // TODO: the positions the robot shoots from.
        public static final Pose blueMidShoot = new Pose(0, 0, 0);
        public static final Pose redMidShoot = new Pose(0, 0, 0);
        public static final Pose redFarShoot = new Pose(0, 0, 0);
    }

    public static class IntakePoses {
        // TODO: where the robot picks game elements up.
        public static final Pose blueSpike1 = new Pose(0, 0, 0);
        public static final Pose redSpike1 = new Pose(0, 0, 0);
        public static final Pose redFarSpike = new Pose(0, 0, 0);
    }

    public static class Timings {
        /** How long the flywheel gets to reach speed before we stop waiting. */
        public static final double SHOOTER_SPIN_UP_SECONDS = 2.0;
        /** Hard stop so an auto can never overrun the 30 second period. */
        public static final double AUTO_TIMEOUT_SECONDS = 29.0;
    }
}
