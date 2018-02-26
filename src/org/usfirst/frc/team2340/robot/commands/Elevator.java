package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Command {
	long startTime = 0;
	double desiredHeight = 0;
	double distance = 0;

	@Override
	protected void initialize() {
		Robot.elevator.setPosition();
		Robot.myLogger.log("Elevator","desiredHeight", distance);
		
		startTime = System.currentTimeMillis();
		desiredHeight = RobotUtils.getEncPositionFromIN(distance);
		Robot.elevator.movePosition( desiredHeight);
	}
	public  Elevator(double wantedHeight) {
		requires(Robot.elevator);
		distance = wantedHeight;
	}

	@Override
	protected void execute() {
		Robot.myLogger.log("Elevator","height position",Robot.elevator.getEncoder());

		SmartDashboard.putNumber("height position", Robot.elevator.getEncoder());
	}
	@Override
	protected boolean isFinished() {
		if (Robot.elevator.getEncoder() <= (desiredHeight + 50)
			&& Robot.elevator.getEncoder() >= (desiredHeight - 50)) {
			return true;
		}
		else {
			return false;
		}
	}
}

