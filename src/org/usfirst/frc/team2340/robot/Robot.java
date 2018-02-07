
package org.usfirst.frc.team2340.robot;

import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoDriveForward;
import org.usfirst.frc.team2340.robot.commands.AutoRotate;
import org.usfirst.frc.team2340.robot.commands.CameraCommand;
import org.usfirst.frc.team2340.robot.commands.VisionCommand;
import org.usfirst.frc.team2340.robot.subsystems.*;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
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
	public static final DriveSubsystem drive = DriveSubsystem.getInstance();
	public static final AcquisitionSubsystem acquisition = AcquisitionSubsystem.getInstance();
	public static int lastTargets = 0;

	CommandGroup autonomousCommand = null;
	CameraCommand cameraCommand = null;
	SendableChooser<AutoMode> autoMode = new SendableChooser<AutoMode>();

//	private VisionThread visionThread;
//	private final Object imgLock = new Object();

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	public void robotInit() {
		Robot.drive.centerX = 0;
		RobotUtils.lengthOfRobot(33);
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

		cameraCommand = new CameraCommand(); 
		(new VisionCommand(cameraCommand.getcamera())).start();
	}

	public void disabledInit(){
		if(cameraCommand != null){
			cameraCommand.cancel();
		}
	}

	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	public void autonomousInit() {
		oi.gyro.reset();
		Robot.drive.setForPosition();
		autonomousCommand = new CommandGroup();
		AutoMode am = (AutoMode) autoMode.getSelected();
		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if(am == AutoMode.DriveForward){
			autonomousCommand.addSequential(new AutoDriveForward(135));
		}
		else if (am == AutoMode.OneSwitch) {
			if(gameData.charAt(0) == 'R') {
				//168 is distance to the switch
				autonomousCommand.addSequential(new AutoDriveForward(168 + (RobotUtils.getLengthOfRobot()*.5)));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
		}
		else if(am == AutoMode.OneScale){
			if(gameData.charAt(1) == 'R')
			{
				//TODO: 200 is not the right number
				autonomousCommand.addSequential(new AutoDriveForward(200 + (RobotUtils.getLengthOfRobot()*.5)));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
		}
		else if(am == AutoMode.TwoSwitch){
			if(gameData.charAt(0) == 'R'){
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(135));
			}
		}
		else if(am == AutoMode.ThreeSwitch){
			if(gameData.charAt(0) == 'R')
			{
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
		}
		else if(am == AutoMode.ThreeScale){
			if(gameData.charAt(1) == 'R')
			{
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
			else {
				autonomousCommand.addSequential(new AutoDriveForward(135));
				autonomousCommand.addSequential(new AutoRotate(90, false));
				autonomousCommand.addSequential(new AutoDriveForward(20));
			}
		}
		else if (am == AutoMode.DISABLED) {} //Do Nothing if disabled

		System.out.println(am);
		if (autonomousCommand != null) autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("Left Position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("Right Position ",Robot.oi.right.getSelectedSensorPosition(0)); 
		Scheduler.getInstance().run();
	}

	public void teleopInit() {
		if (autonomousCommand != null) autonomousCommand.cancel();
		Robot.drive.setForVBus();
		cameraCommand.start();	
	}

	public void teleopPeriodic() {
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSelectedSensorPosition(0));
		Scheduler.getInstance().run();
	}

	public void testPeriodic() {
	}
}
