package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoDriveForward extends Command {
	long startTime = 0;
	boolean rDone, lDone;
	double desiredSpot = 0;
	double desiredDistanceInches = 0;

	public AutoDriveForward(double _desiredDistanceInches) {
		requires(Robot.drive);
		desiredDistanceInches = _desiredDistanceInches;
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceFromRobotFront(desiredDistanceInches));
		Robot.oi.left.set(desiredSpot);
		Robot.oi.right.set(desiredSpot);
	}

	@Override
	protected void execute() {
		long elapsed = (System.currentTimeMillis() - startTime)/1000;

		SmartDashboard.putNumber("Auto Drive Forward Elapsed", elapsed);
		SmartDashboard.putNumber("Auto Drive Forward Desired Distance (in)", desiredDistanceInches);
		
		if (!lDone || !rDone) {
			if(Robot.oi.right.getSelectedSensorPosition(0) >= desiredSpot){
				Robot.oi.right.setSelectedSensorPosition(0,0,0);
				Robot.oi.right.set(0);
				rDone = true;
				System.out.println("RDONE");
			}
			if(Robot.oi.left.getSelectedSensorPosition(0) >= desiredSpot){
				Robot.oi.left.setSelectedSensorPosition(0,0,0);
				Robot.oi.left.set(0);
				lDone = true;
				System.out.println("LDONE");
			}
		}
	}

	@Override
	protected boolean isFinished() {
		System.out.println("AUTO DRIVE FORWARD DONE");
		return rDone && lDone; 
	}
}
