package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class RobotTemplate extends SimpleRobot {
    //Variable declaration zone
    
    //Controls
    Joystick leftstick;
    Joystick rightstick;
    //Motors and drive train
    RobotDrive drive;
    public void robotInit() {
        System.out.println("Robot Initial");
        
        //Initializing our variables
        
        //Controls
        leftstick = new Joystick(1);
        rightstick = new Joystick(2);
        //Motors and drive train
        drive = new RobotDrive(1,2,3,4);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true); //These motors misbehaved, so we flipped them
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }
    public void autonomous() {
        //Autonomous code goes here, nothing yet.
    }
    public void operatorControl() {
        System.out.println("Entering Teleop");
        while(isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(leftstick, rightstick);
        }
    }
}
