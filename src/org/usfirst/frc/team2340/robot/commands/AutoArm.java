package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class AutoArm extends Command {
	long startTime = 0;
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}
	public  AutoArm( ) {
	}
	@Override
	protected void execute() {
		Robot.oi.armone.set(1); 
		Robot.oi.armtwo.set(1);
	}
	@Override
	protected boolean isFinished() {
		if (System.currentTimeMillis() >= (startTime + 4000)) {
			Robot.oi.armone.set(0); 
			Robot.oi.armtwo.set(0);
			return true;
		}
		else {
			return false;
		}

	}
}
