package org.usfirst.frc.team2340.robot.commands;


import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ClimbingCommand extends Command {
	private Joystick controller;
	double currentTime;
	public ClimbingCommand(){
		requires(Robot.climbing);
	}

	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
	}

	@Override
	protected void execute() {
		if(controller.getRawButton(RobotMap.BUTTON_2)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))){
			{
				Robot.climbing.move(1);
			}
		}
		else if(controller.getRawButton(RobotMap.BUTTON_2 )&&(controller.getRawButton(RobotMap.BUTTON_1 ))){
			{
				Robot.climbing.move(-1);
			}

		}
		else{
			Robot.oi.climbing.set(0);
		}
		
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}
}
