package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

/**
 * Pedro Pathing 3 configuration.
 * <p>
 * This file is deliberately small: in Pedro 3 the drivetrain only needs motor names
 * and directions, the localizer only needs the sensor name plus pod offsets, and the
 * Foresight algorithm is tuned on the robot with AutoTune instead of by hand.
 * <p>
 * Everything labelled TODO is a placeholder. Run the Mecanum, Pinpoint and Foresight
 * procedures in AutoTune (http://192.168.43.1:10158) and paste the generated config
 * blocks over these.
 */
public class Constants {

    /** Motor names are unchanged from last season. */
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.backLeftName.set("leftBack");
        c.frontRightName.set("rightFront");
        c.backRightName.set("rightBack");

        // TODO(AutoTune): the Mecanum procedure spins each wheel and generates the four
        // directions below. FORWARD is a placeholder, not a guess at your wiring.
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);

        // Holds the wheels when the sticks are released in teleop.
        c.manualBrakeMode.set(true);
    });

    /** Pinpoint is still the localizer; its mounting on the new robot is unknown. */
    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");

        // TODO: measure both pod offsets from the robot's center of rotation.
        c.xPodOffset.set(0.0);
        c.yPodOffset.set(0.0);

        // TODO(AutoTune): the Pinpoint procedure works these two directions out for you.
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);

        // TODO: confirm the odometry pod type actually on the robot (4-bar is the common one).
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    /**
     * Foresight = the path-following algorithm that replaces Pedro 2's predictive braking.
     * The controllers below are generic starting points; AutoTune replaces every number.
     */
    public static ForesightConfig foresightConfig = new ForesightConfig(c -> {
        Controller primaryTranslationalForward = Controller.proportional(0.3);
        Controller secondaryTranslationalForward = Controller.proportional(0.1);
        Controller primaryTranslationalLateral = Controller.proportional(0.3);
        Controller secondaryTranslationalLateral = Controller.proportional(0.1);

        c.forwardTranslational.set(
                Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
        c.strafeTranslational.set(
                Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

        c.coast.set(Controller.proportionalFeedforward(0.01));
        c.brake.set(Controller.proportionalFeedforward(0.01));
        c.headingFeedback.set(Controller.proportional(1.0));

        // TODO(AutoTune): braking coefficients and heading feedforward come from Foresight tuning.
        c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05, 0.005));
        c.linearBrakeCoefficients.set(Matrix.diag(0.1, 0.1));
        c.quadraticBrakeCoefficients.set(Matrix.diag(0.001, 0.001));

        // TODO(AutoTune): measured top speeds and natural decelerations.
        c.maxAchievableForwardVelocity.set(100.0);
        c.maxAchievableStrafeVelocity.set(70.0);
        c.naturalForwardDeceleration.set(80.0);
        c.naturalStrafeDeceleration.set(80.0);
    });

    /** The one object the whole robot talks to for pose and path following. */
    public static Follower create(HardwareMap hardwareMap) {
        return new Follower(
                new PinpointLocalizer(hardwareMap, localizerConfig),
                new Mecanum(hardwareMap, drivetrainConfig),
                new Foresight(foresightConfig));
    }

    /** Used by the AutoTune procedures, which need the drivetrain on its own. */
    public static Mecanum drivetrain(HardwareMap hardwareMap) {
        return new Mecanum(hardwareMap, drivetrainConfig);
    }

    /** Used by the AutoTune procedures, which need the localizer on its own. */
    public static PinpointLocalizer localizer(HardwareMap hardwareMap) {
        return new PinpointLocalizer(hardwareMap, localizerConfig);
    }
}
