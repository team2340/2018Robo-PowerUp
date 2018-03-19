package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class PneumaticArms extends Command {
	
	
	
		@Override
		protected void initialize() {
			Robot.arm.open();
		}
	
	@Override
	protected void execute() {
		

	}
	@Override
	protected boolean isFinished() {
		// TODO Auto-generated method stub
		Robot.arm.open();
		return true;
	}
}



