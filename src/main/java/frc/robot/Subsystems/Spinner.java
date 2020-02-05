package frc.robot.Subsystems;

import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.util.Color;

/**
 * Spinner - contains spinner arm, spinner wheel, and color sensor
 */
public class Spinner {

    private ColorSensor colorSensor;

    private Spark spinner;
    private final int SPINNER_ID = -1;

    private Spark arm;
    private final int ARM_ID = -1;

    public Spinner() {

        colorSensor = new ColorSensor();

        spinner = new Spark(SPINNER_ID);
        arm = new Spark(ARM_ID);

    }

    /**
     * Finds a certain color on the color wheel
     * @param toFind
     * @return Whether the spinner is still trying to find the color - true = still trying to find color
     */
    public boolean findColor(Color toFind) {
        if (!colorSensor.getAdjustedColor().equals(toFind)) {
            spinner.set(1.0);
            return true;
        } else {
            spinner.set(0.0);
            return false;
        }
    }

    public void moveArm(double speed) {
        arm.set(speed);
    }

    public void moveWheel(double speed) {
        spinner.set(speed);
    }


/**
 * ColorSensor - from Vex
 */
private class ColorSensor {

    /**
     * Change the I2C port below to match the connection of your color sensor
     */
    private final I2C.Port i2cPort = I2C.Port.kOnboard;
  
    /**
     * A Rev Color Sensor V3 object is constructed with an I2C port as a 
     * parameter. The device will be automatically initialized with default 
     * parameters.
     */
    private final ColorSensorV3 m_colorSensor = new ColorSensorV3(i2cPort);
  
    /**
     * A Rev Color Match object is used to register and detect known colors. This can 
     * be calibrated ahead of time or during operation.
     * 
     * This object uses a simple euclidian distance to estimate the closest match
     * with given confidence range.
     */
    private final ColorMatch m_colorMatcher = new ColorMatch();
  
    /**
     * Note: Any example colors should be calibrated as the user needs, these
     * are here as a basic example.
     */
    private final Color kBlueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color kGreenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color kRedTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color kYellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);
  
    public ColorSensor() {
      m_colorMatcher.addColorMatch(kBlueTarget);
      m_colorMatcher.addColorMatch(kGreenTarget);
      m_colorMatcher.addColorMatch(kRedTarget);
      m_colorMatcher.addColorMatch(kYellowTarget);    
    }
  
    public Color getColor() {
      /**
       * The method GetColor() returns a normalized color value from the sensor and can be
       * useful if outputting the color to an RGB LED or similar. To
       * read the raw color, use GetRawColor().
       * 
       * The color sensor works best when within a few inches from an object in
       * well lit conditions (the built in LED is a big help here!). The farther
       * an object is the more light from the surroundings will bleed into the 
       * measurements and make it difficult to accurately determine its color.
       */
      Color detectedColor = m_colorSensor.getColor();
  
      /**
       * Run the color match algorithm on our detected color
       */
      ColorMatchResult match = m_colorMatcher.matchClosestColor(detectedColor);
  
      if (match.color == kBlueTarget) {
        return Color.kBlue;
      } else if (match.color == kRedTarget) {
        return Color.kRed;
      } else if (match.color == kGreenTarget) {
        return Color.kGreen;
      } else if (match.color == kYellowTarget) {
        return Color.kYellow;
      } else {
        return Color.kBlack; // Should never hit
      }
    }
  
    /**
     * Adjusts the color read from the color sensor to the color pointed
     * @return The shifted color
     */
    public Color getAdjustedColor() {
        Color currentColor = getColor();
        if (currentColor.equals(Color.kYellow)) {
            return Color.kBlue;
        } else if (currentColor.equals(Color.kBlue)) {
            return Color.kGreen;
        } else if (currentColor.equals(Color.kGreen)) {
          return Color.kRed;
        } else if (currentColor.equals(Color.kRed)) {
          return Color.kYellow;
        } else {
            return Color.kBlack; // Should never hit
        }
    }
  
  }
}