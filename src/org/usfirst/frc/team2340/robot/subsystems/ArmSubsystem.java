package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.ArmCommand;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class ArmSubsystem extends Subsystem {
	static private ArmSubsystem subsystem;
	private Compressor compressor;
	private Solenoid festioSolenoid;
	private Solenoid festioSolenoid2;
	private ArmSubsystem() {
		createArmone();
		createArmtwo();
//		compressor = new Compressor();
//		compressor.setClosedLoopControl (true);
//		
//		festioSolenoid = new Solenoid(RobotMap.PNEUMATICS_TAL_ID, 0);
//		festioSolenoid2 = new Solenoid(RobotMap.PNEUMATICS_TAL_ID,1);
		System.out.println( "arm Subsystem created");
		
	}

	public static ArmSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new ArmSubsystem();
		}
		return subsystem;
	}
	private void createArmone() {
		try {
			Robot.oi.armone = new WPI_TalonSRX(RobotMap.ARM_ONE_TAL_ID);
			Robot.oi.armone.configNominalOutputForward(0,0);
			Robot.oi.armone.configNominalOutputReverse(0,0);
		    Robot.oi.armone.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("createArmone FAILED");
		}
		}
	private void createArmtwo() {
		try {
			Robot.oi.armtwo = new WPI_TalonSRX(RobotMap.ARM_TWO_TAL_ID);
			Robot.oi.armtwo.configNominalOutputForward(0,0);
			Robot.oi.armtwo.configNominalOutputReverse(0,0);
		    Robot.oi.armtwo.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("createArmone FAILED");
	
		}
	}
	
	public void move(double amt) {
		Robot.oi.armone.set(amt);
		Robot.oi.armtwo.set(-amt);
	}
	
	public void open() {
		festioSolenoid.set(true);
		festioSolenoid2.set(true);
	}
	public void close() {
		festioSolenoid.set(false);
		festioSolenoid2.set(false);
	}
	
	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new ArmCommand());
	}
}
