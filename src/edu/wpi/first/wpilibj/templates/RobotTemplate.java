package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class RobotTemplate extends SimpleRobot {
    final boolean FEELERS_EXTEND = true;
    final boolean FEELERS_RETRACT = false;
    final boolean CATAPULT_UP = false;
    final boolean CATAPULT_DOWN = true;
    final long ONE_SECOND_IN_US = 1000000;
    //Controls
    Joystick leftstick;
    Joystick rightstick;
    DriverStationLCD display = DriverStationLCD.getInstance();
    
    //Motors and drive train
    RobotDrive drive;
    Jaguar feeler1;
    Jaguar feeler2;
    Encoder leftEncoder;
    Encoder rightEncoder;
    
    //Pneumatics
    Solenoid s1, s2, s3, s4, feelerSolenoid;
    Compressor compressor;
    AnalogChannel pressureTransducer;
    double analog;
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
        drive = new RobotDrive(1, 2, 3, 4);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontRight, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearRight, true);
        //Pneumatics
        s1 = new Solenoid(1);
        s2 = new Solenoid(2);
        s3 = new Solenoid(3);
        s4 = new Solenoid(4);
        setCatapult(CATAPULT_DOWN);
        feelerSolenoid = new Solenoid(5);
        compressor = new Compressor(2, 2);
        pressureTransducer = new AnalogChannel(3);
    }
    
    public void autonomous() {
        compressor.start();
        feelerSolenoid.set(FEELERS_EXTEND);
        Timer.delay(1.0);
        setCatapult(CATAPULT_DOWN);
        Timer.delay(1.0);
        long startTime = Timer.getUsClock();
        for (long currentTime = Timer.getUsClock(); (currentTime - startTime) <= (2*ONE_SECOND_IN_US); currentTime = Timer.getUsClock()) {
            drive.drive(-((currentTime-startTime)/(ONE_SECOND_IN_US)*4), 0.0);
        }
        for (long currentTime = Timer.getUsClock(); (currentTime - startTime) <= (2*ONE_SECOND_IN_US); currentTime = Timer.getUsClock()) {
            drive.drive(-0.1, 0.0);
        }
        for (long currentTime = Timer.getUsClock(); (currentTime - startTime) <= (2*ONE_SECOND_IN_US); currentTime = Timer.getUsClock()) {
            drive.drive(-(1-((currentTime-startTime)/(ONE_SECOND_IN_US)*4)), 0.0);
        }
        fire();
        Timer.delay(1);
    }

    public void operatorControl() {
        compressor.start();
        
        while (isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(leftstick, rightstick);
            //Feelers
            if (leftstick.getRawButton(1)) {
                //feeler1.set(leftstick.getZ());
                //feeler2.set(leftstick.getZ());
                minifire();
            }
            else {
                feeler1.set(0.0);
                feeler2.set(0.0);
            }
            if (rightstick.getRawButton(1)){
                fire();    
            }
            if (rightstick.getRawButton(3)) {
                feelerSolenoid.set(FEELERS_EXTEND);
            }
            if (rightstick.getRawButton(2)) {
                feelerSolenoid.set(FEELERS_RETRACT);
            }
            getPressure();
            printDs();
        }
    }

    public void fire() {
        if (beamsDown()) {
            setCatapult(CATAPULT_UP);
            Timer.delay(.5);
            setCatapult(CATAPULT_DOWN);
        }
    }
    
    public void minifire() {
        setCatapult(CATAPULT_UP);
        Timer.delay(.01);
        setCatapult(CATAPULT_DOWN);
    }

    public void printDs() {
        display.println(DriverStationLCD.Line.kUser1, 1, "PSI: " + psi + "00");
        display.println(DriverStationLCD.Line.kUser2, 1, "Feeler Sensitivity: " + leftstick.getZ());
        display.println(DriverStationLCD.Line.kUser3, 1, "Voltage: " + analog);
        display.updateLCD();
        SmartDashboard.putNumber("pressure", psi);
    }

    public void getPressure() {
        analog = pressureTransducer.getVoltage();
        psi = ((51.4166331*analog)-24.97030688);
    } 
        
    public void setCatapult(boolean mode) {
        s1.set(mode);
        s2.set(mode);
        s3.set(mode);
        s4.set(mode);
    }
    public boolean beamsDown() {
        return feelerSolenoid.get();
    }
    public void disabled() {
        feelerSolenoid.set(FEELERS_EXTEND);
        Timer.delay(0.5);
        setCatapult(CATAPULT_DOWN);
        Timer.delay(0.5);
        feelerSolenoid.set(FEELERS_RETRACT);
        Timer.delay(0.5);
    }
}