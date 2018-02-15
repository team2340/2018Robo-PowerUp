package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.Driving;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveSubsystem extends GenericSubsystem {
	DifferentialDrive robotDrive;
	public double speedP = 7.0;
	public double speedI = 0.005;
	public double speedD = 0.0;
	public double speedF = 0.0;
	public double speedMaxOutput = 350;
	public double speedPeakOutputVoltage = 1.0f;
	
	public double positionP = 1.5;
	public double positionI = .0001;
	public double positionD = 0.0;
	public double positionF = 0.0;
	public float positionPeakOutputVoltage = 5.0f/12.0f;
	
	public double vBusMaxOutput = 1.0;

	public DriveSubsystem() {
		createLeftSide();
		createRightSide();
		setForPosition();
		robotDrive = new DifferentialDrive(Robot.oi.left, Robot.oi.right);
		robotDrive.setSafetyEnabled(false);
	}

	private void createLeftSide() {
		try {
			Robot.oi.left = new WPI_TalonSRX(RobotMap.LEFT_TAL_ID);
			Robot.oi.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
			Robot.oi.left.configNominalOutputForward(0, 0);
			Robot.oi.left.configNominalOutputReverse(0, 0);
			Robot.oi.left.selectProfileSlot(0, 0);
		}
		catch (Exception ex) {
			System.out.println("createLeftSide FAILED");
		}
	}

	private void createRightSide() {
		try {
			Robot.oi.right = new WPI_TalonSRX(RobotMap.RIGHT_TAL_ID);

			Robot.oi.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
			Robot.oi.right.configNominalOutputForward(0, 0);
			Robot.oi.right.configNominalOutputReverse(0, 0);
			Robot.oi.right.selectProfileSlot(0, 0);
		}
		catch (Exception ex) {
			System.out.println("createRightSide FAILED");
		}
	}

	private void setForSpeed() {
		Robot.oi.left.set(ControlMode.Current, 0);
		Robot.oi.right.set(ControlMode.Current, 0);
		Robot.oi.right.selectProfileSlot(0, 0);
		Robot.oi.right.config_kF(0, speedF, 0);
		Robot.oi.right.config_kP(0, speedP, 0);
		Robot.oi.right.config_kI(0, speedI, 0);
		Robot.oi.right.config_kD(0, speedD, 0);
		Robot.oi.left.selectProfileSlot(0, 0);
		Robot.oi.left.config_kF(0, speedF, 0);
		Robot.oi.left.config_kP(0, speedP, 0);
		Robot.oi.left.config_kI(0, speedI, 0);
		Robot.oi.left.config_kD(0, speedD, 0);
		robotDrive.setMaxOutput(speedMaxOutput);
		Robot.oi.left.configPeakOutputForward(speedPeakOutputVoltage, 0);
		Robot.oi.left.configPeakOutputReverse(-speedPeakOutputVoltage, 0);
		Robot.oi.right.configPeakOutputForward(speedPeakOutputVoltage, 0);
		Robot.oi.right.configPeakOutputReverse(-speedPeakOutputVoltage, 0);
	}

	public void setPeakOutputVoltage(float voltage) {
		Robot.oi.left.configPeakOutputForward(voltage, 0);
		Robot.oi.left.configPeakOutputReverse(-voltage, 0);
		Robot.oi.right.configPeakOutputForward(voltage, 0);
		Robot.oi.right.configPeakOutputReverse(-voltage, 0);
	}

	private void setForPosition() {
		Robot.oi.left.set(ControlMode.Position, 0);
		Robot.oi.right.set(ControlMode.Position, 0);
		Robot.oi.right.selectProfileSlot(0, 0);
		Robot.oi.right.config_kF(0, positionF, 0);
		Robot.oi.right.config_kP(0, positionP, 0);
		Robot.oi.right.config_kI(0, positionI, 0);
		Robot.oi.right.config_kD(0, positionD, 0);
		Robot.oi.left.selectProfileSlot(0, 0);
		Robot.oi.left.config_kF(0, positionF, 0);
		Robot.oi.left.config_kP(0, positionP, 0);
		Robot.oi.left.config_kI(0, positionI, 0);
		Robot.oi.left.config_kD(0, positionD, 0);
		Robot.oi.left.configPeakOutputForward(positionPeakOutputVoltage, 0);
		Robot.oi.left.configPeakOutputReverse(-positionPeakOutputVoltage, 0);
		Robot.oi.right.configPeakOutputForward(positionPeakOutputVoltage, 0);
		Robot.oi.right.configPeakOutputReverse(-positionPeakOutputVoltage, 0);
		Robot.oi.right.setSelectedSensorPosition(0, 0, 0);
		Robot.oi.left.setSelectedSensorPosition(0, 0, 0);

	}

	private void setForVBus() {
		Robot.oi.right.set(ControlMode.PercentOutput, 0);
		robotDrive.setMaxOutput(vBusMaxOutput);
		setArcadeSpeed(0, 0);
	}

	public void setBrakeMode(boolean _brake) {
		if (_brake) {
			Robot.oi.right.setNeutralMode(NeutralMode.Brake);
			Robot.oi.left.setNeutralMode(NeutralMode.Brake);
		}
		else {
			Robot.oi.right.setNeutralMode(NeutralMode.Coast);
			Robot.oi.left.setNeutralMode(NeutralMode.Coast);
		}
	}
	
	public void move(double _val, ControlMode _mode) {
		move(_val, _val, _mode);
	}
	
	public void move(double _lVal, double _rVal, ControlMode _mode) {
		if (_mode != Robot.oi.left.getControlMode() 
			|| _mode != Robot.oi.right.getControlMode()) {
			switch (_mode) {
				case Current: {
					setForSpeed();
					break;
				}
				case Position: {
					setForPosition();
					break;
				}
				case PercentOutput: {
					setForVBus();
					break;
				}
				default:
					break;
			}
		}
		Robot.oi.left.set(_mode, _lVal);
		Robot.oi.right.set(_mode, -_rVal);
	}

	public void setArcadeSpeed(double x, double y) {
		robotDrive.arcadeDrive(y, x);
	}

	@Override
	public void stop() {
		move(0, ControlMode.PercentOutput);
	}

	public int getLeftEncoderValue() {
		return Robot.oi.left.getSelectedSensorPosition(0);
	}
	
	public int getRightEncoderValue() {
		return -Robot.oi.right.getSelectedSensorPosition(0); //XXX: Inverted due to inverted motor
	}
	
	public void resetEncoders() {
		setLeftEncoderValue(0);
		setRightEncoderValue(0);
	}
	
	public void setLeftEncoderValue(int _val) {
		Robot.oi.left.setSelectedSensorPosition(_val, 0, 0);
	}
	
	public void setRightEncoderValue(int _val) {
		Robot.oi.right.setSelectedSensorPosition(_val, 0, 0);
	}
	
	@Override
	public int getEncoderValue(int _id) {
		if (_id == Robot.oi.left.getBaseID())
			return getLeftEncoderValue();
		else if (_id == Robot.oi.right.getBaseID())
			return getRightEncoderValue();
		else
			return 0;
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new Driving());
	}
}
