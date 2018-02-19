package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.ElevatorCommand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem {
	static private ElevatorSubsystem subsystem;
	private ElevatorSubsystem() {
		createElevator();
	}

	public static ElevatorSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new ElevatorSubsystem();
		}
		return subsystem;
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
	public void move(double amt) {
		Robot.oi.elevator.set(amt);
	}
	public void movePosition(double amt) {
		Robot.oi.elevator.set(ControlMode.Position,amt);
	}
	public void setEncoder(int amt) {
		Robot.oi.elevator.setSelectedSensorPosition(amt,0,0);
	}
	public int  getEncoder() {
	return Robot.oi.elevator.getSelectedSensorPosition(0);
	}
	

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ElevatorCommand());
	}
}
