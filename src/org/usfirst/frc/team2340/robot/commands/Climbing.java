package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class Climbing extends Command {
	private Joystick controller;

	public Climbing() {
		requires(Robot.climbing);
	}
	
	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
	}

	@Override
	protected void execute() {
		if(controller.getRawButton(RobotMap.BUTTON_2)&&(!controller.getRawButton(RobotMap.BUTTON_1))){
			Robot.climbing.up(1, ControlMode.PercentOutput);
		}
		else if(controller.getRawButton(RobotMap.BUTTON_2)&&(controller.getRawButton(RobotMap.BUTTON_1))){
			Robot.climbing.down(1, ControlMode.PercentOutput);
		}
		else{
			Robot.climbing.stop();
		}
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}

}
