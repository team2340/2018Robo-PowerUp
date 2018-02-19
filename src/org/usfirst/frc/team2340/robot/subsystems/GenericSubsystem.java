package org.usfirst.frc.team2340.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.command.Subsystem;

public abstract class GenericSubsystem extends Subsystem {
	
	public abstract int getEncoder(int _id);
	public abstract void move(double _val, ControlMode _mode);
	public abstract void stop();
}
