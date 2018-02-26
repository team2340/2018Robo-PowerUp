package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Timer extends Command {
	long startTime = 0;
	double delay = 0;

	@Override
	protected void initialize() {
		Robot.myLogger.log("Timer", "", "");
		startTime = System.currentTimeMillis();
		delay = SmartDashboard.getNumber("Delay", 0);
		System.out.println("TIMER!!!!! Start " + startTime + " " + delay);
	}

	@Override
	protected void execute() {
	}

	@Override
	protected boolean isFinished() {
		if (System.currentTimeMillis() >= (startTime + delay*1000)) {
			System.out.println("TIMER DONE!!!!!");
			return true;
		}
		else {
			return false;
		}

	}
}
