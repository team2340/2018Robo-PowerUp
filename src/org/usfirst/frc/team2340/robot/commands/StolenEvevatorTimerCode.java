package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class StolenEvevatorTimerCode extends Command {
	long startTime = 0;
	double delay = 0;

	public StolenEvevatorTimerCode(double _delay) {
		requires(Robot.elevator);
		delay = _delay;
	}
	@Override
	protected void initialize() {
		Robot.myLogger.log("ElevatorTimed", "", "");
		startTime = System.currentTimeMillis();
		System.out.println("ELEVATOR TIMED!!!!! Start " + startTime + " " + delay);
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
