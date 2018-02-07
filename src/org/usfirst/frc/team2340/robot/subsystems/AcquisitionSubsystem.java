package org.usfirst.frc.team2340.robot.subsystems;

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
