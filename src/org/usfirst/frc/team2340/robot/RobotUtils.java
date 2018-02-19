package org.usfirst.frc.team2340.robot;

public class RobotUtils {
	public enum AutoMode {
		DISABLED,
		DRIVE_FORWARD,
		ONE_SWITCH,
		ONE_SCALE,
		TWO_SWITCH,
		THREE_SWITCH,
		THREE_SCALE
	}

	private static double wheelDiameter = 1;
	private static double lengthOfRobot = 0;

	public static void lengthOfRobot(double _lengthOfRobot) {
		lengthOfRobot = _lengthOfRobot;
	}

	public static double getEncPositionFromIN(double distanceInInches) {
		return (distanceInInches / (wheelDiameter * Math.PI)) * 1024 * 4;
	}

	public static double distanceMinusRobot(double distance) {
		return distance - lengthOfRobot;
	}

	public static double getLengthOfRobot() {
		return lengthOfRobot;
	}

	public static void setWheelDiameter(double _wheelDiameter) {
		wheelDiameter = _wheelDiameter;
	}
}
