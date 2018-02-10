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
		startTime = System.currentTimeMillis();
		desiredHeight = RobotUtils.getEncPositionFromIN(distance);
		Robot.oi.elevator.set(desiredHeight);
	}
	public  Elevator(double wantedHeight) {
		requires(Robot.drive);
		distance = wantedHeight;
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("height position", Robot.oi.elevator.getSelectedSensorPosition(0));
		if (Robot.oi.elevator.getSelectedSensorPosition(0) >= desiredHeight) {
			Robot.oi.elevator.set(0);
		}
	}
	@Override
	protected boolean isFinished() {
		if (Robot.oi.elevator.getSelectedSensorPosition(0) >= desiredHeight) {
			return true;
		}
		else {
			return false;
		}
	}
}

