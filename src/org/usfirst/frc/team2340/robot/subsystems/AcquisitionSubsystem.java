package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class AcquisitionSubsystem extends Subsystem {
	static private AcquisitionSubsystem subsystem;
	
	private AcquisitionSubsystem() {
	
	}

	public static AcquisitionSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new AcquisitionSubsystem();
		}
		return subsystem;
	}
	
	@Override
	protected void initDefaultCommand() {
//		setDefaultCommand(new AcquisitionCommand(this));
	}
}
