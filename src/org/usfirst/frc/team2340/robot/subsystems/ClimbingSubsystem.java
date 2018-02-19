package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.ClimbingCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ClimbingSubsystem extends Subsystem {
	static private ClimbingSubsystem subsystem;
	private ClimbingSubsystem() {
	createClimbing();
		
	}

	public static ClimbingSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new ClimbingSubsystem();
		}
		return subsystem;
	}
	private void createClimbing() {
		try {
			Robot.oi.climbing = new WPI_TalonSRX(RobotMap.CLIMBING_TAL_ID);
			Robot.oi.climbing.configNominalOutputForward(0,0);
			Robot.oi.climbing.configNominalOutputReverse(0,0);
		    Robot.oi.climbing.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("climbing FAILED");
			
		}
	}
	public void move(double amt) {
		Robot.oi.climbing.set(amt);
	}
	
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ClimbingCommand());
	}
}
