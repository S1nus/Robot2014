/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
public class RobotTemplate extends SimpleRobot {
    Joystick leftstick;
    Joystick rightstick;
    RobotDrive drive;
    public void robotInit() {
        System.out.println("Robot Initial");
        leftstick = new Joystick(1);
        rightstick = new Joystick(2);
        drive = new RobotDrive(1,2,3,4);
        drive.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, true);
        drive.setInvertedMotor(RobotDrive.MotorType.kRearLeft, true);
    }
    public void autonomous() {
        
    }
    public void operatorControl() {
        System.out.println("Entering Teleop");
        while(isOperatorControl() && isEnabled()) {
            drive.tankDrive(leftstick, rightstick);
        }
    }
}
