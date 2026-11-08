package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Constants;

/**
 * The one object teleop and autos talk to.
 * <p>
 * Last season this class was a 1077-line hub that also ran the indexer, the color
 * queue, the climb, the LEDs and the shot solver. Everything that had no hardware
 * this year was removed; what is left wires the follower, intake, shooter, turret
 * and vision together.
 */
public class Robot {

    public final Follower follower;
    public final Intake intake;
    public final Feed feed;
    public final Shooter shooter;
    public final Turret turret;
    public final Vision vision;

    private final Telemetry telemetry;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.follower = org.firstinspires.ftc.teamcode.pedro.Constants.create(hardwareMap);
        this.intake = new Intake(hardwareMap);
        this.feed = new Feed(hardwareMap);
        this.shooter = new Shooter(hardwareMap);
        this.turret = new Turret(hardwareMap);
        this.vision = new Vision(hardwareMap);
    }

    /**
     * Call once when the OpMode starts. The pose is NOT set here: autos set their own
     * starting pose and teleop re-zeroes from vision, so setting it here would fight both.
     */
    public void start() {
        turret.zero();
    }

    /** Call once per loop. Order matters: pose first, then anything that uses it. */
    public void update() {
        follower.update();

        if (Constants.Field.GOAL_CONFIGURED) {
            turret.aimAt(follower.pose(), Constants.Field.GOAL);
        }
        turret.update();
    }

    public Pose getPose() {
        return follower.pose();
    }

    public void setPose(Pose pose) {
        follower.setPose(pose);
    }

    /** Robot-centric drive, matching how the robot was driven last season. */
    public void drive(double forward, double lateral, double turn) {
        follower.manual(forward, lateral, turn);
    }

    public double distanceToGoal() {
        Pose pose = follower.pose();
        return Math.hypot(Constants.Field.GOAL.x() - pose.x(), Constants.Field.GOAL.y() - pose.y());
    }

    /**
     * Picks a flywheel speed for the current distance.
     * The distance -> RPM table has not been measured yet, so this does nothing
     * until Constants.Shooter.NEAR_RPM / FAR_RPM are filled in.
     */
    public void spinUpForDistance() {
        double near = Constants.Shooter.NEAR_RPM;
        double far = Constants.Shooter.FAR_RPM;
        if (near <= 0 && far <= 0) {
            return; // TODO: build the shot table on the real robot
        }

        double distance = distanceToGoal();
        double t = clamp((distance - 24.0) / (110.0 - 24.0), 0, 1);
        shooter.setTargetRpm(near + t * (far - near));
    }

    public void stopShooter() {
        shooter.stop();
    }

    /** Runs the feed motor while the flywheel is up to speed. */
    public void feed() {
        feed.run();
    }

    public void stopFeed() {
        feed.stop();
    }

    /** True when the flywheel and turret are both where they need to be. */
    public boolean readyToShoot() {
        return shooter.atSpeed() && turret.atTarget();
    }

    /**
     * Re-zero the pose from AprilTags. Call every loop while the button is held;
     * once enough frames are collected the averaged pose is applied.
     */
    public void relocalizeFromVision() {
        vision.sample();
        if (vision.hasEnoughSamples()) {
            Pose averaged = vision.averagedPose();
            if (averaged != null) {
                follower.setPose(averaged);
            }
            vision.clearSamples();
        }
    }

    public void stopAll() {
        intake.stop();
        feed.stop();
        shooter.stop();
        turret.stop();
        vision.stop();
        follower.stop();
    }

    /** Convenience for teleop telemetry and for AutoTune-style debugging. */
    public Telemetry getTelemetry() {
        return telemetry;
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
