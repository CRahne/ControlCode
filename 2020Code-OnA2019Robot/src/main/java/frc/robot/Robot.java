/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private RobotContainer m_robotContainer;

  public static WPI_TalonSRX RightMaster = new WPI_TalonSRX(2);
  public static WPI_VictorSPX RightSlave = new WPI_VictorSPX(3);
  public static WPI_TalonSRX LeftMaster = new WPI_TalonSRX(0);
  public static WPI_VictorSPX LeftSlave = new WPI_VictorSPX(1);

  public static SpeedControllerGroup Right = new SpeedControllerGroup(RightMaster, RightSlave);
  public static SpeedControllerGroup Left = new SpeedControllerGroup(LeftMaster, LeftSlave);

  public static DifferentialDrive drive = new DifferentialDrive(Left, Right);

  public static Joystick stick = new Joystick(0);

  public static Compressor comp = new Compressor();

  /**
   * This function is run when the robot is first started up and should be used for any
   * initialization code.
   */
  @Override
  public void robotInit() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.
    m_robotContainer = new RobotContainer();
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for items like
   * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
    comp.start();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
    comp.stop();
  }

  public double getMotorAverages() {
    return (RightMaster.getMotorOutputPercent() + LeftMaster.getMotorOutputPercent()) / 2;
  }

  public double output = 0;
  public double i = 0;
  public double change = 0;

  public void teleopPeriodic() {
    
  /* ACCL CODE */
    double forward_gain = 0.009;
    double reverse_gain = -0.5;
    // double greatestChange = 0.025;
    double curr = getMotorAverages();
    double targ = (-stick.getY()) * 0.8;

    if(Math.abs(targ) < 0.2) {
      targ = 0.0;
    }
    
    if (targ == 0.0) {
      change = (reverse_gain * curr);
    } else {
      change = -(forward_gain * (targ - curr));
    }

    output = output + change;

    if(output > 1) {
      output = 1;
    } else if (output < -1) {
      output = -1;
    }
    
    // drive.set(ControlMode.PercentOutput, output);
    if(Math.abs(output) > 0.3) {
      drive.arcadeDrive(output, stick.getZ());
    } else {
      drive.arcadeDrive(0, stick.getZ());
    }

    i = i + 1;
    SmartDashboard.putNumber("curr - Motor", curr);
    SmartDashboard.putNumber("targ - Joystick", targ);
    SmartDashboard.putNumber("Output", output);
    SmartDashboard.putNumber("change", change);
    SmartDashboard.putNumber("Iterations", i);
    SmartDashboard.putNumber("RightVolts", RightMaster.getMotorOutputVoltage());
    SmartDashboard.putNumber("rightpercent", RightMaster.getMotorOutputPercent());
    

    // drive.arcadeDrive(stick.getY(), stick.getZ() * 0.8);
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
