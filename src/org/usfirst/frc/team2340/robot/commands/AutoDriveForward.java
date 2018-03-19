package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	long startTime = 0;
	double desiredSpot = 0;
	double distance = 0;

	public AutoDriveForward(double howFar) {
		requires(Robot.drive);
		distance = howFar;
	}

	@Override
	protected void initialize() {
		Robot.oi.gyro.reset();
		Robot.drive.setForPosition();
		Robot.myLogger.log("AutoDriveForward","", "");
		startTime = System.currentTimeMillis();
		double go = RobotUtils.distanceMinusRobot(distance);
		desiredSpot = RobotUtils.getEncPositionFromIN(go);
		Robot.myLogger.log("AutoDriveForward","distance", go);
		Robot.myLogger.log("AutoDriveForward", "Desired Spot (enc)", desiredSpot);
		Robot.oi.left.set(ControlMode.Position, desiredSpot);
		Robot.oi.right.set(ControlMode.Position, -desiredSpot);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSelectedSensorPosition(0));

		Robot.myLogger.log("leftencoder","position", Robot.oi.left.getSelectedSensorPosition(0));
		Robot.myLogger.log("rightencoder","position", Robot.oi.right.getSelectedSensorPosition(0));
		Robot.myLogger.log( "Gyro","angle", Robot.oi.gyro.getAngle());
		if (Robot.oi.gyro.getAngle()>1){
//			System.out.println("Rotate to the left!!!!!!!!! gyro: " + Robot.oi.gyro.getAngle());
			double correction =Robot.drive.positionP-(Robot.drive.positionP * (.5 * (Math.abs(Robot.oi.gyro.getAngle())/ 3.0) ) );
			if (correction < 0 ) {
				correction = 0;
			}
			Robot.myLogger.log("Correct", "left", correction);
			Robot.oi.left.config_kP(0,correction,0);
			Robot.oi.left.set(ControlMode.Position, desiredSpot);
			
			Robot.oi.right.config_kP(0,Robot.drive.positionP,0);
			Robot.oi.right.set(ControlMode.Position, -desiredSpot);
		}
		else if (Robot.oi.gyro.getAngle()<-1) {
			
			double correction =Robot.drive.positionP-(Robot.drive.positionP * (.5 * (Math.abs(Robot.oi.gyro.getAngle())/ 3.0) ) );
//			System.out.println("Rotate to the right!!!!!!!!!!!!! gyro: " + Robot.oi.gyro.getAngle() );
			if (correction < 0 ) {
				correction = 0;
			}
			Robot.myLogger.log("Correct", "right", correction);
			Robot.oi.right.config_kP(0, correction , 0);
			
			Robot.oi.right.set(ControlMode.Position, -desiredSpot);
			Robot.oi.left.config_kP(0,Robot.drive.positionP,0);
			Robot.oi.left.set(ControlMode.Position, desiredSpot);
			
		}
		else {
			System.out.println("Don't rotate!!!!!");
			Robot.oi.right.config_kP(0,Robot.drive.positionP,0);
			Robot.oi.left.config_kP(0,Robot.drive.positionP,0);
			Robot.oi.left.set(ControlMode.Position, desiredSpot);
			Robot.oi.right.set(ControlMode.Position, -desiredSpot);
		}
	}
	//TODO: Check if current spike or encoder stopped 
	//		System.out.println("PID ERROR right " + Robot.oi.right.getClosedLoopError(0));
	//		System.out.println("PID ERROR left " + Robot.oi.left.getClosedLoopError(0));


	protected boolean done() {
		int leftPos = Math.abs(Robot.oi.left.getSelectedSensorPosition(0));
		int rightPos = Math.abs(Robot.oi.right.getSelectedSensorPosition(0));
		//		int leftErr = Math.abs(Robot.oi.left.getClosedLoopError(0));
		//		int rightErr = Math.abs(Robot.oi.right.getClosedLoopError(0));

		if((leftPos <= desiredSpot+500 && leftPos >= desiredSpot-500)
				&& (rightPos <= desiredSpot+500 && rightPos >= desiredSpot-500))
		{
			Robot.myLogger.log("AutoDriveForward", "Done", (System.currentTimeMillis() - startTime) / 1000);
			Robot.myLogger.log("AutoDriveForward", "Angle", Robot.oi.gyro.getAngle());
			return true;
		}
		return false;
	}

	@Override
	protected boolean isFinished() {
		return done();
	}
}
