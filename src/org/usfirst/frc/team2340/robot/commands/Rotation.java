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
	double turnAngle=0;

	public Rotation(double wantedAngle) {
		requires(Robot.drive);
		desiredAngle = wantedAngle;
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		Robot.drive.setForSpeed();
		Robot.myLogger.log("Rotation","Start", startTime);
		Robot.myLogger.log("Rotation","desiredAngle", desiredAngle);
		Robot.myLogger.log("Rotation","rotationDirection", (rotateRight) ? "right" : "left");
		Robot.myLogger.log("Rotation","currentAngle", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		turnAngle = desiredAngle - Robot.oi.gyro.getAngle();
		Robot.oi.gyro.reset();
		if (turnAngle > 0) {
			rotateRight = true;
		}
		else {
			rotateRight = false;
		}
		Robot.myLogger.log("Rotation","turnAngle", turnAngle);
		turnAngle = Math.abs(turnAngle);
	}
	
	@Override
	protected void execute() {
		angle = Math.abs(Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Gyro angle", angle);
		Robot.myLogger.log("Rotation","angle", angle);

		if (angle >= turnAngle) {
			Robot.oi.left.set(ControlMode.Velocity, 0);
			Robot.oi.right.set(ControlMode.Velocity, 0);
		}
		else {
			double t = 1023*((turnAngle - angle)/turnAngle);
			if (t < 1000) t = 1000; //was 800
			double rotateSpeed = t;
			if (rotateRight) {
				Robot.oi.left.set(ControlMode.Velocity, rotateSpeed);
				Robot.oi.right.set(ControlMode.Velocity, rotateSpeed);
			}
			else {
				Robot.oi.left.set(ControlMode.Velocity, -rotateSpeed);
				Robot.oi.right.set(ControlMode.Velocity, -rotateSpeed);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		if (angle >= turnAngle) {
			Robot.myLogger.log("Rotation", "Done", (System.currentTimeMillis() - startTime)/1000);
			Robot.myLogger.log("Rotation", "endAngle", angle);
			Robot.oi.left.set(0);
			Robot.oi.right.set(0);

			Robot.oi.gyro.reset();
			return true;
		}
		else {
			return false;
		}
	}
};