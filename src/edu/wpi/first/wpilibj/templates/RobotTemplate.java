package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;

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
    //Pneumatics
    DoubleSolenoid ds1;
    DoubleSolenoid ds2;
    Compressor compressor;
    public void robotInit() {
        System.out.println("Robot Initialization");
        //Initializing our variables
        //Controls
        leftstick = new Joystick(1);
        rightstick = new Joystick(2);
        //Motors and drive train (placeholders)
        feeler1 = new Jaguar(0);
        feeler2 = new Jaguar(0);
        drive = new RobotDrive(1,2,3,4); //Note: some motors may need to be flipped
        //Pneumatics (placeholders)
        ds1 = new DoubleSolenoid(0,0);
        ds2 = new DoubleSolenoid(0,0);
        compressor = new Compressor(0,0);
    }
    
    public void autonomous() {
        drive.drive(1.0, 0.0);
        Timer.delay(5);
        drive.drive(0.0, 0.0);
    }
    
    public void operatorControl() {
        System.out.println("Entering Teleop");
        while(isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(leftstick, rightstick);
            if (rightstick.getRawButton(1)) {
                fire();
            }
            if (leftstick.getRawButton(1)) {
                feeler1.set(1.0);
                feeler2.set(1.0);
            }
            else {
                feeler1.set(0.0);
                feeler2.set(0.0);
            }
        }
    }
    
    public void fire() {
        ds1.set(DoubleSolenoid.Value.kForward);
        ds2.set(DoubleSolenoid.Value.kForward);
        Timer.delay(1);
        ds1.set(DoubleSolenoid.Value.kOff);
        ds2.set(DoubleSolenoid.Value.kOff);
        Timer.delay(1);
        ds1.set(DoubleSolenoid.Value.kReverse);
        ds2.set(DoubleSolenoid.Value.kReverse);
    }
}
