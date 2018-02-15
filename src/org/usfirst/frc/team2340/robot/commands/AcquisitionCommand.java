package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class AcquisitionCommand extends Command {
	private Joystick controller;
	public AcquisitionCommand(){
		requires(Robot.acquisition);
	}

	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
	}

	@Override
	protected void execute() {
		double z = (1 + controller.getZ()) / 2; // Makes it a range from [0,1] instead of [-1,1]

		if (controller.getRawButton(RobotMap.BUTTON_3) && (!controller.getRawButton(RobotMap.BUTTON_1))) {
			if (z != 0) {
				Robot.acquisition.move(z);
			}
		}
		else if (controller.getRawButton(RobotMap.BUTTON_3) && (controller.getRawButton(RobotMap.BUTTON_1))) {
			if (z != 0) {
				Robot.acquisition.move(-z);
			}
		}
		else {
			Robot.acquisition.move(0);
		}

		if (controller.getRawButton(RobotMap.BUTTON_7)) {
			if (!controller.getRawButtonPressed(RobotMap.BUTTON_7)) {
				Robot.acquisition.toggle();
			}
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
