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
    Jaguar feeler1;
    Jaguar feeler2;
    double feelerspeed;
    public void robotInit() {
        System.out.println("Robot Initialization");
        
        //Initializing our variables
        
        //Controls
        leftstick = new Joystick(1);
        rightstick = new Joystick(2);
        //Motors and drive train
        feeler1 = new Jaguar(0); //Feeler1 - fix placeholder when completed
        feeler2 = new Jaguar(0); //Feeler2 - fix placeholder when completed
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
            if (rightstick.getRawButton(3)) {
                feeler1.set(feelerspeed);
                feeler2.set(feelerspeed);
            }
            else {
                feeler1.set(0.0);
                feeler2.set(0.0);
            }
            if (rightstick.getRawButton(2)) {
                toggleFeelers();
            }
        }
    }
    public void toggleFeelers() {
        if (feelerspeed == 1.0) {
            feelerspeed = -1.0;
        }
        else if (feelerspeed == -1.0) {
            feelerspeed = 1.0;
        }
        Timer.delay(1.5);
    }
}
