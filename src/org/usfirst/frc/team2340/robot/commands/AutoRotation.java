package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoRotation extends Command {
	long startTime = 0;
	double desiredAngle = 0;
	boolean rotateRight;
	double angle = 0;
	double turnAngle = 0;

	public AutoRotation(double wantedAngle) {
		requires(Robot.drive);
		desiredAngle = wantedAngle;
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		turnAngle = desiredAngle - Robot.oi.gyro.getAngle();
		if (turnAngle > 0) {
			rotateRight = true;
		}
		else {
			rotateRight = false;
		}
		turnAngle = Math.abs(turnAngle);

		Robot.myLogger.log("Rotation", "Desired Angle", desiredAngle);
		Robot.myLogger.log("Rotation", "Turn Angle", turnAngle);
		Robot.myLogger.log("Rotation", "Rotation Direction", (rotateRight) ? "right" : "left");
		Robot.myLogger.log("Gyro", "Current Angle", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		
		Robot.oi.gyro.reset();
	}

	@Override
	protected void execute() {
		angle = Math.abs(Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Gyro angle", angle);
		Robot.myLogger.log("Gyro", "Current Angle", angle);

		double rotateSpeed = ((turnAngle - angle) / turnAngle) + 700;
		if (rotateRight) {
			Robot.drive.move(rotateSpeed, -rotateSpeed, ControlMode.Velocity);
		}
		else {
			Robot.drive.move(-rotateSpeed, rotateSpeed, ControlMode.Velocity);
		}
	}

	@Override
	protected boolean isFinished() {
		if (angle >= turnAngle) {
			Robot.myLogger.log("Rotation", "End Angle", angle);
			Robot.myLogger.log("Rotation", "Elapsed", (System.currentTimeMillis() - startTime) / 1000);
			Robot.myLogger.logBreak();
			Robot.drive.stop();
			Robot.oi.gyro.reset(); //Necessary? Yes, if we want to stay absolute!
			return true;
		}
		else {
			return false;
		}
	}
};
