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
	public double speedP = 1.5;
	public double speedI = 0.0;
	public double speedD = 0.0;
	public double speedF = 0.001;
	public double speedPeakOutputVoltage = 1f;
	
	//public double positionP = 0.08525; //25% power at 3000 error ((%*1023)/desiered error)- increases power or drecers error ( bigger nubmer, closere to one) pos- 15% power at 1024/)
//	public double positionP = 0.127875;// 25% power at 2000 error
	public double positionP = 0.25575;// 25% power at 1000 error
//	public double positionP = 0.1023;// 30% power at 3000 error
//	public double positionP = 0.15345;// 30% power at 2000 error
//	public double positionP = 0.1023;// 30% power at 1000 error
//	public double positionI = 0.00001; 
	//public double positionD = 10.0;
	

	public double positionI = 0.000;
	public double positionD = 0.0;
	public double positionF = 0.0;
	public float positionPeakOutputVoltage = 10.0f/12.0f;
	
	public double vBusMaxOutput = 1.0; //An output multiplier
	public double vBusPeakOutputVoltage = 1f; //the peak output (between 0 and 1)

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
		createLeftSide();
		createRightSide();
		setBrakeMode(true);
		setForPosition();
		robotDrive = new DifferentialDrive(Robot.oi.left, Robot.oi.right);
		robotDrive.setSafetyEnabled(false);
	}

	private void createLeftSide() {
		try {
			Robot.oi.left = new WPI_TalonSRX(RobotMap.LEFT_TAL_ID);
			Robot.oi.left.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		    Robot.oi.left.selectProfileSlot(0,0);
		    Robot.oi.left.setSensorPhase(true);
		} catch (Exception ex) {
			System.out.println("createLeftSide FAILED");
		}
	}

	private void createRightSide() {
		try {
			Robot.oi.right = new WPI_TalonSRX(RobotMap.RIGHT_TAL_ID);

			Robot.oi.right.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
		    Robot.oi.right.selectProfileSlot(0,0);
		    Robot.oi.right.setSensorPhase(true);
		} catch (Exception ex) {
			System.out.println("createRightSide FAILED");
		}
	}
	
	public void setForSpeed() {
		Robot.oi.left.set(ControlMode.Velocity, 0);
		Robot.oi.right.set(ControlMode.Velocity, 0);
		Robot.oi.right.config_kF(0,speedF,0);
	    Robot.oi.right.config_kP(0,speedP,0);
	    Robot.oi.right.config_kI(0,speedI,0); 
	    Robot.oi.right.config_kD(0,speedD,0);
	    Robot.oi.left.config_kF(0,speedF,0);
	    Robot.oi.left.config_kP(0,speedP,0);  
	    Robot.oi.left.config_kI(0,speedI,0);  
	    Robot.oi.left.config_kD(0,speedD,0);
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
        Robot.oi.left.configPeakOutputForward(vBusPeakOutputVoltage,0); 
	    Robot.oi.left.configPeakOutputReverse(-vBusPeakOutputVoltage,0);
	    Robot.oi.right.configPeakOutputForward(vBusPeakOutputVoltage,0);
	    Robot.oi.right.configPeakOutputReverse(-vBusPeakOutputVoltage,0);
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
