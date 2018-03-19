
package org.usfirst.frc.team2340.robot;

import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoArm;
import org.usfirst.frc.team2340.robot.commands.AutoDriveForward;
import org.usfirst.frc.team2340.robot.commands.Elevator;
import org.usfirst.frc.team2340.robot.commands.ElevatorTimed;
import org.usfirst.frc.team2340.robot.commands.HomingCommand;
import org.usfirst.frc.team2340.robot.commands.PneumaticArms;
import org.usfirst.frc.team2340.robot.commands.CameraCommand;
import org.usfirst.frc.team2340.robot.commands.Rotation;
import org.usfirst.frc.team2340.robot.commands.StolenEvevatorTimerCode;
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
	public static DriveSubsystem drive = null;
	public static ClimbingSubsystem climbing = null; 
	public static ArmSubsystem arm = null;
	public static ElevatorSubsystem elevator = null;
	public static final DebugLogger myLogger = new DebugLogger();
	public static int lip = 1;

	CommandGroup autonomousCommand = null;
	CameraCommand cameraCommand = null;
	SendableChooser<AutoMode> autoMode = new SendableChooser<AutoMode>();

	/**
	 * This function is run when the robot is first started up and should be used
	 * for any initialization code.
	 */
	public void robotInit() {
		new ConfigParser("config.txt");
		Integer length = ConfigParser.get(ParserDefs.RobotLength, Integer.class);
		length = (length != null)? length : 33;
		Robot.myLogger.log("Robot","Length",length); 
		
		Integer width = ConfigParser.get(ParserDefs.RobotWidth, Integer.class);
		width = (width != null)? width : 28;
		Robot.myLogger.log("Robot","Width",width); 
		
		Integer wheelDiameter = ConfigParser.get(ParserDefs.WheelDiameter, Integer.class);
		wheelDiameter = (wheelDiameter != null)? wheelDiameter : 4;
		Robot.myLogger.log("Robot","WheelDiameter",wheelDiameter); 
		
		Integer bumpers = ConfigParser.get(ParserDefs.Bumpers, Integer.class);
		bumpers = (bumpers != null)? bumpers : 6;
		Robot.myLogger.log("Robot","Bumpers",bumpers); 
		length += bumpers;
		width += bumpers;
		
		RobotUtils.lengthOfRobot(length);
		RobotUtils.widthOfRobot(width);
		RobotUtils.heightOfRobotArms(11);
		RobotUtils.heightOfBox(13);
		RobotUtils.setWheelDiameter(wheelDiameter);

		drive = DriveSubsystem.getInstance();         
		climbing = ClimbingSubsystem.getInstance();
		arm = ArmSubsystem.getInstance();               
		elevator = ElevatorSubsystem.getInstance();

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
		autoMode.addObject("Test", AutoMode.Test);
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
			Robot.myLogger.log("GameData", "Auto", DriverStation.getInstance().getGameSpecificMessage());
			if (am == AutoMode.DriveForward) {
				autonomousCommand.addSequential(new AutoDriveForward(120/*-RobotUtils.getLengthOfRobot()/2*/));
			}
			else if (am == AutoMode.Test) {
				Robot.myLogger.log("Test", "R", "");
				Robot.elevator.setEncoder(0);
//				autonomousCommand.addSequential(new HomingCommand());
//				autonomousCommand.addSequential(new PneumaticArms());
//				autonomousCommand.addSequential (new StolenEvevatorTimerCode(2));
//				autonomousCommand.addSequential(new AutoDriveForward(235.235));
				autonomousCommand.addSequential(new Elevator(65));
//				autonomousCommand.addParallel(new ElevatorTimed(5));
//				autonomousCommand.addSequential (new StolenEvevatorTimerCode(3));
			}
			else if (am == AutoMode.OneSwitch) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(0) == 'R') {
					Robot.myLogger.log("OneSwitch", "R", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(168));
					autonomousCommand.addParallel(new ElevatorTimed(5));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new Rotation(-90));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new AutoDriveForward(55.56 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("OneSwitch", "L", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));/*228.735 + (.5 * RobotUtils.getLengthOfRobot())));*/
					autonomousCommand.addParallel(new ElevatorTimed(5));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(-90));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new AutoDriveForward(67.235 + (.5 * RobotUtils.getLengthOfRobot()) /*- (.5 * RobotUtils.getWidthOfRobot())*/));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(
							55.56 -(.5 * RobotUtils.getLengthOfRobot()) + (.5 * RobotUtils.getWidthOfRobot())));
					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.OneScale) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(1) == 'R') {
					Robot.myLogger.log("OneScale", "R", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(324));
					autonomousCommand.addParallel(new ElevatorTimed(6));
//					autonomousCommand.addParallel(new Elevator(((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip)))*.5));					
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addParallel(new ElevatorTimed(8));
//					autonomousCommand.addParallel(new Elevator((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip))));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("OneScale", "L", "");
					autonomousCommand.addParallel(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));
					autonomousCommand.addParallel(new ElevatorTimed(6));
