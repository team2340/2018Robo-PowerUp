package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	double desiredSpot = 0;

	public AutoDriveForward(double _howFar) {
		requires(Robot.drive);
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(_howFar));
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
			Robot.drive.resetEncoders();
			return true; 
		}
		else {
			return false;
		}
	}
}
