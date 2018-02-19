
package org.usfirst.frc.team2340.robot;

import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoArm;
import org.usfirst.frc.team2340.robot.commands.AutoDriveForward;
import org.usfirst.frc.team2340.robot.commands.AutoElevator;
import org.usfirst.frc.team2340.robot.commands.Camera;
import org.usfirst.frc.team2340.robot.commands.Rotation;
import org.usfirst.frc.team2340.robot.subsystems.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
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
public class Robot extends TimedRobot {
	public static final OI oi = new OI();
	public static final DriveSubsystem drive = new DriveSubsystem();
	public static final ArmSubsystem arm = new ArmSubsystem();
	public static final ElevatorSubsystem elevator = new ElevatorSubsystem();
	public static final ClimbingSubsystem climbing = new ClimbingSubsystem();
	public static int lastTargets = 0;

	CommandGroup autonomousCommand = null;
	Camera camera = null;
	SendableChooser<AutoMode> autoMode = new SendableChooser<AutoMode>();


	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		System.out.println("robotinit");
		RobotUtils.lengthOfRobot(39);
		RobotUtils.setWheelDiameter(4);

		oi.gyro = new ADXRS450_Gyro();

		autoMode.addDefault("DriveForward", AutoMode.DriveForward);
		autoMode.addObject("Disabled", AutoMode.DISABLED);
		autoMode.addObject("OneSwitch", AutoMode.OneSwitch);
		autoMode.addObject("OneScale", AutoMode.OneScale);
		autoMode.addObject("TwoSwitch", AutoMode.TwoSwitch);
		autoMode.addObject("ThreeSwitch", AutoMode.ThreeSwitch);
		autoMode.addObject("ThreeScale", AutoMode.ThreeScale);
		SmartDashboard.putData("Autonomous Modes", autoMode);

		camera = new Camera(); 
		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		System.out.println("Camera name: " + camera.getName());
		camera.setResolution((int)Robot.oi.IMG_WIDTH, (int)Robot.oi.IMG_HEIGHT);

	}

	public void disabledInit(){
		if(camera != null){
			camera.cancel();
		}
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		AutoMode am = (AutoMode) autoMode.getSelected();
		autonomousCommand = new CommandGroup();

		if(am == AutoMode.DriveForward){
			//Drive forward is relative to the back of the robot
			autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(135)));
		}
		else if (am == AutoMode.OneSwitch) {
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(0) == 'R')
			{
				DriveCorrection corr = new DriveCorrection();
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(168), corr, -1));
				autonomousCommand.addSequential(new Rotation(90, false));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(55.56 + (.5 * RobotUtils.getLengthOfRobot()) + corr.getCorrection().x)));
				autonomousCommand.addSequential(new AutoArm());
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new Rotation(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(264 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new Rotation(90, false));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(88.735 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new Rotation(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(55.56 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
		}
		else if(am == AutoMode.OneScale){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(1) == 'R')
			{
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(340.5)));
				autonomousCommand.addSequential(new Rotation(90, false));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(42.06 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new Rotation(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(264 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new Rotation(90, true));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(106.265 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new Rotation(90, true));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(42.06 + (.5 * RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm());
			}
		}
		else if(am == AutoMode.TwoSwitch){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(0) == 'R'){
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(140)));
				autonomousCommand.addSequential(new AutoArm ( ));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(130)));
			}
		}
		else if(am == AutoMode.ThreeSwitch){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(0) == 'R')
			{
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new Rotation (90, true));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(264+(.5*RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new Rotation (90, true));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDriveForward (88.735+(.5*RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new Rotation (90, true));
				autonomousCommand.addSequential(new AutoDriveForward (55.56+(.5*RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new AutoArm ( ));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(168)));
				autonomousCommand.addSequential(new Rotation (90, true));
				autonomousCommand.addParallel(new AutoElevator(19));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(55.56+(.5*RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm ( ));
			}
		}
		else if(am == AutoMode.ThreeScale){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(1) == 'R')
			{
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(228.735)));
				autonomousCommand.addSequential(new Rotation (90, true));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(264+(.5*RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new Rotation (90, false));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(new AutoDriveForward (106.265+(.5*RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new Rotation (90, false));
				autonomousCommand.addSequential(new AutoDriveForward (42.06+(.5*RobotUtils.getLengthOfRobot())));
				autonomousCommand.addSequential(new AutoArm ( ));

			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(340.5)));
				autonomousCommand.addSequential(new Rotation (90,true));
				autonomousCommand.addParallel(new AutoElevator(51));
				autonomousCommand.addSequential(new AutoDriveForward(RobotUtils.distanceMinusRobot(41.88+(.5*RobotUtils.getLengthOfRobot()))));
				autonomousCommand.addSequential(new AutoArm ( ));
			}
		}
		else if (am == AutoMode.DISABLED) {} //Do Nothing if disabled

		System.out.println(am);
		if (autonomousCommand != null) autonomousCommand.start();
		
		camera.start();
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoderValue());
		SmartDashboard.putNumber("right position ",Robot.drive.getRightEncoderValue()); 
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		if (autonomousCommand != null) autonomousCommand.cancel();
	}

	public void teleopPeriodic() {
		SmartDashboard.putNumber("left position", Robot.drive.getLeftEncoderValue());
		SmartDashboard.putNumber("right position ",Robot.drive.getRightEncoderValue()); 
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
	}
}
