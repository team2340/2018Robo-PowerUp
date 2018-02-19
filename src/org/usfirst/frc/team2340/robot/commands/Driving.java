package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Driving extends Command {
	private Joystick controller;

	public Driving() {
		requires(Robot.drive);
		controller = Robot.oi.driveController;
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Gyro angle", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoder());
		SmartDashboard.putNumber("right position ", Robot.drive.getRightEncoder());

		double x, y, z;

		z = (3 - controller.getZ()) / 2;
		y = -controller.getY() / z;
		x = controller.getX() / z;

		Robot.drive.setArcadeSpeed(x, y);
		System.out.println("ArcadeSpeed x:" + x + " y:" + y);
	}

	@Override
	protected void end() {
		Robot.drive.stop();
	}

	@Override
	protected void interrupted() {
		end();
	}

	@Override
	protected boolean isFinished() {
		return false;
	}
}
