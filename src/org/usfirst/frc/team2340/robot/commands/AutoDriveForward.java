package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.DriveCorrection;
import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	double desiredSpot = 0;
	DriveCorrection corr = null;
	double gyroCorrLimit;

	public AutoDriveForward(double _howFar) {
		this(_howFar, null);
	}
	
	public AutoDriveForward(double _howFar, DriveCorrection _corr) {
		this(_howFar, _corr, -1);
	}
	
	public AutoDriveForward(double _howFar, DriveCorrection _corr, double _gyroCorrLimit) {
		requires(Robot.drive);
		desiredSpot = RobotUtils.getEncPositionFromIN(_howFar);
		corr = _corr;
		gyroCorrLimit = _gyroCorrLimit;
	}

	@Override
	protected void initialize() {
		Robot.drive.resetEncoders();
		Robot.drive.move(desiredSpot, ControlMode.Position);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoderValue());
		SmartDashboard.putNumber("right position ", Robot.drive.getRightEncoderValue());

		if (Robot.drive.getRightEncoderValue() >= desiredSpot) {
			Robot.drive.stop();
		}
		if (Robot.drive.getLeftEncoderValue() >= desiredSpot) {
			Robot.drive.stop();
		}
	}

	@Override
	protected boolean isFinished() {
		if(Robot.drive.getLeftEncoderValue() >= desiredSpot
		   && Robot.drive.getRightEncoderValue() >= desiredSpot) {
			if(corr != null) {
				corr.correction(desiredSpot, Robot.oi.gyro.getAngle(), gyroCorrLimit);
			}
			Robot.drive.resetEncoders();
			return true; 
		}
		else {
			return false;
		}
	}
}
