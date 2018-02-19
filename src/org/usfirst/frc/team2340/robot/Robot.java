
package org.usfirst.frc.team2340.robot;

import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoArm;
import org.usfirst.frc.team2340.robot.commands.AutoDrive;
import org.usfirst.frc.team2340.robot.commands.AutoElevator;
import org.usfirst.frc.team2340.robot.commands.Camera;
import org.usfirst.frc.team2340.robot.framework.DebugLogger;
import org.usfirst.frc.team2340.robot.commands.AutoRotation;
import org.usfirst.frc.team2340.robot.subsystems.*;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	public static final OI oi = new OI();
	public static final DriveSubsystem drive = new DriveSubsystem();
	public static final ArmSubsystem arm = new ArmSubsystem();
	public static final ElevatorSubsystem elevator = new ElevatorSubsystem();
	public static final ClimbingSubsystem climbing = new ClimbingSubsystem();
	public static int lastTargets = 0;
	public static final DebugLogger myLogger = new DebugLogger();

	CommandGroup autonomousCommand = null;
	Camera camera = null;
	SendableChooser<AutoMode> autoMode = new SendableChooser<AutoMode>();

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	public void robotInit() {
		RobotUtils.lengthOfRobot(39);
		RobotUtils.setWheelDiameter(4);

		// myLogger.open("/", "DebugLogger", ".csv");

		oi.gyro = new ADXRS450_Gyro();
		camera = new Camera();

		autoMode.addDefault("DriveForward", AutoMode.DRIVE_FORWARD);
		autoMode.addObject("Disabled", AutoMode.DISABLED);
		autoMode.addObject("OneSwitch", AutoMode.ONE_SWITCH);
		autoMode.addObject("OneScale", AutoMode.ONE_SCALE);
		autoMode.addObject("TwoSwitch", AutoMode.TWO_SWITCH);
		autoMode.addObject("ThreeSwitch", AutoMode.THREE_SWITCH);
		autoMode.addObject("ThreeScale", AutoMode.THREE_SCALE);
		SmartDashboard.putData("Autonomous Modes", autoMode);

		camera = new Camera();
	}

	public void disabledInit() {
		if (camera != null) {
			camera.cancel();
		}
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		oi.gyro.reset();
		AutoMode am = (AutoMode) autoMode.getSelected();
		autonomousCommand = new CommandGroup();
		String gameData = DriverStation.getInstance().getGameSpecificMessage();
		myLogger.log("Autonomous", "GameData", gameData);
		myLogger.log("Autonomous", "Mode", am.toString());
		if (am == AutoMode.DISABLED) {
			// Do Nothing if disabled
		}
		else if (DriverStation.getInstance().getGameSpecificMessage().isEmpty()) {
			autonomousCommand.addSequential(new AutoDrive(135));
		}
		else if (am == AutoMode.DRIVE_FORWARD) {
			// Drive forward is relative to the back of the robot
			autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(135)));
		}
		else if (am == AutoMode.ONE_SWITCH) {
			if (gameData.charAt(0) == 'R') {
				DriveCorrection corr = new DriveCorrection();
				// This should bring the center of the robot to the center of the switch
				// distance from wall to bottom of switch + switch width / 2 - Length of robot
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(168), corr, -1));
				// Rotates to the left 90
				autonomousCommand.addSequential(new AutoRotation(-90));
				// TODO: 19 is a made up value...
				autonomousCommand.addParallel(new AutoElevator(19));
				// distance from wall to front of switch (d) - Length of robot (l) - back of
				// robot to wall (w) + correction (c)
				// so, desired distance = d - l - w + c; or, d - l - (w - c)
				// distance from wall is known ahead of time (how wide is the diagonal piece).
				autonomousCommand.addSequential(new AutoDrive(RobotUtils
						.distanceMinusRobot(55.56 + (.5 * RobotUtils.getLengthOfRobot()) + corr.getCorrection().x)));
				autonomousCommand.addSequential(new AutoArm());
			}
			else {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(264 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(88.735 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(55.56 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
		}
		else if (am == AutoMode.ONE_SCALE) {
			if (gameData.charAt(1) == 'R') {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(340.5)));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(42.06 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
			else {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(264 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(106.265 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(42.06 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
		}
		else if (am == AutoMode.TWO_SWITCH) {
			if (gameData.charAt(0) == 'R') {
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(140)));
				autonomousCommand.addSequential(new AutoArm());
			}
			else {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(130)));
			}
		}
		else if (am == AutoMode.THREE_SWITCH) {
			if (gameData.charAt(0) == 'R') {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(264 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDrive(88.735 + (.5 * RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addSequential(new AutoDrive(55.56 + (.5 * RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new AutoArm());
			}
			else {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(168)));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(55.56 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
		}
		else if (am == AutoMode.THREE_SCALE) {
			if (gameData.charAt(1) == 'R') {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(264 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(new AutoDrive(106.265 + (.5 * RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new AutoRotation(-90));
				autonomousCommand.addSequential(new AutoDrive(42.06 + (.5 * RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new AutoArm());

			}
			else {
				autonomousCommand.addSequential(new AutoDrive(RobotUtils.distanceMinusRobot(340.5)));
				autonomousCommand.addSequential(new AutoRotation(90));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(
						new AutoDrive(RobotUtils.distanceMinusRobot(41.88 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
		}

		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoder());
		SmartDashboard.putNumber("right position ", Robot.drive.getRightEncoder());
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		camera.start();
	}

	public void teleopPeriodic() {
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoder());
		SmartDashboard.putNumber("right position ", Robot.drive.getRightEncoder());
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
		Scheduler.getInstance().run();
	}
}
