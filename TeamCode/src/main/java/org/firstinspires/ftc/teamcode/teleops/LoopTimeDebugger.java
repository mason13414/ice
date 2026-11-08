package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Kept from last season because it is the quickest way to find a slow loop.
 * It does not touch any hardware, so it works even while the robot is half built.
 */
@TeleOp(name = "Loop Time Debugger", group = "Debug")
public class LoopTimeDebugger extends OpMode {

    private long lastNanos;
    private double minMs = Double.MAX_VALUE;
    private double maxMs = 0;
    private double sumMs = 0;
    private int frames = 0;

    @Override
    public void init() {
        lastNanos = System.nanoTime();
    }

    @Override
    public void loop() {
        long now = System.nanoTime();
        double ms = (now - lastNanos) / 1e6;
        lastNanos = now;

        minMs = Math.min(minMs, ms);
        maxMs = Math.max(maxMs, ms);
        sumMs += ms;
        frames++;

        telemetry.addData("loop (ms)", "%.2f", ms);
        telemetry.addData("min", "%.2f", minMs);
        telemetry.addData("max", "%.2f", maxMs);
        telemetry.addData("avg", "%.2f", sumMs / frames);
        telemetry.addData("samples", frames);
        telemetry.update();
    }
}
