/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {

public static TalonSRX Motor0;
public static VictorSP Motor1;
public static VictorSP Motor2;
public static VictorSP Motor3;

  public static void init(){

    TalonSRX Motor0 = new TalonSRX(5);
    VictorSP Motor1 = new VictorSP(1);
    VictorSP Motor2 = new VictorSP(2);
    VictorSP Motor3 = new VictorSP(3);

    /*
    DriveTrain_Left = new SpeedControllerGroup(
				DriveTrain_left1,DriveTrain_left2);
		
		DriveTrain_Right = new SpeedControllerGroup(
				DriveTrain_right1,DriveTrain_right2);
		
		RobotDrive = new DifferentialDrive(DriveTrain_Left,DriveTrain_Right);
*/
  }


  // For example to map the left and right motors, you could define the
  // following variables to use with your drivetrain subsystem.
  // public static int leftMotor = 1;
  // public static int rightMotor = 2;

  // If you are using multiple modules, make sure to define both the port
  // number and the module. For example you with a rangefinder:
  // public static int rangefinderPort = 1;
  // public static int rangefinderModule = 1;
}
