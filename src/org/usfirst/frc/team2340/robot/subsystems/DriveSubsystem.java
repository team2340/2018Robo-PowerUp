package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.ArcadeDriveCommand;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class DriveSubsystem extends Subsystem {

	static private DriveSubsystem subsystem;
	DifferentialDrive robotDrive;
	public double centerX;
	public double finalDistance;
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

	static public DriveSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new DriveSubsystem();
		}
		return subsystem;
	}

	protected void initDefaultCommand() {
		setDefaultCommand(new ArcadeDriveCommand());
	}

	private DriveSubsystem() {
		centerX = -1;
		finalDistance = 0.0;
		createLeftSide();
		createRightSide();
		setForPosition();
		robotDrive = new DifferentialDrive(Robot.oi.left, Robot.oi.right);
		robotDrive.setSafetyEnabled(false);
	}

	private void createLeftSide() {
		try {
			Robot.oi.left = new WPI_TalonSRX(RobotMap.LEFT_TAL_ID);
			Robot.oi.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
//			Robot.oi.left.reverseSensor(true);
//			Robot.oi.left.configEncoderCodesPerRev(360); //TODO: not using 360
			Robot.oi.left.configNominalOutputForward(0,0);
			Robot.oi.left.configNominalOutputReverse(0,0);
		    Robot.oi.left.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("createLeftSide FAILED");
		}
	}

	private void createRightSide() {
		try {
			Robot.oi.right = new WPI_TalonSRX(RobotMap.RIGHT_TAL_ID);

			Robot.oi.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
			Robot.oi.right.setInverted(true);
//			Robot.oi.right.configEncoderCodesPerRev(360); //TODO: not using 360
			Robot.oi.right.configNominalOutputForward(0,0);
			Robot.oi.right.configNominalOutputReverse(0,0);
		    Robot.oi.right.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("createRightSide FAILED");
		}
	}
	
	public void setForSpeed() {
		Robot.oi.left.set(ControlMode.Current, 0);
		Robot.oi.right.set(ControlMode.Current, 0);
		Robot.oi.right.selectProfileSlot(0, 0);
		Robot.oi.right.config_kF(0,speedF,0);
	    Robot.oi.right.config_kP(0,speedP,0);
	    Robot.oi.right.config_kI(0,speedI,0); 
	    Robot.oi.right.config_kD(0,speedD,0);
	    Robot.oi.left.selectProfileSlot(0,0);
	    Robot.oi.left.config_kF(0,speedF,0);
	    Robot.oi.left.config_kP(0,speedP,0);  
	    Robot.oi.left.config_kI(0,speedI,0);  
	    Robot.oi.left.config_kD(0,speedD,0);
	    robotDrive.setMaxOutput (speedMaxOutput);
	    Robot.oi.left.configPeakOutputForward(speedPeakOutputVoltage,0); 
	    Robot.oi.left.configPeakOutputReverse(-speedPeakOutputVoltage,0);
	    Robot.oi.right.configPeakOutputForward(speedPeakOutputVoltage,0);
	    Robot.oi.right.configPeakOutputReverse(-speedPeakOutputVoltage,0);	
	}
	
	public void setPeakOutputVoltage(float voltage) {
		Robot.oi.left.configPeakOutputForward(voltage,0);
		Robot.oi.left.configPeakOutputReverse(-voltage,0);
	    Robot.oi.right.configPeakOutputForward(voltage,0);
	    Robot.oi.right.configPeakOutputReverse(-voltage,0);	
	}
	
	public void setForPosition() {
		Robot.oi.left.set(ControlMode.Position, 0);
		Robot.oi.right.set(ControlMode.Position,0);
		Robot.oi.right.selectProfileSlot(0,0);
		Robot.oi.right.config_kF(0,positionF,0);
	    Robot.oi.right.config_kP(0,positionP,0);
	    Robot.oi.right.config_kI(0,positionI,0); 
	    Robot.oi.right.config_kD(0,positionD,0);
	    Robot.oi.left.selectProfileSlot(0,0);
	    Robot.oi.left.config_kF(0,positionF,0);
	    Robot.oi.left.config_kP(0,positionP,0);  
	    Robot.oi.left.config_kI(0,positionI,0);  
	    Robot.oi.left.config_kD(0,positionD,0);   
	    Robot.oi.left.configPeakOutputForward(positionPeakOutputVoltage,0);
	    Robot.oi.left.configPeakOutputReverse(-positionPeakOutputVoltage,0);
	    Robot.oi.right.configPeakOutputForward(positionPeakOutputVoltage,0);
	    Robot.oi.right.configPeakOutputReverse(-positionPeakOutputVoltage,0);
	    Robot.oi.right.setSelectedSensorPosition(0,0,0);
	    Robot.oi.left.setSelectedSensorPosition(0,0,0);
	    
	}
	
	public void setForVBus() {
		Robot.oi.left.set(ControlMode.PercentOutput,0);
        Robot.oi.right.set(ControlMode.PercentOutput,0);
        robotDrive.setMaxOutput (vBusMaxOutput);
		setArcadeSpeed(0,0);
	}
	
	public void setBrakeMode(boolean brake) {
		Robot.oi.right.setNeutralMode(NeutralMode.Brake);
		Robot.oi.left.setNeutralMode(NeutralMode.Brake);
	}
	
	public void setArcadeSpeed(double x, double y){
		robotDrive.arcadeDrive(y, x);
	}
}
