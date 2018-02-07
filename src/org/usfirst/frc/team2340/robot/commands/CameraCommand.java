package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class CameraCommand extends Command {
	UsbCamera curCam, camera0, camera1;
	Joystick controller;
	int toggleButton;
	boolean buttonPressed = false;
	VideoSink server;
	
	public CameraCommand(int _button){
		controller = Robot.oi.driveController;
		toggleButton = _button;
		int intcam0 = 0;
		int intcam1 = 1;
		
		camera0 = new UsbCamera("USB Camera " + intcam0, intcam0);    
	    CameraServer.getInstance().addCamera(camera0);
	    server = CameraServer.getInstance().addServer("serve_" + camera0.getName());
	    
	    camera1 = new UsbCamera("USB Camera " + intcam1, intcam1);    
	    CameraServer.getInstance().addCamera(camera1);

		curCam = camera0;
		SmartDashboard.putString("Current Cam", curCam.getName());
		buttonPressed = false;
		
		server.setSource(camera0);
		curCam.setResolution((int)Robot.oi.IMG_WIDTH, (int)Robot.oi.IMG_HEIGHT);
	}
	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		SmartDashboard.putString("Current Cam", curCam.getName());
		if(controller.getRawButton(toggleButton)){
			if(!buttonPressed) {
				buttonPressed = true;
				switchView();
				server.setSource(curCam);
			}
		}
		else
		{
			buttonPressed = false;
		}
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	
	}
	public UsbCamera getcamera(){
		return curCam;
	}
	
	public void switchView(){
		if(curCam == camera0){
			curCam = camera1;
		} else if(curCam == camera1){
			curCam = camera0;
		}
	}
}
