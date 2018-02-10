package org.usfirst.frc.team2340.robot.subsystems;

import org.usfirst.frc.team2340.robot.Robot;
import org.usfirst.frc.team2340.robot.RobotMap;
import org.usfirst.frc.team2340.robot.commands.AcquisitionCommand;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;

public class AcquisitionSubsystem extends Subsystem {
	static private AcquisitionSubsystem subsystem;
	private Compressor compressor;
	private Solenoid festioSolenoid;
	private Solenoid festioSolenoid2;
	private AcquisitionSubsystem() {
		createElevator();
		createClimbing();
		createArmone();
		createArmtwo();
		compressor = new Compressor();
		compressor.setClosedLoopControl (true);
		
		festioSolenoid = new Solenoid(RobotMap.PNEUMATICS_TAL_ID, 0);
		festioSolenoid2 = new Solenoid(RobotMap.PNEUMATICS_TAL_ID,1);
		System.out.println( "acquistion Subsystem created");
		
	}

	public static AcquisitionSubsystem getInstance() {
		if (subsystem == null) {
			subsystem = new AcquisitionSubsystem();
		}
		return subsystem;
	}
	private void createElevator() {
		try {
			Robot.oi.elevator = new WPI_TalonSRX(RobotMap.ELEVATOR_TAL_ID);
			Robot.oi.elevator.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder,0,0);
//			Robot.oi.elevator.reverseSensor(true);
//			Robot.oi.elevator.configEncoderCodesPerRev(360); //TODO: not using 360
			Robot.oi.elevator.configNominalOutputForward(0,0);
			Robot.oi.elevator.configNominalOutputReverse(0,0);
		    Robot.oi.elevator.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("createElevator FAILED");
		}
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
	private void createClimbing() {
		try {
			Robot.oi.climbing = new WPI_TalonSRX(RobotMap.CLIMBING_TAL_ID);
			Robot.oi.climbing.configNominalOutputForward(0,0);
			Robot.oi.climbing.configNominalOutputReverse(0,0);
		    Robot.oi.climbing.selectProfileSlot(0,0);
		} catch (Exception ex) {
			System.out.println("climbing FAILED");
			
		}
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
		setDefaultCommand(new AcquisitionCommand());
	}
}
