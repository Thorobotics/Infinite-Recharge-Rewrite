/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Subsystems.CameraServerGroup;    // current sensing, pathweaver 
import frc.robot.Subsystems.Map;                    // https://blog.alexbeaver.com/wpilib-trajectory-guide/
import edu.wpi.first.wpilibj.util.Color;                    //  ^-- for pathweaver

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

  // Other classes
  private Map map;
  private CameraServerGroup cameras;

  // Variables for this class
  private boolean isIntakeOn;
  private boolean wasSwitchHitLastTime;
  private int ballsInHopper;
  private boolean isFindingColor;

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    map = new Map();

    // Startup cameras running
    cameras = new CameraServerGroup();

    isIntakeOn = false;
    wasSwitchHitLastTime = false;
    ballsInHopper = -1; // Need to change
    isFindingColor = false;
  }

  @Override
  public void autonomousInit() {
  }

  @Override
  public void autonomousPeriodic() {
    map.chassis().arcadeDrive(0.6, 0.0);
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {

    boolean toggleIntake = map.controller().getXButtonPressed();
    boolean drawbridgeUp = map.controller().getBumper(Hand.kLeft);
    boolean drawbridgeDown = map.controller().getBumper(Hand.kRight);
    boolean startSpinnerButton = map.controller().getYButtonPressed();
    boolean spinnerArmUp = map.controller().getAButton();
    boolean spinnerArmDown = map.controller().getBButton();
    boolean spinnerWheelLeft = map.controller().getBumperPressed(Hand.kLeft);
    boolean spinnerWheelRight = map.controller().getBumperPressed(Hand.kRight);

    // Drives the robot based on joystick
    map.chassis().arcadeDrive(map.controller().getRawAxis(Map.Y_AXIS), map.controller().getRawAxis(Map.X_AXIS));

    // Toggle intake on and off if x button pressed
    if(toggleIntake) {
      // If balls hit the limit, stop the intake
      if (ballsInHopper > 5) {
        map.intake().set(0.0);
      } else {   
        if (isIntakeOn) { // If intake is currently on, turn off
          map.intake().set(0.0);
          isIntakeOn = false;
        } else { // If intake is currently off, turn on
          map.intake().set(1.0);   // Speeds may need to change
          isIntakeOn = true;
        }
      }
      
    }

    // Move drawbridge up and down with left and right bumpers
    if (drawbridgeUp) {
      if(!map.bridgeUpper().get()) {
        map.drawbridge().set(1.0);
      }  
      map.stopControllerRumble(); 
    } else if (drawbridgeDown) {
      if(!map.bridgeLower().get()) {
        map.drawbridge().set(-1.0);   // Speeds may need to change
      }
      map.stopControllerRumble();
    } else if (!map.bridgeReady().get()) {
      ballsInHopper = 0;           // Assumes that bringing up the door means that hopper has been emptied
      map.rumbleController();
    } else { // If no bumpers pressed, stop the drawbridge moving
      map.drawbridge().set(0.0);
      map.stopControllerRumble();
    }

    // Check count for ball counter limit switch
    if (map.ballLimitSwitch().get()) {
      // If the ball was not pressing the switch down last loop, increase the count
      if (!wasSwitchHitLastTime) {
        ballsInHopper++;
        wasSwitchHitLastTime = true;
      } else {
        wasSwitchHitLastTime = false;
      }
    }



    // Move the spinner arm up and down with buttons
    if (spinnerArmUp) {
      if (!map.armUpper().get()) {
        map.spinner().moveArm(-1.0);
      } else {
            // Check if the spinner should start finding the color
    if (startSpinnerButton) {
      isFindingColor = true;
    }

    // Check if the spinner is finding the color
    if (isFindingColor) {
      if (map.spinner().findColor(Color.kBlue)) { // Need to change 
        isFindingColor = true;
      } else {
        isFindingColor = false;
      }
    } else {
        // Move the spinner wheel left and right manually
        if (spinnerWheelLeft) {
          map.spinner().moveWheel(-1.0); // Change values
        } else if (spinnerWheelRight) {
          map.spinner().moveWheel(1.0);
        } else {
          map.spinner().moveWheel(0.0);
        }
      }
        map.rumbleController();
      }
    } else if (spinnerArmDown) {
      if (!map.armLower().get()) {
        map.spinner().moveArm(1.0); //Change values
      }
      map.stopControllerRumble();
    } else {
      map.spinner().moveArm(0.0);
      map.stopControllerRumble();
    }

  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
