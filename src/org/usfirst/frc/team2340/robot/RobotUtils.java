package org.usfirst.frc.team2340.robot;

public class RobotUtils {
	public enum AutoMode {
		DISABLED,
		DriveForward,
		OneSwitch,
		OneScale,
		TwoSwitch,
		ThreeSwitch,
		ThreeScale
	}
	
	private static double wheelDiameter = 1;
	private static double lengthOfRobot = 0;
	
	public static void lengthOfRobot(double _lengthOfRobot) {
		lengthOfRobot = _lengthOfRobot;
	}
	
	public static double getLengthOfRobot() {
		return lengthOfRobot;
	}
	
	public static double getEncPositionFromIN(double _distanceInInches) {
		return _distanceInInches/(wheelDiameter * Math.PI);
	}

	public static double distanceFromRobotFront(double _distance){
		return _distance-lengthOfRobot;
	}

	public static void setWheelDiameter(double _wheelDiameter) {
		wheelDiameter = _wheelDiameter;
	}
}
