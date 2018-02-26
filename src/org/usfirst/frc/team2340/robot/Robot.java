
package org.usfirst.frc.team2340.robot;

import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoArm;
import org.usfirst.frc.team2340.robot.commands.AutoDriveForward;
import org.usfirst.frc.team2340.robot.commands.Elevator;
import org.usfirst.frc.team2340.robot.commands.HomingCommand;
import org.usfirst.frc.team2340.robot.commands.CameraCommand;
import org.usfirst.frc.team2340.robot.commands.Rotation;
import org.usfirst.frc.team2340.robot.commands.Timer;
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
//TODO: add 13 to arm height for the box height
public class Robot extends IterativeRobot {
	public static final OI oi = new OI();
	public static final DriveSubsystem drive = DriveSubsystem.getInstance();
	public static final ClimbingSubsystem climbing = ClimbingSubsystem.getInstance(); 
	public static final ArmSubsystem arm = ArmSubsystem.getInstance();
	public static final ElevatorSubsystem elevator = ElevatorSubsystem.getInstance();
	public static int lastTargets = 0;
	public static final DebugLogger myLogger = new DebugLogger();

	CommandGroup autonomousCommand = null;
	CameraCommand cameraCommand = null;
	SendableChooser<AutoMode> autoMode = new SendableChooser<AutoMode>();

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	public void robotInit() {
		RobotUtils.lengthOfRobot(33);
		RobotUtils.widthOfRobot(28);
		RobotUtils.heightOfRobotArms(16);
		RobotUtils.setWheelDiameter(4);

		myLogger.open("logs/", "DebugLogger", ".csv");

		oi.gyro = new ADXRS450_Gyro();
		cameraCommand = new CameraCommand();

		autoMode.addDefault("DriveForward", AutoMode.DriveForward);
		autoMode.addObject("Disabled", AutoMode.DISABLED);
		autoMode.addObject("OneSwitch", AutoMode.OneSwitch);
		autoMode.addObject("OneScale", AutoMode.OneScale);
		autoMode.addObject("TwoSwitch", AutoMode.TwoSwitch);
		autoMode.addObject("ThreeSwitch", AutoMode.ThreeSwitch);
		autoMode.addObject("ThreeScale", AutoMode.ThreeScale);
		SmartDashboard.putData("Autonomous Modes", autoMode);

		SmartDashboard.putNumber("Delay", 0);
	}

	public void disabledInit() {
		if (cameraCommand != null) {
			cameraCommand.cancel();
		}
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		oi.gyro.reset();
		Robot.drive.setForPosition();
		AutoMode am = (AutoMode) autoMode.getSelected();
		autonomousCommand = new CommandGroup();
		autonomousCommand.addSequential(new Timer());
		if	 (am == AutoMode.DISABLED) {
			Robot.myLogger.log("Disabled", "", "");
		}
		else {
		if (DriverStation.getInstance().getGameSpecificMessage().isEmpty()) {
			Robot.myLogger.log("No Game Data", "", "");
			autonomousCommand.addSequential(new AutoDriveForward(120/*-RobotUtils.getLengthOfRobot()/2*/));
		}
		else {
			if (am == AutoMode.DriveForward) {
				autonomousCommand.addSequential(new AutoDriveForward(120/*-RobotUtils.getLengthOfRobot()/2*/));
			}
			else if (am == AutoMode.OneSwitch) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(0) == 'R') {
					Robot.myLogger.log("OneSwitch", "R", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(168));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addParallel(new Elevator(19-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(55.56 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("OneSwitch", "L", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand
							.addSequential(new AutoDriveForward(235.235));/*228.735 + (.5 * RobotUtils.getLengthOfRobot())));*/
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addParallel(new Elevator(19-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(
							67.235 + (.5 * RobotUtils.getLengthOfRobot()) /*- (.5 * RobotUtils.getWidthOfRobot())*/));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(
							55.56 -(.5 * RobotUtils.getLengthOfRobot()) + (.5 * RobotUtils.getWidthOfRobot())));
//					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.OneScale) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(1) == 'R') {
					Robot.myLogger.log("OneScale", "R", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(324));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addParallel(new Elevator(51-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("OneScale", "L", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addParallel(new Elevator(51-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(88.735 + (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.TwoSwitch) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(0) == 'R') {
					Robot.myLogger.log("TwoSwitch", "R", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addParallel(new Elevator(19-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(140-.5*RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("TwoSwitch", "L", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(67));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addParallel(new Elevator(19-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(87.5 + (.5 * RobotUtils.getWidthOfRobot())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(104 - (.5 * RobotUtils.getLengthOfRobot())));
//					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.ThreeSwitch) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(0) == 'R') {
					Robot.myLogger.log("ThreeSwitch", "R", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addParallel(new Elevator(19-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(67.235 + (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(55.56 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("ThreeSwitch", "L", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(168));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addParallel(new Elevator(19-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(55.56 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.ThreeScale) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(1) == 'R') {
					Robot.myLogger.log("ThreeScale", "R", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addParallel(new Elevator(51-(/*.5**/RobotUtils.getHeightOfRobotArms())));;/*half Height of Arms to get the center, then +12 where the evelator will over lap becuse this time there shiud be two over laps */
					autonomousCommand.addSequential(new AutoDriveForward(88.735 + (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());

				}
				else {
					Robot.myLogger.log("ThreeScale", "L", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new AutoDriveForward(324));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addParallel(new Elevator(51-(/*.5**/RobotUtils.getHeightOfRobotArms())));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
//					autonomousCommand.addSequential(new AutoArm());
					}
				}
			}
		}
		System.out.println(am);
		if (autonomousCommand != null)
			autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ", Robot.oi.right.getSelectedSensorPosition(0));
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		oi.gyro.reset();
		SmartDashboard.putNumber("Gyro angle", oi.gyro.getAngle());
		Robot.drive.setForVBus();
		cameraCommand.start();
	}

	public void teleopPeriodic() {
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ", Robot.oi.right.getSelectedSensorPosition(0));
		SmartDashboard.putBoolean("Elevator Down ", Robot.oi.elevator.getSensorCollection().isRevLimitSwitchClosed());
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
	}
}
