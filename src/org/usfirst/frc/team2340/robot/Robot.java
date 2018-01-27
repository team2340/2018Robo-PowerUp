
package org.usfirst.frc.team2340.robot;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2340.robot.RobotUtils.AutoMode;
import org.usfirst.frc.team2340.robot.commands.AutoDriveForward;
import org.usfirst.frc.team2340.robot.commands.AutoOneSwitchRight;
import org.usfirst.frc.team2340.robot.commands.CameraCommand;
import org.usfirst.frc.team2340.robot.subsystems.*;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

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

	Command autonomousCommand = null;
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

//		oi.gyro = new ADXRS450_Gyro();
		
        autoMode.addDefault("DriveForward", AutoMode.DriveForward);
		autoMode.addObject("Disabled", AutoMode.DISABLED);
		autoMode.addObject("OneSwitch", AutoMode.OneSwitch);
		autoMode.addObject("OneScale", AutoMode.OneScale);
		autoMode.addObject("TwoSwitch", AutoMode.TwoSwitch);
		autoMode.addObject("ThreeSwitch", AutoMode.ThreeSwitch);
		autoMode.addObject("ThreeScale", AutoMode.ThreeScale);
		SmartDashboard.putData("Autonomous Modes", autoMode);

		// when we didn't have the camera open in this resolution 640x480, the grip pipeline was 
		// finding a different number of targets than the computer would find attached to the 
		// same camera in the grip application.

		cameraCommand = new CameraCommand(); 
		UsbCamera camera = cameraCommand.getcamera();
//		UsbCamera camera = CameraServer.getInstance().startAutomaticCapture();
		System.out.println("Camera name: " + camera.getName());
		camera.setResolution((int)Robot.oi.IMG_WIDTH, (int)Robot.oi.IMG_HEIGHT);

//		visionThread = new VisionThread(camera,new GripPipelineRectangle(), grip -> {
//			if(!grip.filterContoursOutput().isEmpty()){
//				ArrayList<MatOfPoint> contours = grip.filterContoursOutput();
//				ArrayList<MatOfPoint> targets = new ArrayList<MatOfPoint>();
//				for(MatOfPoint point : contours){
//					double expectedRation = 2.54;
//					double tolerance = 2;
//					Rect r = Imgproc.boundingRect(point);
//					double ration = r.height/r.width;
//
//					if(ration < expectedRation + tolerance && ration > expectedRation - tolerance){
//						targets.add(point);
//					}
//				}
//				if ( lastTargets != targets.size()) { 
//					lastTargets = targets.size();
////					if(!isOperatorControl()){
//						System.out.println(System.currentTimeMillis() + " num targets: " + lastTargets);
////					}
//				}
//				if(targets.size() == 2){
//					Rect r = Imgproc.boundingRect(grip.filterContoursOutput().get(0));
//					Rect q = Imgproc.boundingRect(grip.filterContoursOutput().get(1));
//					synchronized(imgLock){
//						Robot.drive.centerX = (r.x + (r.width/2) + q.x + (q.width/2))/2.0;
//						//put code here
//						double leftmost=0.0;
//						double rightmost=0.0;
//						double pxBetweenTargets=0.0;
//						double angleBetweenTargets=0.0;
//						double halfAngleBetweenTargets=0.0;
//						double lengthOfOpposite=5.125;
//						double distanceFromTarget =0.0;
//						if ( r.x < q.x) {
//							leftmost = r.x;
//							rightmost = q.x+ q.width;
//						} else {
//							leftmost = q.x;
//							rightmost = r.x+ r.width;
//						}
//						pxBetweenTargets = rightmost - leftmost;
//						angleBetweenTargets = (Robot.oi.CAM_VIEWING_ANGLE * pxBetweenTargets)/Robot.oi.IMG_WIDTH;
//						halfAngleBetweenTargets = angleBetweenTargets/2.0;
//						double radians = Math.toRadians(halfAngleBetweenTargets);
//						distanceFromTarget = lengthOfOpposite/ (Math.tan(radians));
//						if ( distanceFromTarget >= 0 ) {
//							Robot.drive.finalDistance = distanceFromTarget;
//						}
////						if(!isOperatorControl()){
//							System.out.println(System.currentTimeMillis() + " r.x: "+r.x+", r.width: "+r.width
//									+", q.x: "+q.x+", q.width: "+q.width
//									+", centerX: "+Robot.drive.centerX + ", distance: "+distanceFromTarget);
////						}
//						//System.out.println("leftmost: " + leftmost + " rightmost: " + rightmost);
//					}
//				} else {
//					Robot.drive.centerX = -1;
//				}
//				SmartDashboard.putNumber("CenterX", Robot.drive.centerX);
//			} else {
//				Robot.drive.centerX = -1;
//			}
//		});
//		visionThread.start();
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
		AutoMode am = (AutoMode) autoMode.getSelected();

		if(am == AutoMode.DriveForward){
			autonomousCommand = new AutoDriveForward();
		}
		else if (am == AutoMode.OneSwitch) {
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(0) == 'R')
			{
				autonomousCommand = new AutoOneSwitchRight();
			}
			else {
				autonomousCommand = new AutoOneSwitchLeft();
			}
		}
		else if(am == AutoMode.OneScale){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(1) == 'R')
			{
				autonomousCommand = new AutoOneSwitchRight();
			}
			else {
				autonomousCommand = new AutoOneSwitchLeft();
			}
		}
		else if(am == AutoMode.TwoSwitch){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(0) == 'R'){
				autonomousCommand = new AutoTwoSwitchRight();
			}
			else {
				autonomousCommand = new AutoDriveForward();
			}
		}
		else if(am == AutoMode.ThreeSwitch){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(0) == 'R')
			{
				autonomousCommand = new AutoThreeSwitchRight();
			}
			else {
				autonomousCommand = new AutoThreeSwitchLeft();
			}
		}
		else if(am == AutoMode.ThreeScale){
			String gameData;
			gameData = DriverStation.getInstance().getGameSpecificMessage();
			if(gameData.charAt(1) == 'R')
			{
				autonomousCommand = new AutoThreeScaleRight();
			}
			else {
				autonomousCommand = new AutoThreeScaleLeft();
			}
		}
		else if (am == AutoMode.DISABLED) {} //Do Nothing if disabled

		System.out.println(am);
		if (autonomousCommand != null) autonomousCommand.start();
	}

	public void autonomousPeriodic() {
		SmartDashboard.putNumber("left position", Robot.oi.left.getSelectedSensorPosition(0));
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSelectedSensorPosition(0)); 
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
