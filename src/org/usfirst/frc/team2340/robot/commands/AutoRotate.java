package org.usfirst.frc.team2340.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team2340.robot.Robot;

public class AutoRotate extends Command {
	long startTime = 0;
	boolean rotateRight = false;
	double desiredDegrees = 0;
	boolean rotateComplete = false;
	
	public AutoRotate(double _degrees, boolean _right) {
		requires(Robot.drive);
		desiredDegrees = _degrees;
		rotateRight = _right;
	}
	
	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
	}
	
	@Override
	protected void execute() {
		long elapsed = (System.currentTimeMillis() - startTime)/1000;
		SmartDashboard.putNumber("Auto Rotate Elapsed", elapsed);
		SmartDashboard.putBoolean("Auto Rotate Rotate Right ", rotateRight);
		
		rotateComplete = true;
		double currentAngle = Math.abs(Robot.oi.gyro.getAngle());
		if(currentAngle < desiredDegrees)
		{
			rotateComplete = false;
			Robot.drive.setForSpeed(); //TODO:Get current mode, if not speed, do this
			double speed = 2.5*(desiredDegrees - currentAngle) + 10; //Made up formula that works
			if(rotateRight)
			{
				Robot.oi.left.set(speed);
				Robot.oi.right.set(-speed);
			}
			else
			{
				Robot.oi.left.set(-speed);
				Robot.oi.right.set(speed);
			}
		}
	}
	
	@Override
	protected boolean isFinished() {
		System.out.println("AUTO ROTATE DONE");
		return rotateComplete;
	}

}
