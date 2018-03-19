package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.ElevatorCommand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ElevatorSubsystem extends Subsystem {
	static private ElevatorSubsystem subsystem;
	public double positionP = 2; //0.08525; //25% power at 3000 error
	public double positionI = 0.000001;
	//public double positionD = 10.0;

	//	public double positionI = 0.000;
	public double positionD = 0.0;
	public double positionF = 0.0;
	public float positionPeakOutputVoltage = 12.0f/12.0f;

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
			Robot.oi.elevator.selectProfileSlot(0,0);
			Robot.oi.elevator.configPeakOutputForward(1,0); 
			Robot.oi.elevator.configPeakOutputReverse(-1,0);
			//Robot.oi.elevator.setSensorPhase(true);
		} catch (Exception ex) {
			System.out.println("createElevator FAILED");
		}
	}
	public void hitTheRoof() {
		if (Robot.oi.elevator.getSensorCollection().isFwdLimitSwitchClosed()){
			move(0);
		}
		else {
			move(1);
		}
	}
	public void hitTheDeck() {
		if (!Robot.oi.elevator.getSensorCollection().isRevLimitSwitchClosed()){
			move(-1);
		}
		else {
			move(0);
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

	public void setPosition() {
		Robot.oi.elevator.set(ControlMode.Position,0);
		Robot.oi.elevator.selectProfileSlot(0,0);
		Robot.oi.elevator.config_kF(0,positionF,0);
		Robot.oi.elevator.config_kP(0,positionP,0);
		Robot.oi.elevator.config_kI(0,positionI,0); 
		Robot.oi.elevator.config_kD(0,positionD,0); 
		//Robot.oi.elevator.configPeakOutputForward(positionPeakOutputVoltage,0);
		//Robot.oi.elevator.configPeakOutputReverse(-positionPeakOutputVoltage,0);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ElevatorCommand());
	}
}
