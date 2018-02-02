package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Rotation extends Command {
	long startTime = 0;
	double desiredAngle = 0;
	boolean rotateRight;
	double angle= 0;

	public Rotation(double wantedAngle, boolean rightRotate) {
		requires(Robot.drive);
		desiredAngle = wantedAngle;
		rotateRight = rightRotate;
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	protected void execute() {
		angle = Math.abs(Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Gyro angle", angle);

		if (angle >= desiredAngle) {
			Robot.oi.left.set(0);
			Robot.oi.right.set(0);
		}
		else {
//			2.5 * (desiredAngle - angle) + 10
			if (rotateRight) {
				Robot.oi.left.set(5);
				Robot.oi.right.set(-5);
			}
			else {
				Robot.oi.left.set(-5);
				Robot.oi.right.set(5);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		if (angle >= desiredAngle) {
			System.out.println("Auto Rotate Desired Angle: " + desiredAngle
					+ ", End Angle: " + angle
					+ ", Right: " + rotateRight
					+ ", Elapsed: " + (System.currentTimeMillis() - startTime)/1000);
			return true;
		}
		else {
			return false;
		}
	}
};