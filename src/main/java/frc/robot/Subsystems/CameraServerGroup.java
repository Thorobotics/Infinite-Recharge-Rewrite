package frc.robot.Subsystems;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

/**
 * Controls all cameras on the robot
 */
public class CameraServerGroup {
    private UsbCamera camera1;
    private UsbCamera camera2;


    public CameraServerGroup(){
        camera1 = CameraServer.getInstance().startAutomaticCapture(0);
        camera1.setResolution(640, 480);
        camera2 = CameraServer.getInstance().startAutomaticCapture(1);
        camera2.setResolution(640, 480);
    }
}