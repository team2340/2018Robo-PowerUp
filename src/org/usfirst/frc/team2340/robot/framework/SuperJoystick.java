package org.usfirst.frc.team2340.robot.framework;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.command.Command;

class Action extends Button {
	final ButtonAction[] collection;
	final Joystick joystick;

	Action(Joystick _joystick, ButtonAction[] _collection) {
		joystick = _joystick;
		collection = _collection;
	}

	@Override
	public boolean get() {
		boolean satisfied = true;
		for (ButtonAction button : collection) {
			if (joystick.getRawButton(button.buttonId) != button.pressed) {
				satisfied = false;
				break;
			}
		}
		return satisfied;
	}
}

public class SuperJoystick extends Joystick {
	public SuperJoystick(int port) {
		super(port);
	}

	public void whenPressed(Command _command, ButtonAction... _collection) {
		new Action(this, _collection).whenPressed(_command);
	}

	public void whileHeld(Command _command, ButtonAction... _collection) {
		new Action(this, _collection).whileHeld(_command);
	}

	public void whenReleased(Command _command, ButtonAction... _collection) {
		new Action(this, _collection).whenReleased(_command);
	}

	public void toggleWhenPressed(Command _command, ButtonAction... _collection) {
		new Action(this, _collection).toggleWhenPressed(_command);
	}

	public void cancelWhenPressed(Command _command, ButtonAction... _collection) {
		new Action(this, _collection).cancelWhenPressed(_command);
	}
}
