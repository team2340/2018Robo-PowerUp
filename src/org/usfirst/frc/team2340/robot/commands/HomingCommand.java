package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

public class HomingCommand extends Command {
	long startTime = 0;
	public HomingCommand() {
		requires(Robot.elevator);
	}
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void execute() {
		Robot.myLogger.log("Elevator","Homing",Robot.elevator.getEncoder());
		Robot.elevator.move(-.5);
	}

	@Override
	protected boolean isFinished() {
		if (Robot.oi.elevator.getSensorCollection().isRevLimitSwitchClosed()) {
//		if (System.currentTimeMillis() >= startTime+1000) {
			Robot.myLogger.log("Elevator","HOMED",Robot.elevator.getEncoder());
			Robot.elevator.setEncoder(0);
			return true;
		}
		return false;
	}
}
