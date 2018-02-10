package org.usfirst.frc.team2340.robot.commands;
import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoOneSwitchRight extends Command {
	long startTime = 0;
	boolean rDone, lDone;
	double desiredSpot = 0;

	public AutoOneSwitchRight() {
		requires(Robot.drive);
	}

	@Override
	protected void initialize() {
		startTime = System.currentTimeMillis();
		double distancesToSwitch = 168 + (RobotUtils.getLengthOfRobot()*.5);
		desiredSpot = RobotUtils.getEncPositionFromIN(RobotUtils.distanceMinusRobot(distancesToSwitch));
		Robot.oi.left.set(desiredSpot);
		Robot.oi.right.set(-desiredSpot);
	}

	@Override
	
	protected void execute() {
		long elapsed = (System.currentTimeMillis() - startTime)/1000;

		SmartDashboard.putNumber("left position", Robot.oi.left.getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("Auto Elapsed", elapsed);
		
		if (!lDone || !rDone) {
			if(Robot.oi.right.getSensorCollection().getQuadraturePosition()<=-desiredSpot){
				Robot.oi.right.getSensorCollection().setQuadraturePosition(0,0);
				Robot.oi.right.set(0);
				rDone = true;
				RobotMap.TAKE_PIC = true;
				System.out.println("RDONE");
			}
			if(Robot.oi.left.getSensorCollection().getQuadraturePosition()>=desiredSpot){
				Robot.oi.left.getSensorCollection().setQuadraturePosition(0,0);
				Robot.oi.left.set(0);
				lDone = true;
				RobotMap.TAKE_PIC = true;
				System.out.println("LDONE");
			}
		}

		if(lDone && rDone) {
			RotateLeft();
		}
	}
	
	protected boolean RotateLeft(){
		double angle = Robot.oi.gyro.getAngle();
		double desiredAngle = 90;
		if(angle <= -desiredAngle){
			Robot.drive.setForPosition();
			Robot.oi.left.set(0);
			Robot.oi.right.set(0);
			System.out.print("ROTATE done");
			RobotMap.TAKE_PIC = true;
			return true;
		}
		else{
			Robot.drive.setForSpeed();
			Robot.oi.left.set(-2.5 * (desiredAngle + angle) - 10);
			Robot.oi.right.set(-2.5 * (desiredAngle + angle) - 10);
			return false;
		}
	}

	@Override
	protected boolean isFinished() {
		return System.currentTimeMillis() -startTime >= 15000; 
	}

	@Override
	protected void end() {
		Robot.drive.setForVBus();
	}

	@Override
	protected void interrupted() {
	}
}



