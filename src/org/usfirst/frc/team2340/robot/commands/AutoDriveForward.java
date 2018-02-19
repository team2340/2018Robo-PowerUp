package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	long startTime = 0;
	boolean rDone = false, lDone = false;
	double desiredSpot = 0;
	double distance = 0;

	public AutoDriveForward(double howFar) {
		requires(Robot.drive);
		distance = howFar;
	}

	@Override
	protected void initialize() {
		Robot.drive.setForPosition();
		Robot.myLogger.log("AutoDriveForward","", "");
		startTime = System.currentTimeMillis();
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(distance));
		Robot.myLogger.log("desired","spot", RobotUtils.distanceMinusRobot(distance));
		Robot.oi.left.set(ControlMode.Position, desiredSpot);
		Robot.oi.right.set(ControlMode.Position, -desiredSpot);
		System.out.println("desired spot " + desiredSpot);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("Current angle: ", Robot.oi.gyro.getAngle());
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSelectedSensorPosition(0));

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
			long elapsed = (System.currentTimeMillis() - startTime) / 1000;
			System.out.println("Auto Drive Foward Distance: " + distance + ", Elapsed: " + elapsed);
			System.out.println("Current angle: "+ Robot.oi.gyro.getAngle());
		}
		return done();
	}
}
