package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.Elevator;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class ElevatorSubsystem extends GenericSubsystem {

	public ElevatorSubsystem() {
		createElevator();
	}
	
	private void createElevator() {
		try {
			Robot.oi.elevator = new WPI_TalonSRX(RobotMap.ELEVATOR_TAL_ID);
			Robot.oi.elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
			Robot.oi.elevator.configNominalOutputForward(0,0);
			Robot.oi.elevator.configNominalOutputReverse(0,0);
		    Robot.oi.elevator.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("createElevator FAILED");
		}
	}
	
	public int getEncoder() {
		return Robot.oi.elevator.getSelectedSensorPosition(0);
	}
	
	public int getEncoder(int _id) {
		return getEncoder();
	}
	
	public void move(double _val, ControlMode _mode) {
		Robot.oi.elevator.set(_mode, _val);
	}
	
	public void movePosition(double _val) {
		Robot.oi.elevator.set(ControlMode.Position, _val);
	}
	
	public void move(double _val) {
		Robot.oi.elevator.set(ControlMode.PercentOutput, _val);
	}
	
	public void up(double _val) {
		move(Math.abs(_val), ControlMode.PercentOutput);
	}

	public void down(double _val) {
		move(-Math.abs(_val), ControlMode.PercentOutput);
	}

	public void stop() {
		Robot.oi.elevator.set(0);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Elevator());
	}

}
