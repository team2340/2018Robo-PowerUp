package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;
//import org.usfirst.frc.team2340.robot.RobotMap;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ArcadeDriveCommand extends Command {

	private Joystick controller;

	public ArcadeDriveCommand() {
		requires(Robot.drive);
		controller = Robot.oi.driveController;
	}

	@Override
	protected void initialize() {
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
		SmartDashboard.putNumber("right position ", Robot.oi.right.getSensorCollection().getQuadraturePosition());

		double x, y, z;

		z = (3 - controller.getZ()) / 2;
		y = -controller.getY() / z;
		x = controller.getX() / z;

		Robot.drive.setArcadeSpeed(x, y);
		// System.out.println("ArcadeSpeed x:"+x+" y:"+y); this was already comented out
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
