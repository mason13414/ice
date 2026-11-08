package org.firstinspires.ftc.teamcode.autos;

import com.pedropathing.api.Paths;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * The first thing to run on the new robot: drive 48 inches forward and back.
 * If this works, the drivetrain, localizer and Foresight are all roughly right.
 */
@Autonomous(name = "Path Following Test", group = "Test")
public class PathFollowingTest extends OpMode {

    private static final double DISTANCE_IN = 48.0;

    private Follower follower;
    private Path forward;
    private Path backward;
    private boolean goingForward = true;

    @Override
    public void init() {
        follower = Constants.create(hardwareMap);

        Pose start = Pose.zero();
        Pose end = new Pose(DISTANCE_IN, 0, 0);

        // A straight line with the heading held at 0 radians.
        forward = Paths.line(start, end).constant(0);
        backward = Paths.line(end, start).constant(0);

        follower.setPose(start);
    }

    @Override
    public void start() {
        follower.follow(forward);
    }

    @Override
    public void loop() {
        follower.update();

        if (follower.atParametricEnd()) {
            goingForward = !goingForward;
            follower.follow(goingForward ? forward : backward);
        }

        telemetry.addData("pose", "x %.1f  y %.1f  heading %.1f deg",
                follower.pose().x(), follower.pose().y(), Math.toDegrees(follower.pose().heading()));
        telemetry.addData("following", follower.following());
        telemetry.update();
    }
}
