package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.*;                                                                                                                 

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
    Encoder encoder1;
    Encoder encoder2;
    //Pneumatics
    DoubleSolenoid ds1;
    DoubleSolenoid ds2;
    Compressor compressor;
    AnalogChannel pressureSensor;
    int analog;
    double pressure;
    double psi;
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
        pressureSensor = new AnalogChannel(2);
    }
    
    public void autonomous() {
        drive.drive(0.5, 0.0);
        Timer.delay(1);
        drive.drive(0.0, 0.0);
    }
    
    public void operatorControl() {
        compressor.start();
        System.out.println("Entering Teleop");
        while(isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(rightstick, leftstick);
            drive.setSensitivity((rightstick.getZ()+1.0)*.5);
            if (rightstick.getRawButton(1)) {
                fire();
            }
            //Feelers
            if (leftstick.getRawButton(1)) {
                feeler1.set(leftstick.getZ());
                feeler2.set(leftstick.getZ());
            } 
            else {
                feeler1.set(0.0);
                feeler2.set(0.0);
            }
            getPressure();
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
        display.println(DriverStationLCD.Line.kUser1, 1, "PSI: "+ psi + "00");
        
        //Here we debug the pistons
        if (ds1.get() == DoubleSolenoid.Value.kForward) {
            display.println(DriverStationLCD.Line.kUser2, 1, "ds1: forward");
        }
        else if (ds1.get() == DoubleSolenoid.Value.kReverse) {
            display.println(DriverStationLCD.Line.kUser2, 1, "ds1: reverse");
        }
        else if (ds1.get() == DoubleSolenoid.Value.kOff) {
            display.println(DriverStationLCD.Line.kUser2, 1, "ds1: off");
        }
        if (ds2.get() == DoubleSolenoid.Value.kForward) {
            display.println(DriverStationLCD.Line.kUser3, 1, "ds2: forward");
        }
        else if (ds2.get() == DoubleSolenoid.Value.kReverse) {
            display.println(DriverStationLCD.Line.kUser3, 1, "ds2: reverse");
        }
        else if (ds2.get() == DoubleSolenoid.Value.kOff) {
            display.println(DriverStationLCD.Line.kUser3, 1, "ds2: off");
        }
        display.updateLCD();
    }
    public void getPressure() {
            //gross linear function
        analog = pressureSensor.getValue();
        psi = ((.00001 * analog * analog) + (0.2442 * analog) - 23.254);
        psi= (int) (psi+.5);
    }
    
}