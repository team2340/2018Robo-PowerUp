package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class ArmCommand extends Command {
	private Joystick controller;
	double currentTime;
	private boolean button7Pressed, on;

	public ArmCommand() {
		requires(Robot.arm);
	}

	@Override
	protected void initialize() {
		controller = Robot.oi.acquisitionController;
		button7Pressed = false;
	}

	@Override
	protected void execute() {
		// double z = (1 + controller.getZ()) / 2;

		if (controller.getRawButton(RobotMap.BUTTON_3) && (!controller.getRawButton(RobotMap.BUTTON_1))) {

			Robot.arm.move(1);

		}
		else if (controller.getRawButton(RobotMap.BUTTON_3) && (controller.getRawButton(RobotMap.BUTTON_1))) {
			{
				Robot.arm.move(-1);
			}

		}
		else {
			Robot.arm.move(0);
		}

		if (controller.getRawButton(RobotMap.BUTTON_7)) {
			if (!button7Pressed) {
				toggleAq();
				button7Pressed = true;
			}
		}
		else {
			button7Pressed = false;
		}

	}

	private void toggleAq() {
		if (!on) {
			Robot.arm.open();
			on = true;
		}
		else {
			Robot.arm.close();
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
