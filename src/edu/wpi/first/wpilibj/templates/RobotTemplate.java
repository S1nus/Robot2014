package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DriverStationLCD;

public class RobotTemplate extends SimpleRobot {
    //Variable declaration zone
    //Controls
    Joystick leftstick;
    Joystick rightstick;
    DriverStationLCD display = DriverStationLCD.getInstance();
    //Motors and drive train
    RobotDrive drive;
    Jaguar feeler1;
    Jaguar feeler2;
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
        //Motors and drive train
        feeler1 = new Jaguar(5);
        feeler2 = new Jaguar(6);
        drive = new RobotDrive(1,2,3,4);
        //Because WPILib is kill
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        //Pneumatics
          ds1 = new DoubleSolenoid(2,1);
          ds2 = new DoubleSolenoid(4,3);
          ds1.set(DoubleSolenoid.Value.kReverse);
          ds2.set(DoubleSolenoid.Value.kReverse);
        compressor = new Compressor(2,2);
    }
    
    public void autonomous() {
        drive.drive(1.0, 0.0);
        Timer.delay(5);
        drive.drive(0.0, 0.0);
    }
    
    public void operatorControl() {
        compressor.start();
        System.out.println("Entering Teleop");
        while(isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(rightstick, leftstick);
            if (rightstick.getRawButton(1)) {
                fire();
            }
            //Feelers
            if (leftstick.getRawButton(1)) {
                feeler1.set(leftstick.getZ()*-1);
                feeler2.set(leftstick.getZ());
            } 
            else {
                feeler1.set(0.0);
                feeler2.set(0.0);
            }
            printDs();
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
    
    public void printDs() {
        //Here we debug the pistons
        if (ds1.get() == DoubleSolenoid.Value.kForward) {
            display.println(DriverStationLCD.Line.kUser1, 1, "ds1: forward");
        }
        else if (ds1.get() == DoubleSolenoid.Value.kReverse) {
            display.println(DriverStationLCD.Line.kUser1, 1, "ds1: reverse");
        }
        else if (ds1.get() == DoubleSolenoid.Value.kOff) {
            display.println(DriverStationLCD.Line.kUser1, 1, "ds1: off");
        }
        if (ds2.get() == DoubleSolenoid.Value.kForward) {
            display.println(DriverStationLCD.Line.kUser2, 1, "ds2: forward");
        }
        else if (ds2.get() == DoubleSolenoid.Value.kReverse) {
            display.println(DriverStationLCD.Line.kUser2, 1, "ds2: reverse");
        }
        else if (ds2.get() == DoubleSolenoid.Value.kOff) {
            display.println(DriverStationLCD.Line.kUser2, 1, "ds2: off");
        }        
        display.updateLCD();
    } 
}