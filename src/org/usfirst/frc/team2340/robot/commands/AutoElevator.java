package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

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
		Robot.elevator.move(desiredHeight, ControlMode.Position);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("height position", Robot.elevator.getEncoderValue());
		if (Robot.elevator.getEncoderValue() >= desiredHeight) {
			Robot.elevator.stop();
		}
	}
	
	protected void interrupted() {
		System.out.println("Interrupted!");
		Robot.elevator.stop();
	}
	
	@Override
	protected boolean isFinished() {
		if (Robot.elevator.getEncoderValue() >= desiredHeight) {
			return true;
		}
		else {
			return false;
		}
	}
}
