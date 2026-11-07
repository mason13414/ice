# 13414 BioBuzz

FTC Team 13414 robot code for the BioBuzz season, built on **Pedro Pathing 3**
(`com.pedropathing:revhub:3.0.2`, `com.pedropathing:tuning:1.0.3`) and FTC SDK 12.0.0.

## Robot

- Four drive motors (6000 RPM bare, 9T:108T = 12:1, 104 mm wheels) named
  `leftFront`, `leftBack`, `rightFront`, `rightBack`
- goBILDA Pinpoint odometry, name `pinpoint`
- Intake motor `intake`, feed motor `transfer`, flywheel `flywheel1`
- Turret motor `turret1` (its own encoder)
- Limelight 3A, name `limelight` (AprilTag re-localization)

## Tuning order

1. Build and install, then open `http://192.168.43.1:10158` while connected to the robot.
2. Run **Mecanum** and paste the generated directions into `pedro/Constants.java`.
3. Run **Pinpoint** and paste the directions and offsets into `pedro/Constants.java`.
4. Run **Foresight** and paste the generated config into `pedro/Constants.java`.
5. Run the `Path Following Test` auto (48 in out and back).
6. Fill in the `TODO` values in `Constants.java` and `pedro/Constants.java`.

Everything marked `TODO` is a placeholder: no tuning values were carried over from
the previous robot.
