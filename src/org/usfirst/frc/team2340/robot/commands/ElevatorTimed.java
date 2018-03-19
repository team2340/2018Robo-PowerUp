package org.usfirst.frc.team2340.robot.commands;

import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class ElevatorTimed extends Command {
	long startTime = 0;
	double delay = 0;

	public ElevatorTimed(double _delay) {
		requires(Robot.elevator);
		delay = _delay;
	}
	@Override
	protected void initialize() {
		Robot.myLogger.log("ElevatorTimed", "", "");
		startTime = System.currentTimeMillis();
		Robot.myLogger.log("ElevatorTimer","start",startTime);
		Robot.myLogger.log("ElevatorTimer","delay",delay); 
//		System.out.println("ELEVATOR TIMED!!!!! Start " + startTime + " " + delay); fish
	}

	@Override
	protected void execute() {
		Robot.elevator.move(1);
	}

	@Override
	protected boolean isFinished() {
		if (System.currentTimeMillis() >= (startTime + delay*1000)) {
			Robot.myLogger.log("ElevatorTimer","Done","");
//			System.out.println("TIMER DONE!!!!!"); fish
			return true;
		}
		else {
			return false;
		}

	}
}
