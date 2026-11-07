package org.firstinspires.ftc.teamcode.subsystems;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Constants;

/**
 * The intake is now a single motor. Last season's intake also had a CR servo and a
 * beam-break sensor; both are gone, so the code for them was dropped rather than
 * left behind to confuse whoever reads this next season.
 */
public class Intake {

    private final DcMotorEx motor;
    private final DcMotorSimple.Direction direction = DcMotorSimple.Direction.FORWARD; // TODO: confirm on the robot

    public Intake(HardwareMap hardwareMap) {
        motor = hardwareMap.get(DcMotorEx.class, "intake");
        motor.setDirection(direction);
        motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /** Runs the intake inward. */
    public void run() {
        motor.setPower(Constants.Intake.INTAKE_POWER);
    }

    /** Runs it backwards to clear a jam. */
    public void spit() {
        motor.setPower(Constants.Intake.SPIT_POWER);
    }

    public void setPower(double power) {
        motor.setPower(power);
    }

    public void stop() {
        motor.setPower(0);
    }
}
