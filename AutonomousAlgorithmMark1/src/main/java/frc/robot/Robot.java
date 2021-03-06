package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;

public class Robot extends TimedRobot {
  public static ExampleSubsystem m_subsystem = new ExampleSubsystem();
  public static OI m_oi;
  
  public static Compressor comp = new Compressor();

  public static WPI_TalonSRX DrivetrainLeftMaster = new WPI_TalonSRX(0);
  public static WPI_VictorSPX DrivetrainLeftSlave = new WPI_VictorSPX(1);
  public static WPI_TalonSRX DrivetrainRightMaster = new WPI_TalonSRX(2);
  public static WPI_VictorSPX DrivetrainRightSlave = new WPI_VictorSPX(3);

  public static SpeedControllerGroup Left = new SpeedControllerGroup(DrivetrainLeftMaster, DrivetrainLeftSlave);
  public static SpeedControllerGroup Right = new SpeedControllerGroup(DrivetrainRightMaster, DrivetrainRightSlave);

  public static DifferentialDrive drive = new DifferentialDrive(Left, Right);

  public static Joystick stick = new Joystick(0);
  

  // https://first.wpi.edu/FRC/roborio/beta/docs/java/edu/wpi/first/wpilibj/Timer.html - Timer Docs

  


  public void robotInit() {
    m_oi = new OI();
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


  // Time Constants
  public static double TIME_init = Timer.getFPGATimestamp();
  public static double TIME_total = 4;

  // Voltage Constants
  public static double VOLTS_max = 0.8;
  public static double VOLTS_min = 0.35;

  // Created Constants
  public static double TIME_half = TIME_total / 2;
  public static double gain = (VOLTS_min - VOLTS_max) / Math.pow((TIME_init - TIME_half), 2);
  public double TIME_curr;
  public double VOLTS_curr;
  
  public double getNewVoltCurr(double TIME_curr) {
    return (gain * (Math.pow(TIME_curr - TIME_half, 2))) + VOLTS_max;
  }
  public void autonomousInit() {
    comp.stop();
    TIME_init = Timer.getFPGATimestamp();
    TIME_curr = Timer.getFPGATimestamp() - TIME_init;
    VOLTS_curr = getNewVoltCurr(TIME_curr);
    drive.arcadeDrive(VOLTS_curr, 0);
    SmartDashboard.putNumber("Current Volts", VOLTS_curr);
    SmartDashboard.putNumber("Time_curr", TIME_curr);
    SmartDashboard.putNumber("Gain", gain);
    SmartDashboard.putNumber("Time_init", TIME_init);
    SmartDashboard.putNumber("FPGA Time Stamp", Timer.getFPGATimestamp());
  }


  public void autonomousPeriodic() {
    if (TIME_curr <= 10) {
    TIME_curr = Timer.getFPGATimestamp() - TIME_init;
    VOLTS_curr = getNewVoltCurr(TIME_curr);
    }
    if (VOLTS_curr < VOLTS_min) {
      VOLTS_curr = 0.0;
      
    drive.arcadeDrive(VOLTS_curr, 0);
    SmartDashboard.putNumber("Current Volts", VOLTS_curr);
    SmartDashboard.putNumber("Time_curr", TIME_curr);
    SmartDashboard.putNumber("FPGA Time Stamp", Timer.getFPGATimestamp());
    } 
    
    else {
      SmartDashboard.putString("Done", "done");
    }
  }


  public void teleopInit() {
    // comp.start();
  }


  public void teleopPeriodic() {
    Scheduler.getInstance().run();
    drive.arcadeDrive(stick.getY() * 0.5, stick.getX() *0.8);
  }

  public void testPeriodic() {
  }
}
