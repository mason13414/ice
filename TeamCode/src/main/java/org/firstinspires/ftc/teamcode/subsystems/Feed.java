package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * The feed/transfer motor that pushes game elements into the flywheel.
 * <p>
 * The hardware name is "transfer", the same name last season's code used. Note that
 * last season the turret encoder was read off this motor as a hack; now that the
 * turret has its own motor encoder, "transfer" is a real feed motor again.
 */
public class Feed {

    private final DcMotorEx motor;

    public Feed(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "transfer");
        motor.setDirection(DcMotorSimple.Direction.FORWARD); // TODO: confirm feed direction
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /** Pushes a game element toward the flywheel. */
    public void run() {
        motor.setPower(Constants.Feed.FEED_POWER);
    }

    /** Runs backwards to clear a jam. */
    public void spit() {
        motor.setPower(Constants.Feed.SPIT_POWER);
    }

    public void stop() {
        motor.setPower(0);
    }
}
