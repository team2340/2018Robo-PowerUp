package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.ButtonAction;
import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.RobotUtils;
import org.usfirst.frc.team2340.robot.SuperJoystick;

import edu.wpi.first.wpilibj.command.Command;

public class Elevator extends Command {
	private SuperJoystick controller;

	public Elevator() {
		requires(Robot.elevator);
	}
	
	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;

		controller.toggleWhenPressed(
				new AutoElevator(RobotUtils.getEncPositionFromIN(77) - Robot.elevator.getEncoder()),
				new ButtonAction(RobotMap.BUTTON_4, ButtonAction.Action.Pressed),
				new ButtonAction(RobotMap.BUTTON_1, ButtonAction.Action.NotPressed));
		
		controller.toggleWhenPressed(
				new AutoElevator(Robot.elevator.getEncoder()*-1),
				new ButtonAction(RobotMap.BUTTON_4, ButtonAction.Action.Pressed),
				new ButtonAction(RobotMap.BUTTON_1, ButtonAction.Action.Pressed));
		
		controller.toggleWhenPressed(
				new AutoElevator(RobotUtils.getEncPositionFromIN(19)-Robot.elevator.getEncoder()),
				new ButtonAction(RobotMap.BUTTON_6, ButtonAction.Action.Pressed),
				new ButtonAction(RobotMap.BUTTON_1, ButtonAction.Action.NotPressed));
	}

	@Override
	protected void execute() {
		double y = -controller.getY();
		if (y != 0) {
			Robot.elevator.move(y);
		}
		else if (controller.getRawButton(RobotMap.BUTTON_5) && (!controller.getRawButton(RobotMap.BUTTON_1))) {
			Robot.elevator.up(1);
		}
		else if (controller.getRawButton(RobotMap.BUTTON_5) && (controller.getRawButton(RobotMap.BUTTON_1))) {
			Robot.elevator.down(-1);
		}
		else {
			Robot.elevator.stop();
		}

//		if(controller.getRawButton(RobotMap.BUTTON_4)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))) {
//			double desiredHeight = RobotUtils.getEncPositionFromIN(77);
//			Robot.oi.elevator.set(desiredHeight-Robot.oi.elevator.getSelectedSensorPosition(0));	
//
//		}
//		else if(controller.getRawButton(RobotMap.BUTTON_4)&&(controller.getRawButton(RobotMap.BUTTON_1))){
//			double desiredHeight =(Robot.oi.elevator.getSelectedSensorPosition(0)*-1);
//			Robot.oi.elevator.set(desiredHeight);	
//		}
//		else if(controller.getRawButton(RobotMap.BUTTON_6)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))) {
//			double desiredHeight = RobotUtils.getEncPositionFromIN(19);
//			Robot.oi.elevator.set(desiredHeight-Robot.oi.elevator.getSelectedSensorPosition(0));	
//
//		}
	}
	
	protected void interrupted() {
		Robot.elevator.stop();
	}
	
	@Override
	protected boolean isFinished() {
		return false;
	}
}

