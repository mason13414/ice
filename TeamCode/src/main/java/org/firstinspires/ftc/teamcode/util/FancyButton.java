package org.firstinspires.ftc.teamcode.util;

/**
 * Turns a raw gamepad boolean into useful events.
 * <p>
 * Ported from last season, but trimmed to the two behaviors the new teleop uses:
 * a toggle (press flips isOn) and a long press (startPress/endPress while held).
 */
public class FancyButton {

    public enum PressType {
        Toggle,
        LongPress
    }

    private final PressType pressType;

    /** True while the button is toggled on (Toggle) or currently held (LongPress). */
    public boolean isOn = false;
    /** True for one loop when the button goes down. */
    public boolean startPress = false;
    /** True for one loop when the button comes back up. */
    public boolean endPress = false;
    /** True while the button is down. */
    public boolean isPressed = false;

    private boolean lastState = false;

    public FancyButton(PressType pressType) {
        this.pressType = pressType;
    }

    /**
     * Call this exactly once per loop with the current button state.
     * The work happens on the rising edge only, so the caller can read the flags
     * for the rest of the loop.
     */
    public void checkStatus(boolean pressed) {
        startPress = pressed && !lastState;
        endPress = !pressed && lastState;
        isPressed = pressed;

        if (pressType == PressType.Toggle) {
            if (startPress) {
                isOn = !isOn;
            }
        } else {
            isOn = pressed;
        }

        lastState = pressed;
    }
}
