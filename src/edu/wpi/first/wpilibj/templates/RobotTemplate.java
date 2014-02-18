package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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
    Solenoid feelerSolenoid;
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
        ds1 = new DoubleSolenoid(1, 2);
        ds2 = new DoubleSolenoid(3, 4);
        ds1.set(DoubleSolenoid.Value.kReverse);
        ds2.set(DoubleSolenoid.Value.kReverse);
        feelerSolenoid = new Solenoid(5);
        compressor = new Compressor(2, 2);
        pressureTransducer = new AnalogChannel(2);
    }

    public void autonomous() {
        drive.drive(0.5, 0.0);
        Timer.delay(3);
        drive.drive(0.0, 0.0);
        fire();
    }

    public void operatorControl() {
        compressor.start();
        System.out.println("Entering Teleop");
        while (isOperatorControl() && isEnabled()) {
            //Setting the left and rights sticks to control the drive train like a tank
            drive.tankDrive(rightstick, leftstick);
            drive.setSensitivity((rightstick.getZ() + 1.0) * .5);
            if (rightstick.getRawButton(1)) {
                fire();
            }
            //Feelers
            if (leftstick.getRawButton(1)) {
                feeler1.set(leftstick.getZ());
                feeler2.set(leftstick.getZ());
            } else {
                feeler1.set(0.0);
                feeler2.set(0.0);
            }
            if (rightstick.getRawButton(2)) {
                feelerSolenoid.set(false);
            }
            if (rightstick.getRawButton(3)) {
                feelerSolenoid.set(true);
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
}