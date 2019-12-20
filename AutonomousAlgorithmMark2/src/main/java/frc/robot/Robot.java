package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Auto;
import frc.robot.commands.TimeAuto;

public class Robot extends TimedRobot {
  public static Auto sAuto = new Auto();
  public Command cTimeAuto;
  public static OI m_oi;
  
  public static Compressor comp = new Compressor();

  // Sets up the motors
  public static WPI_TalonSRX DrivetrainLeftMaster = new WPI_TalonSRX(0);
  public static WPI_VictorSPX DrivetrainLeftSlave = new WPI_VictorSPX(1);
  public static WPI_TalonSRX DrivetrainRightMaster = new WPI_TalonSRX(2);
  public static WPI_VictorSPX DrivetrainRightSlave = new WPI_VictorSPX(3);

  public static SpeedControllerGroup Left = new SpeedControllerGroup(DrivetrainLeftMaster, DrivetrainLeftSlave);
  public static SpeedControllerGroup Right = new SpeedControllerGroup(DrivetrainRightMaster, DrivetrainRightSlave);

  public static DifferentialDrive drive = new DifferentialDrive(Left, Right);

  public static Joystick stick = new Joystick(0);

  public void robotInit() {
    m_oi = new OI();
    cTimeAuto = new TimeAuto();
    DrivetrainLeftMaster.setInverted(true);
    DrivetrainRightMaster.setInverted(true);
    DrivetrainLeftSlave.setInverted(true);
    DrivetrainRightSlave.setInverted(true);
  }


  public void robotPeriodic() {
  }


  public void disabledInit() {
  }


  public void disabledPeriodic() {
    Scheduler.getInstance().run();
  }

  public void autonomousInit() {
    // sAuto.DistanceAutoInit(30);
    sAuto.DistanceAutoInit(30);
  }


  public void autonomousPeriodic() {
    sAuto.AutoRun();
  }


  public void teleopInit() {
    comp.start();
  }


  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    // Allows us to drive the robot with a joystick in teleop
    drive.arcadeDrive(stick.getY() * 0.5, stick.getX() *0.8);
  }

  public void testPeriodic() {
  }
}
