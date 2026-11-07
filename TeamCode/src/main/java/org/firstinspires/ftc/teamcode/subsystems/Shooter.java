package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * One flywheel, no second flywheel and no hood servo this year.
 * <p>
 * The old shooter ran a hand-written PIDF controller on top of Pedro 2 internals.
 * That library is gone, and the FTC SDK's built-in velocity loop (RUN_USING_ENCODER
 * + setVelocity) does the same job, so the code got a lot smaller.
 */
public class Shooter {

    private final DcMotorEx flywheel;
    private double targetRpm = 0;

    public Shooter(HardwareMap hardwareMap) {
        flywheel = hardwareMap.get(DcMotorEx.class, "flywheel1");
        flywheel.setDirection(DcMotorSimple.Direction.REVERSE); // TODO: confirm spin direction
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    /** @param rpm flywheel speed in RPM; 0 stops the wheel. */
    public void setTargetRpm(double rpm) {
        targetRpm = Math.max(0, rpm);
        if (targetRpm == 0) {
            flywheel.setPower(0);
            return;
        }
        flywheel.setVelocity(rpmToTicksPerSecond(targetRpm));
    }

    public void stop() {
        targetRpm = 0;
        flywheel.setPower(0);
    }

    public double getTargetRpm() {
        return targetRpm;
    }

    public double getRpm() {
        return flywheel.getVelocity() * 60.0 / Constants.Shooter.TICKS_PER_REV;
    }

    /** True when the wheel is spinning at (or very near) the requested speed. */
    public boolean atSpeed() {
        return targetRpm > 0 && Math.abs(getRpm() - targetRpm) <= Constants.Shooter.RPM_TOLERANCE;
    }

    private static double rpmToTicksPerSecond(double rpm) {
        return rpm * Constants.Shooter.TICKS_PER_REV / 60.0;
    }
}
