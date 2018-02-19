package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.Arm;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;

public class ArmSubsystem extends GenericSubsystem {
	private Compressor compressor;
	private Solenoid festioSolenoid;
	private Solenoid festioSolenoid2;
	
	public ArmSubsystem() {
		createArmOne();
		createArmTwo();
		compressor = new Compressor();
		compressor.setClosedLoopControl (true);
		
		festioSolenoid = new Solenoid(RobotMap.PNEUMATICS_TAL_ID, 0);
		festioSolenoid2 = new Solenoid(RobotMap.PNEUMATICS_TAL_ID,1);
		System.out.println( "acquistion Subsystem created");
		
	}
	
	private void createArmOne() {
		try {
			Robot.oi.armOne = new WPI_TalonSRX(RobotMap.ARM_ONE_TAL_ID);
		} catch (Exception ex) {
			System.out.println("createArmone FAILED");
		}
		}
	private void createArmTwo() {
		try {
			Robot.oi.armTwo = new WPI_TalonSRX(RobotMap.ARM_TWO_TAL_ID);
		} catch (Exception ex) {
			System.out.println("createArmone FAILED");
	
		}
	}
	
	public void move(double _val) {
		Robot.oi.armOne.set(ControlMode.PercentOutput, _val);
		Robot.oi.armTwo.set(ControlMode.PercentOutput, -_val);
	}
	
	public void move(double _val, ControlMode _mode) {
		Robot.oi.armOne.set(_mode, _val);
		Robot.oi.armTwo.set(_mode, -_val);
	}
	
	@Override
	public void stop() {
		move(0, ControlMode.PercentOutput);
	}

	public void open() {
		festioSolenoid.set(true);
		festioSolenoid2.set(true);
	}

	public void close() {
		festioSolenoid.set(false);
		festioSolenoid2.set(false);
	}

	public void toggle() {
		if (festioSolenoid.get() && festioSolenoid2.get()) {
			close();
		}
		else {
			open();
		}
	}
	
	@Override
	public int getEncoder(int _id) {
		return 0;
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new Arm());
	}
}
