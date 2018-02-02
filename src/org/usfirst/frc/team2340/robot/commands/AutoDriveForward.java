package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.RobotUtils;

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
		startTime = System.currentTimeMillis();
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(distance));
		Robot.oi.left.set(desiredSpot);
		Robot.oi.right.set(desiredSpot);
	}

	@Override
	protected void execute() {
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSelectedSensorPosition(0));
		
		if (!lDone || !rDone) {
			if(Robot.oi.right.getSelectedSensorPosition(0) >= desiredSpot){
				Robot.oi.right.setSelectedSensorPosition(0,0,0);
				Robot.oi.right.set(0);
				rDone = true;
//				RobotMap.TAKE_PIC = true;
				System.out.println("RDONE");
			}
			if(Robot.oi.left.getSelectedSensorPosition(0) >= desiredSpot){
				Robot.oi.left.setSelectedSensorPosition(0,0,0);
				Robot.oi.left.set(0);
				lDone = true;
//				RobotMap.TAKE_PIC = true;
				System.out.println("LDONE");
			}
		}
	} 

	@Override
	protected boolean isFinished() {
		if(rDone && lDone) {
			long elapsed = (System.currentTimeMillis() - startTime)/1000;
			System.out.println("Auto Drive Foward Distance: " + distance + ", Elapsed: " + elapsed);
			return true; 
		}
		else {
			return false;
		}
	}
}
