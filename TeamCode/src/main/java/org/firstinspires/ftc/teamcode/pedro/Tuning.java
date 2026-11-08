package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;

import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.PinpointTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests;

/**
 * The robot-hosted tuning menu.
 * <p>
 * Last season this class held ~19 hand-written tuning OpModes. Pedro 3 replaces all of
 * them with AutoTune: build and install this, then open http://192.168.43.1:10158 on a
 * browser connected to the robot's Wi-Fi and pick a procedure.
 */
public class Tuning {

    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

    @Tuner
    public static Procedure pinpointTuner() {
        return new PinpointTuner();
    }

    @Tuner
    public static Procedure foresightTuner() {
        return new ForesightTuner(Constants::localizer, Constants::drivetrain);
    }

    @Tuner
    public static Procedure tests() {
        return new Tests(Constants::drivetrain, Constants::localizer,
                () -> new Foresight(Constants.foresightConfig));
    }
}
