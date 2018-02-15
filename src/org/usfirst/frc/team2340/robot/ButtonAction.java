package org.usfirst.frc.team2340.robot;

public class ButtonAction {
	public ButtonAction(int _button, Action _action) {
		buttonId = _button;
		pressed = actionToBoolean(_action);
	}

	public enum Action {
		NotPressed, Pressed
	}

	private boolean actionToBoolean(Action _action) {
		switch (_action) {
			case Pressed:
				return true;
			case NotPressed:
			default:
				return false;
		}
	}

	public int buttonId;
	public boolean pressed;
}
