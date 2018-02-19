package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Elevator extends Command {
	long startTime = 0;
	double desiredHeight = 0;
	double distance = 0;

	@Override
	protected void initialize() {
		Robot.myLogger.log("Elevator","desiredHeight", distance);
		Robot.elevator.setEncoder(0);
		
		startTime = System.currentTimeMillis();
		desiredHeight = RobotUtils.getEncPositionFromIN(distance);
		Robot.elevator.movePosition( desiredHeight);
	}
	public  Elevator(double wantedHeight) {
		requires(Robot.drive);
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

