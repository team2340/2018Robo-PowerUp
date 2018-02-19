package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
//import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveCommand extends Command {
 
	private Joystick controller;
//	private boolean gyroEnable;
//	private boolean prevState;
//	private boolean button2Pressed,on;
	
	public ArcadeDriveCommand(){
		requires(Robot.drive);
		controller = Robot.oi.driveController;
//		gyroEnable = false;
//		prevState = false;
	}
	
	@Override
	protected void initialize() {
//		button2Pressed=on =false;
	}
	private boolean adjustRotation()
	{
		//CENTER IS 320
		if ( Robot.drive.centerX != -1 ) {
			if(Robot.drive.centerX > 340) { //+rotate: go right
				System.out.println("Adjusting 330 " + Robot.drive.centerX);
				rotateRight(20);
			}
			else if(Robot.drive.centerX < 310){ //-rotate: go left
				System.out.println("Adjusting 310 "  + Robot.drive.centerX);
				rotateLeft(20);
			}
			else{
				System.out.println("Good Enough!");
				setSpeed(0);
				return true;
			}
		} else {
			setSpeed(0);
			System.out.println("Nothing detected");
		}

		return false;
	}
	void setSpeed(double rpm) {
		Robot.oi.left.set(+rpm);
		Robot.oi.right.set(-rpm);
	}
	void rotateRight(double rpm) {
		Robot.oi.left.set(rpm);
		Robot.oi.right.set(rpm);
	}
	void rotateLeft(double rpm) {
		Robot.oi.left.set(-rpm);
		Robot.oi.right.set(-rpm);
	}
	@Override
	protected void execute() {
		double angle = Robot.oi.gyro.getAngle();
		SmartDashboard.putNumber("Gyro angle", angle);
		SmartDashboard.putNumber("left position", Robot.oi.left.getSensorCollection().getQuadraturePosition());
		SmartDashboard.putNumber("right position ",Robot.oi.right.getSensorCollection().getQuadraturePosition());

		double x, y, z;

		z = (3-controller.getZ())/2;
		y = -controller.getY()/z;
		x = controller.getX()/z; 
//		if(controller.getRawButton(RobotMap.BUTTON_2)){
//			if(){
//				button2Pressed
//			}
//			}
//			else{
//
//			}


		Robot.drive.setArcadeSpeed(x, y);
//		System.out.println("ArcadeSpeed x:"+x+" y:"+y);
		
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
}