//					autonomousCommand.addParallel(new Elevator(((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip)))*.5));										autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addParallel(new ElevatorTimed(8));
//					autonomousCommand.addParallel(new Elevator((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip))));
					autonomousCommand.addSequential(new AutoDriveForward(88.735 + (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.TwoSwitch) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(0) == 'R') {
					Robot.myLogger.log("TwoSwitch", "R", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addParallel(new ElevatorTimed(5));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new AutoDriveForward(140-.5*RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("TwoSwitch", "L", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(67));
					autonomousCommand.addParallel(new ElevatorTimed(5));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(87.5 + (.5 * RobotUtils.getWidthOfRobot())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(104 - (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.ThreeSwitch) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(0) == 'R') {
					Robot.myLogger.log("ThreeSwitch", "R", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));
					autonomousCommand.addParallel(new ElevatorTimed(5));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(90));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new AutoDriveForward(67.235 + (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(55.56 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
				}
				else {
					Robot.myLogger.log("ThreeSwitch", "L", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(168));
					autonomousCommand.addParallel(new ElevatorTimed(5));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new Rotation(90));
//					autonomousCommand.addParallel(new Elevator((19-RobotUtils.getHeightOfRobotArms())+(RobotUtils.getHeightOfBox())));
					autonomousCommand.addSequential(new AutoDriveForward(55.56 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
				}
			}
			else if (am == AutoMode.ThreeScale) {
				String gameData;
				gameData = DriverStation.getInstance().getGameSpecificMessage();
				if (gameData.charAt(1) == 'R') {
					Robot.myLogger.log("ThreeScale", "R", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addSequential(new PneumaticArms());
					autonomousCommand.addSequential(new AutoDriveForward(235.235));
					autonomousCommand.addParallel(new ElevatorTimed(6));
//					autonomousCommand.addParallel(new Elevator(((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip)))*.5));
					autonomousCommand.addSequential(new Rotation(90));
					autonomousCommand.addSequential(new AutoDriveForward(264));
					autonomousCommand.addSequential(new Rotation(-90));
//					autonomousCommand.addParallel(new Elevator((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip))));
					autonomousCommand.addSequential(new AutoDriveForward(88.735 + (.5 * RobotUtils.getLengthOfRobot())));
					autonomousCommand.addParallel(new ElevatorTimed(8));
//					autonomousCommand.addParallel(new Elevator(((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip)))));
					autonomousCommand.addSequential(new Rotation(-90));
					autonomousCommand.addSequential(new AutoDriveForward(41.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());

				}
				else {
					Robot.myLogger.log("ThreeScale", "L", "");
					autonomousCommand.addSequential(new HomingCommand());
					autonomousCommand.addParallel(new ElevatorTimed(14));
					autonomousCommand.addSequential(new PneumaticArms());
//					autonomousCommand.addSequential(new Elevator(51/*-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip)))*.5*/));
					autonomousCommand.addSequential(new AutoDriveForward(324));
//					autonomousCommand.addParallel(new Elevator((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip))));
					autonomousCommand.addSequential(new Rotation(90));
//					autonomousCommand.addParallel(new Elevator((51-RobotUtils.getHeightOfRobotArms())+((RobotUtils.getHeightOfBox()+lip))));
					autonomousCommand.addSequential(new AutoDriveForward(/*was 41*/43.88 - .5 * RobotUtils.getWidthOfRobot() + .5 * RobotUtils.getLengthOfRobot()));
					autonomousCommand.addSequential(new AutoArm());
					}
				}
			}
		}
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
//		arm.startCompressor();
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ", Robot.oi.right.getSelectedSensorPosition(0));
		SmartDashboard.putBoolean("Elevator Down ", Robot.oi.elevator.getSensorCollection().isRevLimitSwitchClosed());
		SmartDashboard.putBoolean("Elevator Up ", Robot.oi.elevator.getSensorCollection().isFwdLimitSwitchClosed());
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
//		arm.stopCompressor();
	}
}
