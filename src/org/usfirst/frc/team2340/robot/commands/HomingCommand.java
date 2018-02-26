package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class HomingCommand extends Command {
	
	public HomingCommand() {
		requires(Robot.elevator);
	}
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Robot.elevator.move(-1);
	}

	@Override
	protected boolean isFinished() {
		if (Robot.oi.elevator.getSensorCollection().isRevLimitSwitchClosed()) {
			Robot.elevator.setEncoder(0);
			return true;
		}
		return false;
	}
}
