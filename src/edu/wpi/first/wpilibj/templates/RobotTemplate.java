package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class RobotTemplate extends SimpleRobot {
    final boolean FEELERS_EXTEND = true;
    final boolean FEELERS_RETRACT = false;
    final boolean CATAPULT_UP = true;
    final boolean CATAPULT_DOWN = false;
    final long ONE_SECOND_IN_US = 1000000;
    //Controls
    Joystick leftstick;
    Joystick rightstick;
    DriverStationLCD display = DriverStationLCD.getInstance();
    boolean flip = false;
    
    //Motors and drive train
    RobotDrive drive;
    Jaguar feeler1;
    Jaguar feeler2;
    
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
        //Pneumatics
        s1 = new Solenoid(1);
        s2 = new Solenoid(2);
        s3 = new Solenoid(3);
        s4 = new Solenoid(4);
        s1.set(CATAPULT_UP);
        s2.set(CATAPULT_UP);
        s3.set(CATAPULT_UP);
        s4.set(CATAPULT_UP);
        feelerSolenoid = new Solenoid(5);
        compressor = new Compressor(2, 2);
        pressureTransducer = new AnalogChannel(3);
    }

    public void autonomous() {
        compressor.start();
        feelerSolenoid.set(FEELERS_EXTEND);
        Timer.delay(1.0);
        s1.set(CATAPULT_DOWN);
        s2.set(CATAPULT_DOWN);
        s3.set(CATAPULT_DOWN);
        s4.set(CATAPULT_DOWN);
        Timer.delay(1.0);
        long startTime = Timer.getUsClock();
        for (long currentTime = Timer.getUsClock(); (currentTime - startTime) <= (2*ONE_SECOND_IN_US); currentTime = Timer.getUsClock()) {
            drive.drive(((currentTime-startTime)/(ONE_SECOND_IN_US)*2), 0.0);
        }
        for (long currentTime = Timer.getUsClock(); (currentTime - startTime) <= (2*ONE_SECOND_IN_US); currentTime = Timer.getUsClock()) {
            drive.drive(0.5, 0.0);
        }
        for (long currentTime = Timer.getUsClock(); (currentTime - startTime) <= (2*ONE_SECOND_IN_US); currentTime = Timer.getUsClock()) {
            drive.drive(1-((currentTime-startTime)/(ONE_SECOND_IN_US)*2), 0.0);
        }
        fire();
        Timer.delay(1);
        drive.drive(1.0, 0.0);
    }

    public void operatorControl() {
        compressor.start();
        while (isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(rightstick, leftstick);
            if (leftstick.getRawButton(3)) {
                flip();
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
        s1.set(CATAPULT_UP);
        s2.set(CATAPULT_UP);
        s3.set(CATAPULT_UP);
        s4.set(CATAPULT_UP);
        Timer.delay(.5);
        s1.set(CATAPULT_DOWN);
        s2.set(CATAPULT_DOWN);
        s3.set(CATAPULT_DOWN);
        s4.set(CATAPULT_DOWN);
    }

    public void printDs() {
        display.println(DriverStationLCD.Line.kUser1, 1, "PSI: " + psi + "00");
        display.println(DriverStationLCD.Line.kUser2, 1, "Feeler Sensitivity: " + leftstick.getZ());
        display.println(DriverStationLCD.Line.kUser3, 1, "Voltage: " + analog);
        display.updateLCD();
    }

    public void getPressure() {
        analog = pressureTransducer.getVoltage();
        psi = (analog / 0.5) * 14.5;//Much better function
        psi = (int) (psi + .5);
    }
    
    public void flip() {
        long currentTime = Timer.getUsClock();
        long flipUsTime = 0;
        if ((currentTime - flipUsTime) >= 125000) {
            flipUsTime = currentTime;
            if (flip) {
                flip = false;
                drive.setSensitivity(0.5);
            }
            else {
                flip = true;
                drive.setSensitivity(-0.5);
            }
        }
    }
}