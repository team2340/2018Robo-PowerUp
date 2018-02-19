package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoElevator extends Command {
	long startTime = 0;
	double desiredHeight = 0;
	double distance = 0;

	public  AutoElevator(double wantedHeight) {
		requires(Robot.elevator);
		distance = wantedHeight;
	}
	
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		desiredHeight = RobotUtils.getEncPositionFromIN(distance);
		Robot.elevator.movePosition(desiredHeight);
		Robot.myLogger.log("AutoElevator", "Start");
		Robot.myLogger.log("AutoElevator", "Desired Distance (in)", distance);
		Robot.myLogger.log("AutoElevator", "Desired Position", desiredHeight);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("height position", Robot.elevator.getEncoder());
		Robot.myLogger.log("AutoElevator", "Encoder", Robot.elevator.getEncoder());
	}
	
	protected void interrupted() {
		Robot.myLogger.log("AutoElevator","Interrupted!");
		Robot.elevator.stop();
	}
	
	protected boolean done() {
		int position = Math.abs(Robot.elevator.getEncoder());

		if (position <= desiredHeight + 2500 && position >= desiredHeight - 2500) {
			Robot.myLogger.log("AutoElevator", "Complete");
			Robot.myLogger.log("AutoElevator", "Encoder", Robot.elevator.getEncoder());
			Robot.myLogger.log("AutoElevator", "Elapsed", (System.currentTimeMillis() - startTime) / 1000);
			Robot.myLogger.logBreak();
			return true;
		}
		return false;
	}
	
	@Override
	protected boolean isFinished() {
		return done();
	}
}
