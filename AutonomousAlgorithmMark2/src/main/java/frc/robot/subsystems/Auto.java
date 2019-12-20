/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.Robot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class Auto extends Subsystem {
    // Time Variables
    public static double TIME_total = 3.7;
    public static double TIME_init;
    // Voltage Constants
    public static double VOLTS_max = 0.8;
    public static double VOLTS_min = 0.3;
    // Created Constants
    public static double TIME_half;
    public static double gain;
    // Holds the current voltage/tieme
    public double TIME_curr;
    public double VOLTS_curr;


  // https://first.wpi.edu/FRC/roborio/beta/docs/java/edu/wpi/first/wpilibj/Timer.html - Timer Docs


  @Override
  public void initDefaultCommand() {
    // Set the default command for a subsystem here.
    // setDefaultCommand(new MySpecialCommand());
  }

  // Runs the Autonomous Movement
  // One of the inits MUST have been run before running this method
  public void AutoRun(){
    // TIME_half = TIME_total / 2;
    if (TIME_curr <= 10) {
      TIME_curr = Timer.getFPGATimestamp() - TIME_init;
      VOLTS_curr = getNewVoltCurr(TIME_curr);
      if (VOLTS_curr < VOLTS_min) {
        VOLTS_curr = 0.0;
      }
      Robot.drive.arcadeDrive(VOLTS_curr, 0);
      SmartDashboard.putNumber("Current Volts", VOLTS_curr);
      SmartDashboard.putNumber("Time_curr", TIME_curr);
      SmartDashboard.putNumber("FPGA Time Stamp", Timer.getFPGATimestamp());
    } else {
      // SmartDashboard.putString("Done", "done");
    }

  }

  // Sets up for Time-based Autonomous Motion
  // Runs the motors for the time in seconds
  public void TimeAutoInit(double Time){
    Robot.comp.stop();
    TIME_total = Time;
    TIME_init = Timer.getFPGATimestamp();
    TIME_curr = Timer.getFPGATimestamp() - TIME_init;
    VOLTS_curr = getNewVoltCurr(TIME_curr);
    Robot.drive.arcadeDrive(VOLTS_curr, 0);
  }

  // Sets up for Distance-Based Autonomous Motion
  // Moves the distance in inches
  public void DistanceAutoInit(double Distance){
      Robot.comp.stop();
      TIME_total = (Distance + 52.912) / 26.132;
      TIME_init = Timer.getFPGATimestamp();
      TIME_curr = Timer.getFPGATimestamp() - TIME_init;
      TIME_half = TIME_total / 2;
      gain = (VOLTS_min - VOLTS_max) / Math.pow((TIME_init - TIME_half), 2);
      VOLTS_curr = getNewVoltCurr(TIME_curr);
      Robot.drive.arcadeDrive(VOLTS_curr, 0);
  }

  // Calculates what the voltage output should be
  public double getNewVoltCurr(double TIME_curr) {
    SmartDashboard.putNumber("New Volt Curr", (gain * (Math.pow(TIME_curr - TIME_half, 2))) + VOLTS_max);
    SmartDashboard.putNumber("Gain", gain);
    // gain = (VOLTS_min - VOLTS_max) / Math.pow((TIME_init - TIME_half), 2);
    return (gain * (Math.pow(TIME_curr - TIME_half, 2))) + VOLTS_max;
  }
}
