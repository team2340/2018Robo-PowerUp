package org.usfirst.frc.team2340.robot;

public class DriveCorrection {
	Tuple<Double, Double> correction = new Tuple<>(0d, 0d);
	
	public Tuple<Double, Double> correction(double _desiredDistance, double _currentAngle, double _angularLimit) {
		if(_currentAngle < -_angularLimit || _currentAngle > _angularLimit) { //Out of range
			correction.x = Math.sin(Math.toRadians(_currentAngle)) * _desiredDistance;
			correction.y = Math.cos(Math.toRadians(_currentAngle)) * _desiredDistance;
			print();
		}
		else {
			System.out.println("All good in the neighborhood");
		}
		return correction;
	}
	
	public Tuple<Double, Double> getCorrection() {
		System.out.print("Getting correction: ");
		print();
		return correction;
	}
	
	public void clear() {
		System.out.print("Clearing Correction: ");
		print();
		correction = new Tuple<>(0d, 0d);
	}
	
	public void print() {
		System.out.println("Off by X: " + correction.x + ", off by Y: " + correction.y);
	}
}