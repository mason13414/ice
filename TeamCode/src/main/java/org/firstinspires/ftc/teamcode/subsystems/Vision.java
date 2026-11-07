package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Limelight, trimmed to AprilTag re-localization.
 * <p>
 * Last season this class also read the motif and detected balls for the color-sorting
 * indexer. There is no sorter this year, so all of that is gone.
 * <p>
 * The meter -> inch conversion and the 180 degree yaw flip are carried over from last
 * season because they worked; the camera is mounted differently now, so verify them
 * with the robot against a known field position before trusting them.
 */
public class Vision {

    private final Limelight3A limelight;
    private final List<Pose> samples = new ArrayList<>();

    public Vision(HardwareMap hardwareMap) {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0); // TODO: point at the AprilTag pipeline
        limelight.start();
    }

    /** Latest single-tag pose estimate, or null if no tag is visible. */
    public Pose getRobotPose() {
        LLResult result = limelight.getLatestResult();
        if (result == null || !result.isValid()) {
            return null;
        }
        for (LLResultTypes.FiducialResult fiducial : result.getFiducialResults()) {
            return toPedroPose(fiducial.getRobotPoseFieldSpace());
        }
        return null;
    }

    /**
     * Averages several frames so a single noisy tag does not throw the pose.
     * Call once per loop while the re-localize button is held, then read the result.
     */
    public Pose sample() {
        Pose pose = getRobotPose();
        if (pose != null) {
            samples.add(pose);
        }
        return averagedPose();
    }

    public Pose averagedPose() {
        if (samples.isEmpty()) {
            return null;
        }

        double sumX = 0, sumY = 0, sumSin = 0, sumCos = 0;
        for (Pose pose : samples) {
            sumX += pose.x();
            sumY += pose.y();
            sumSin += Math.sin(pose.heading());
            sumCos += Math.cos(pose.heading());
        }

        int count = samples.size();
        double heading = Math.atan2(sumSin / count, sumCos / count);
        return new Pose(sumX / count, sumY / count, heading);
    }

    public boolean hasEnoughSamples() {
        return samples.size() >= Constants.Vision.RELOCALIZE_SAMPLES;
    }

    public void clearSamples() {
        samples.clear();
    }

    /**
     * Hive-tip detection is auto-only and the pipeline does not exist yet.
     * Kept as a clearly marked stub so the auto code can call it later.
     */
    public Pose getHiveTipPose() {
        // TODO: implement once the hive-tip Limelight pipeline exists.
        return null;
    }

    public void stop() {
        limelight.stop();
    }

    private static Pose toPedroPose(Pose3D fieldSpacePose) {
        double x = -fieldSpacePose.getPosition().x * 39.3701 + Constants.Vision.TAG_POSE_OFFSET.x();
        double y = -fieldSpacePose.getPosition().y * 39.3701 + Constants.Vision.TAG_POSE_OFFSET.y();
        double heading = Math.toRadians(fieldSpacePose.getOrientation().getYaw() - 180)
                + Constants.Vision.TAG_POSE_OFFSET.heading();
        return new Pose(x, y, heading);
    }
}
