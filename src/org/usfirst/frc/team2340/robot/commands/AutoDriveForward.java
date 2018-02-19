package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.DriveCorrection;
import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	long startTime = 0;
	double distanceEncoder = 0;
	double distanceInches = 0;
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
		distanceInches = _howFar;
		corr = _corr;
		gyroCorrLimit = _gyroCorrLimit;
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		distanceEncoder = RobotUtils.getEncPositionFromIN(distanceInches);
		Robot.myLogger.log("AutoDrive", "", "");
		Robot.myLogger.log("AutoDrive", "Desired Distance (in)", distanceInches);
		Robot.myLogger.log("AutoDrive", "Desired Position", RobotUtils.distanceMinusRobot(distanceInches));
		Robot.drive.resetEncoders(); // TODO: do we have to reset the encoders?
		Robot.drive.move(distanceEncoder, ControlMode.Position);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoder());
		SmartDashboard.putNumber("right position ", Robot.drive.getRightEncoder());
		Robot.myLogger.log("AutoDrive", "Left Encoder", Robot.drive.getLeftEncoder());
		Robot.myLogger.log("AutoDrive", "Right Encoder", Robot.drive.getRightEncoder());

		double correction = Robot.drive.speedP
				- (Robot.drive.speedP * (.5 * (Math.abs(Robot.oi.gyro.getAngle()) / 3.0)));
		if (correction < 0) {
			correction = 0;
		}

		if (Robot.oi.gyro.getAngle() > 1) {
			Robot.myLogger.log("AutoDrive", "Correct Left", correction);
			Robot.oi.left.config_kP(0, correction, 0);
			Robot.oi.left.set(ControlMode.Position, distanceEncoder);
		}
		else if (Robot.oi.gyro.getAngle() < -1) {
			Robot.myLogger.log("AutoDrive", "Correct Right", correction);
			Robot.oi.right.config_kP(0, correction, 0);
			Robot.oi.right.set(ControlMode.Position, -distanceEncoder);
		}
		else {
			Robot.oi.right.config_kP(0, Robot.drive.speedP, 0);
			Robot.oi.left.config_kP(0, Robot.drive.speedP, 0);
			Robot.oi.left.set(ControlMode.Position, distanceEncoder);
			Robot.oi.right.set(ControlMode.Position, -distanceEncoder);
		}

		// TODO: Check if current spike or encoder stopped for hitting a wall
	}

	protected boolean done() {
		int leftPos = Math.abs(Robot.drive.getLeftEncoder());
		int rightPos = Math.abs(Robot.drive.getRightEncoder());

		if ((leftPos <= distanceEncoder + 2500 && leftPos >= distanceEncoder - 2500)
				&& (rightPos <= distanceEncoder + 2500 && rightPos >= distanceEncoder - 2500)) {
			Robot.myLogger.log("AutoDrive", "Complete");
			Robot.myLogger.log("AutoDrive", "Left Encoder", Robot.drive.getLeftEncoder());
			Robot.myLogger.log("AutoDrive", "Right Encoder", Robot.drive.getRightEncoder());
			Robot.myLogger.log("AutoDrive", "Elapsed", (System.currentTimeMillis() - startTime) / 1000);
			Robot.myLogger.log("AutoDrive", "Current angle: ", Robot.oi.gyro.getAngle());
			
			if (corr != null) {
				corr.correction(distanceInches, Robot.oi.gyro.getAngle(), gyroCorrLimit);
			}
			Robot.myLogger.log("AutoDrive", "Result X Correction", corr.getCorrection().x);
			Robot.myLogger.log("AutoDrive", "Result Y Correction", corr.getCorrection().y);
			Robot.myLogger.logBreak();
			Robot.drive.resetEncoders();
			return true;
		}
		return false;
	}

	@Override
	protected boolean isFinished() {
		return done();
	}
}
