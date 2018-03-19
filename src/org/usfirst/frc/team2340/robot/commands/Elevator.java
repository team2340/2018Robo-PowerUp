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
	protected void initialize() { //853333 -> 55in: 15515.145enc/in
		Robot.elevator.setPosition();
		Robot.myLogger.log("Elevator","desiredHeight", distance);
		startTime = System.currentTimeMillis();
		desiredHeight = RobotUtils.getEncPositionFromINElevator(distance);
		Robot.elevator.movePosition(desiredHeight);
		Robot.myLogger.log("Elevator","desiredHeight enc", desiredHeight);
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
		if (Math.abs(Robot.elevator.getEncoder()) <= (desiredHeight + 50)
			&& Math.abs(Robot.elevator.getEncoder()) >= (desiredHeight - 50)) {
			Robot.myLogger.log("Elevator","Done",Robot.elevator.getEncoder());
			return true;
		}
		else {
			return false;
		}
	}
}

