package org.usfirst.frc.team2340.robot.commands;


import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.RobotUtils;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
//import edu.wpi.first.wpilibj.command.Subsystem; I comented this out hope that is ok

public class AcquisitionCommand extends Command {
	private Joystick controller;
	double currentTime;
	private boolean button7Pressed,on;
	public AcquisitionCommand(){
		requires(Robot.acquisition);
	}

	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
		button7Pressed = false;
	}

	@Override
	protected void execute() {
		double y, z;

		y = -controller.getY();
		if(y!= 0) {
			Robot.oi.elevator.set(y);
		}
		else {

			if(controller.getRawButton(RobotMap.BUTTON_5)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))){
				Robot.oi.elevator.set(1);
			}
			else if(controller.getRawButton(RobotMap.BUTTON_5)&&(controller.getRawButton(RobotMap.BUTTON_1))){
				Robot.oi.elevator.set(-1);
			}
			else{
				Robot.oi.elevator.set(0);
			}
		}
		z = (1+controller.getZ())/2;


		if(controller.getRawButton(RobotMap.BUTTON_3)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))){
			if(z!=0) {
				Robot.oi.armone.set(z);
				Robot.oi.armtwo.set(z);
			}
		}
		else if(controller.getRawButton(RobotMap.BUTTON_3 )&&(controller.getRawButton(RobotMap.BUTTON_1 ))){
			if(z!=0) {
				Robot.oi.armone.set(-z);
				Robot.oi.armtwo.set(-z);
			}

		}
		else{
			Robot.oi.armone.set(0);
			Robot.oi.armtwo.set(0);
		}
		if(controller.getRawButton(RobotMap.BUTTON_4)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))) {
			double desiredHeight = RobotUtils.getEncPositionFromIN(77);
			Robot.oi.elevator.set(desiredHeight-Robot.oi.elevator.getSelectedSensorPosition(0));	

		}
		else if(controller.getRawButton(RobotMap.BUTTON_4)&&(controller.getRawButton(RobotMap.BUTTON_1))){
			double desiredHeight =(Robot.oi.elevator.getSelectedSensorPosition(0)*-1);
			Robot.oi.elevator.set(desiredHeight);	
		}
		else if(controller.getRawButton(RobotMap.BUTTON_6)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))) {
			double desiredHeight = RobotUtils.getEncPositionFromIN(19);
			Robot.oi.elevator.set(desiredHeight-Robot.oi.elevator.getSelectedSensorPosition(0));	

		}
		if(controller.getRawButton(RobotMap.BUTTON_2)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))){
			{
				Robot.oi.climbing.set(1);
			}
		}
		else if(controller.getRawButton(RobotMap.BUTTON_2 )&&(controller.getRawButton(RobotMap.BUTTON_1 ))){
			{
				Robot.oi.climbing.set(-1);
			}

		}
		else{
			Robot.oi.climbing.set(0);
		}
		if (controller.getRawButton(RobotMap.BUTTON_7)){
			if(!button7Pressed)
			{
				toggleAq();
				button7Pressed = true;
			}
		}
		else
		{
			button7Pressed = false;
		}

	}


	private void toggleAq() {
		if(!on) {
			Robot.acquisition.open();
			on = true;
		}
		else {
			Robot.acquisition.close();
			on = false;
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
