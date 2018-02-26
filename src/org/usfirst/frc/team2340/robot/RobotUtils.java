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
	
	public enum AutonomousState {
		DriveForward,
		Rotate
	}
	
	private static double wheelDiameter = 1;
	private static double lengthOfRobot = 0;
	private static double widthOfRobot = 0;
	private static double heightOfRobotArms = 0;
	
	public static void lengthOfRobot(double _lengthOfRobot) {
		lengthOfRobot = _lengthOfRobot;
	}
	public static void widthOfRobot(double _widthOfRobot) {
		widthOfRobot = _widthOfRobot;
	}
	public static void heightOfRobotArms(double _heightOfRobotArms) {
		heightOfRobotArms = _heightOfRobotArms;
	}
	public static double getEncPositionFromIN(double distanceInInches) {
		return (distanceInInches/(wheelDiameter * Math.PI))*1024*4;
	}
	public static double distanceMinusRobot(double distance){
		return distance-lengthOfRobot ;
	}
	public static double getLengthOfRobot() {
		return lengthOfRobot;
	}
	public static double getWidthOfRobot() {
		return widthOfRobot;
	}
	public static double getHeightOfRobotArms() {
		return heightOfRobotArms;
	}
	public static void setWheelDiameter(double _wheelDiameter) {
		wheelDiameter = _wheelDiameter;
	}
}
