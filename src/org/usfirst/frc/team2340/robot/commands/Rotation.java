package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

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
		Robot.oi.gyro.reset();
	}
	
	@Override
	protected void execute() {
		angle = Math.abs(Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Gyro angle", angle);

		if (angle >= desiredAngle) {
			Robot.drive.stop();
		}
		else {
			double rotateVal = 5; // 2.5 * (desiredAngle - angle) + 10
			if (rotateRight) {
				Robot.drive.move(rotateVal, -rotateVal, ControlMode.Current);
			}
			else {
				Robot.drive.move(-rotateVal, rotateVal, ControlMode.Current);
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