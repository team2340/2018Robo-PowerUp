package org.usfirst.frc.team2340.robot.commands;


import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.RobotUtils;


import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ElevatorCommand extends Command {
	private Joystick controller;
	double currentTime;
	public ElevatorCommand(){
		requires(Robot.elevator);
	}

	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
	}

	@Override
	protected void execute() {
		double y;

		y = -controller.getY();
		if(y!= 0) {
			Robot.elevator.move(y);
		}
		else {

			if(controller.getRawButton(RobotMap.BUTTON_5)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))){
				Robot.elevator.move(1);
			}
			else if(controller.getRawButton(RobotMap.BUTTON_5)&&(controller.getRawButton(RobotMap.BUTTON_1))){
				Robot.elevator.move(-1);
			}
			else{
				Robot.elevator.move(0);
			}
		}
	
		if(controller.getRawButton(RobotMap.BUTTON_4)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))) {
			double desiredHeight = RobotUtils.getEncPositionFromIN(77);
			Robot.elevator.movePosition(desiredHeight-Robot.elevator.getEncoder());

		}
		else if(controller.getRawButton(RobotMap.BUTTON_4)&&(controller.getRawButton(RobotMap.BUTTON_1))){
			double desiredHeight =(Robot.elevator.getEncoder()*-1);
			Robot.elevator.movePosition( desiredHeight);	
		}
		else if(controller.getRawButton(RobotMap.BUTTON_6)&&(!controller.getRawButton(RobotMap.BUTTON_1 ))) {
			double desiredHeight = RobotUtils.getEncPositionFromIN(19);
			Robot.elevator.movePosition( desiredHeight-Robot.elevator.getEncoder());	

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
