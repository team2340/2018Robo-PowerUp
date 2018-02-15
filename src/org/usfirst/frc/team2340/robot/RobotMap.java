/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team2340.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// magic numbers~~
	public static final int BUTTON_1 = 1;
	public static final int BUTTON_2 = 2;
	public static final int BUTTON_3 = 3;
	public static final int BUTTON_4 = 4;
	public static final int BUTTON_5 = 5;
	public static final int BUTTON_6 = 6;
	public static final int BUTTON_7 = 7;
	/* Controller Ports */
	public static final int DRIVE_PORT = 1;
	public static final int ACQUISITION_PORT = 2;

	/* TALON IDs */
	// Wheel Ids
	public static final int RIGHT_TAL_ID = 3; // Right Side 3
	public static final int LEFT_TAL_ID = 4; // Left Side 4
	// Acquisition Ids
	public static final int ELEVATOR_TAL_ID = 5; // Elevator is 5
	public static final int ARM_ONE_TAL_ID = 6; // Arm one right side is 6
	public static final int ARM_TWO_TAL_ID = 7; // Arm two left side is 7
	public static final int CLIMBING_TAL_ID = 8; // Ball Shooter is 8
	public static final int PNEUMATICS_TAL_ID = 9; // expand or retract gear holder window motor is 9
	}
