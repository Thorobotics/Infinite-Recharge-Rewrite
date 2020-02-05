/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Subsystems.CameraServerGroup;
import frc.robot.Subsystems.Map;

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
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {

    // Drives the robot based on joystick
    map.chassis().arcadeDrive(map.controller().getRawAxis(Map.Y_AXIS), map.controller().getRawAxis(Map.X_AXIS));

    // Toggle intake on and off if x button pressed
    if(map.controller().getXButtonPressed()) {
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
    if (map.controller().getBumper(Hand.kLeft)) {
      map.drawbridge().set(1.0);
    } else if (map.controller().getBumper(Hand.kRight)) {
      map.drawbridge().set(-1.0);   // Speeds may need to change
      ballsInHopper = 0;           // Assumes that bringing up the door means that hopper has been emptied
    } else { // If no bumpers pressed, stop the drawbridge moving
      map.drawbridge().set(0.0);
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

    // Check if the spinner should start finding the color
    if (map.controller().getYButtonPressed()) {
      isFindingColor = true;
    }

    // Check if the spinner is finding the color
    if (isFindingColor) {
      if (map.spinner().findColor(null)) { // Need to change 
        isFindingColor = true;
      } else {
        isFindingColor = false;
      }
    }

    // Move the spinner arm up and down with buttons
    if (map.controller().getAButton()) {
      map.spinner().moveArm(-1.0);
    } else if (map.controller().getBButton()) {
      map.spinner().moveArm(1.0); //Change values
    } else {
      map.spinner().moveArm(0.0);
    }

    // Move the spinner wheel left and right manually
    if (map.controller().getBumperPressed(Hand.kLeft)) {
      map.spinner().moveWheel(-1.0); // Change values
    } else if (map.controller().getBumperPressed(Hand.kRight)) {
      map.spinner().moveWheel(1.0);
    } else {
      map.spinner().moveWheel(0.0);
    }

  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
