/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2340.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;

import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.framework.SuperJoystick;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	public final SuperJoystick driveController = new SuperJoystick(RobotMap.DRIVE_PORT);
	public final SuperJoystick acquisitionController = new SuperJoystick(RobotMap.ACQUISITION_PORT);

	public ADXRS450_Gyro gyro = null;

	public WPI_TalonSRX left = null;
	public WPI_TalonSRX right = null;
	public WPI_TalonSRX elevator = null;
	public WPI_TalonSRX armOne = null;
	public WPI_TalonSRX armTwo = null;
	public WPI_TalonSRX climbing = null;

	public final double CAM_VIEWING_ANGLE = 61.0;
	public final double IMG_WIDTH = 640.0;
	public final double IMG_HEIGHT = 480.0;
}
