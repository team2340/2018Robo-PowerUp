package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.DriveCorrection;
import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	double desiredSpot = 0;
	double distance = 0;
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
		distance = _howFar;
		corr = _corr;
		gyroCorrLimit = _gyroCorrLimit;
	}

	@Override
	protected void initialize() {
		desiredSpot = RobotUtils.getEncPositionFromIN(distance);
		Robot.myLogger.log("AutoDriveForward","", "");
		Robot.myLogger.log("desired","spot", RobotUtils.distanceMinusRobot(distance));
		System.out.println("desired spot " + desiredSpot);
		Robot.drive.resetEncoders(); //TODO: do we have to reset the encoders?
		Robot.drive.move(desiredSpot, ControlMode.Position);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoderValue());
		SmartDashboard.putNumber("right position ", Robot.drive.getRightEncoderValue());
		
		System.out.println("desired spot " + desiredSpot);
		System.out.println("left position " + Robot.oi.left.getSelectedSensorPosition(0));
		System.out.println("right position " + Robot.oi.right.getSelectedSensorPosition(0));
		
		Robot.myLogger.log("leftencoder","position", Robot.oi.left.getSelectedSensorPosition(0));
		Robot.myLogger.log("rightencoder","position", Robot.oi.right.getSelectedSensorPosition(0));

		if (Robot.oi.gyro.getAngle()>1){
			System.out.println("Rotate to the left!!!!!!!!!");
			double correction =Robot.drive.speedP-(Robot.drive.speedP * (.5 * (Math.abs(Robot.oi.gyro.getAngle())/ 3.0) ) );
			if (correction < 0 ) {
				correction = 0;
			}
			System.out.println("Correct left " + correction);
			Robot.oi.left.config_kP(0,correction,0);
			
			
			Robot.oi.left.set(ControlMode.Position, desiredSpot);
		}
		else if (Robot.oi.gyro.getAngle()<-1) {
			System.out.println("Rotate to the right!!!!!!!!!!!!!");
			double correction =Robot.drive.speedP-(Robot.drive.speedP * (.5 * (Math.abs(Robot.oi.gyro.getAngle())/ 3.0) ) );
			if (correction < 0 ) {
				correction = 0;
			}
			System.out.println("Correct right " + correction);
			Robot.oi.right.config_kP(0, correction , 0);
			
			Robot.oi.right.set(ControlMode.Position, -desiredSpot);
			
		}
		else {
			System.out.println("Don't rotate!!!!!");
			Robot.oi.right.config_kP(0,Robot.drive.speedP,0);
			Robot.oi.left.config_kP(0,Robot.drive.speedP,0);
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

		if((leftPos <= desiredSpot+2500 && leftPos >= desiredSpot-2500)
				&& (rightPos <= desiredSpot+2500 && rightPos >= desiredSpot-2500))
		{
			System.out.println("DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
			return true;
		}
		return false;
	}
	@Override
	protected boolean isFinished() {
		if(done()) {
			System.out.println("DONE");
			System.out.println("desired spot " + desiredSpot);
			System.out.println("left position " + Robot.oi.left.getSelectedSensorPosition(0));
			System.out.println("right position " + Robot.oi.right.getSelectedSensorPosition(0));
//			long elapsed = (System.currentTimeMillis() - startTime) / 1000;
//			System.out.println("Auto Drive Foward Distance: " + distance + ", Elapsed: " + elapsed);
			System.out.println("Current angle: "+ Robot.oi.gyro.getAngle());
			if(corr != null) {
				corr.correction(distance, Robot.oi.gyro.getAngle(), gyroCorrLimit);
			}
			Robot.drive.resetEncoders();
		}
		return done();
	}
}
