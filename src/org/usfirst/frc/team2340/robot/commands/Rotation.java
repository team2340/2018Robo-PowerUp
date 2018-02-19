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
		Robot.drive.setForSpeed();
		Robot.myLogger.log("Rotation","desiredAngle", desiredAngle);
		Robot.myLogger.log("Rotation","rotationDirection", (rotateRight) ? "right" : "left");
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		System.out.println("Current angle: " + Robot.oi.gyro.getAngle());
		turnAngle = desiredAngle - Robot.oi.gyro.getAngle();
		Robot.oi.gyro.reset();
		if (turnAngle >0 ) {
			rotateRight = true;
		}
		else {
			rotateRight = false;
		}
		startTime = System.currentTimeMillis();
		Robot.oi.gyro.reset();
		turnAngle = Math.abs(turnAngle);
	}
	
	@Override
	protected void execute() {
		angle = Math.abs(Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("Gyro angle", angle);
		System.out.println("Gyro angle" + angle);

		if (angle >= turnAngle) {
			Robot.drive.stop();
		}
		else {
			double rotateSpeed = ((turnAngle - angle)/turnAngle)+700;
			if (rotateRight) {
				Robot.drive.move(rotateSpeed, -rotateSpeed, ControlMode.Velocity);
			}
			else {
				Robot.drive.move(-rotateSpeed, rotateSpeed, ControlMode.Velocity);
			}
		}
	}

	@Override
	protected boolean isFinished() {
		if (angle >= turnAngle) {
			System.out.println("Auto Rotate  TurningAngle: " + turnAngle
					+ ", End Angle: " + angle
					+ ", Right: " + rotateRight
					+ ", Elapsed: " + (System.currentTimeMillis() - startTime)/1000);
			Robot.drive.stop(); //TODO: Necessary?

			Robot.oi.gyro.reset();
			return true;
		}
		else {
			return false;
		}
	}
};