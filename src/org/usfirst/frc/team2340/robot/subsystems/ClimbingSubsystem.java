package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.Climbing;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ClimbingSubsystem extends GenericSubsystem {
	public ClimbingSubsystem() {
		createClimbing();
	}

	private void createClimbing() {
		try {
			Robot.oi.climbing = new WPI_TalonSRX(RobotMap.CLIMBING_TAL_ID);
		}
		catch (Exception ex) {
			System.out.println("climbing FAILED");
		}
	}

	@Override
	public void move(double _val, ControlMode _mode) {
		Robot.oi.climbing.set(_mode, _val);
	}

	public void up(double _val) {
		move(Math.abs(_val), ControlMode.PercentOutput);
	}

	public void down(double _val) {
		move(-Math.abs(_val), ControlMode.PercentOutput);
	}

	public void stop() {
		Robot.oi.climbing.set(0);
	}

	public int getEncoder() {
		return Robot.oi.climbing.getSelectedSensorPosition(0);
	}

	@Override
	public int getEncoder(int _id) {
		return getEncoder();
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Climbing());
	}
}
