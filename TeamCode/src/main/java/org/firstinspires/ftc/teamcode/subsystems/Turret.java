package org.firstinspires.ftc.teamcode.subsystems;

import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * Turret driven by a single motor this year (last year it was two CR servos).
 * <p>
 * Because it is a motor, our own motor encoder is the angle sensor. Last season's
 * angle came from the transfer motor's encoder, which no longer exists — this is the
 * one hardware change that genuinely required rewriting a subsystem.
 */
public class Turret {

    private final DcMotorEx motor;

    /** Ticks read at the moment the turret was last zeroed against its hard stop. */
    private double offsetTicks = 0;
    private double targetDeg = 0;

    public Turret(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "turret1");
        motor.setDirection(DcMotorSimple.Direction.FORWARD); // TODO: confirm which way is positive
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /** Call with the turret resting against its hard stop: that position becomes 0 degrees. */
    public void zero() {
        offsetTicks = motor.getCurrentPosition();
        targetDeg = 0;
        motor.setPower(0);
    }

    public double getTicks() {
        return motor.getCurrentPosition() - offsetTicks;
    }

    public double getAngleDeg() {
        double revolutions = getTicks() / (Constants.Turret.TICKS_PER_REV * Constants.Turret.GEAR_RATIO);
        return revolutions * 360.0;
    }

    public void setTargetDeg(double deg) {
        targetDeg = clamp(deg, Constants.Turret.MIN_ANGLE_DEG, Constants.Turret.MAX_ANGLE_DEG);
    }

    public double getTargetDeg() {
        return targetDeg;
    }

    /** Run once per loop, after the pose is up to date. */
    public void update() {
        double error = targetDeg - getAngleDeg();

        if (Math.abs(error) < Constants.Turret.TOLERANCE_DEG) {
            motor.setPower(0);
            return;
        }

        double power = Constants.Turret.KP * error + Math.signum(error) * Constants.Turret.KS;
        motor.setPower(clamp(power, -Constants.Turret.MAX_POWER, Constants.Turret.MAX_POWER));
    }

    public boolean atTarget() {
        return Math.abs(targetDeg - getAngleDeg()) <= Constants.Turret.TOLERANCE_DEG;
    }

    /**
     * Aims at a field position. The turret angle is relative to the robot, so the
     * robot's heading is subtracted from the field angle to the target.
     */
    public void aimAt(Pose robotPose, Pose targetPose) {
        double dx = targetPose.x() - robotPose.x();
        double dy = targetPose.y() - robotPose.y();
        double fieldAngleDeg = Math.toDegrees(Math.atan2(dy, dx));
        double robotHeadingDeg = Math.toDegrees(robotPose.heading());
        setTargetDeg(fieldAngleDeg - robotHeadingDeg);
    }

    public void stop() {
        motor.setPower(0);
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
