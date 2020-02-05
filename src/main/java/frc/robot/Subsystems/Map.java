package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * All Robot Objects declared here - no logic, just declarations
 */
public class Map {

    // Axis IDS

    public static final int X_AXIS = -1;
    public static final int Y_AXIS = -1;

    // Robot Objects

    private Spark frontLeft;
    private Spark frontRight;
    private Spark backLeft;
    private Spark backRight;

    private SpeedControllerGroup leftTrain;
    private SpeedControllerGroup rightTrain;

    private DifferentialDrive chassis;

    private Spark intake;
    private Spark drawbridge;

    // Robot Object IDs

    private final int FRONT_LEFT_ID = -1;
    private final int FRONT_RIGHT_ID = -1;
    private final int BACK_LEFT_ID = -1;
    private final int BACK_RIGHT_ID = -1;

    private final int INTAKE_ID = -1;
    
    private final int DRAWBRIDGE_ID = -1;

    // Joystick Object and ID

    private XboxController controller;

    private final int CONTROLLER_ID = -1;

    // Ball counter

    private DigitalInput ballLimitSwitch;

    private final int BALL_SWTICH_ID = -1;

    // Spinner

    private Spinner spinner;

    public Map() {

        frontLeft = new Spark(FRONT_LEFT_ID);
        frontRight = new Spark(FRONT_RIGHT_ID);
        backLeft = new Spark(BACK_LEFT_ID);
        backRight = new Spark(BACK_RIGHT_ID);

        leftTrain = new SpeedControllerGroup(frontLeft, backLeft);
        rightTrain = new SpeedControllerGroup(frontRight, backRight);

        chassis = new DifferentialDrive(leftTrain, rightTrain);

        intake = new Spark(INTAKE_ID);

        drawbridge = new Spark(DRAWBRIDGE_ID);

        controller = new XboxController(CONTROLLER_ID);

        ballLimitSwitch = new DigitalInput(BALL_SWTICH_ID);

        spinner = new Spinner();
    }

    public DifferentialDrive chassis() {
        return this.chassis;
    }

    public Spark intake() {
        return this.intake;
    }

    public Spark drawbridge() {
        return this.drawbridge;
    }

    public XboxController controller() {
        return this.controller;
    }

    public DigitalInput ballLimitSwitch() {
        return this.ballLimitSwitch;
    }

    public Spinner spinner() {
        return this.spinner;
    }


}