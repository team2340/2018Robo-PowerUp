package org.usfirst.frc.team2340.robot.commands;

import java.util.ArrayList;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import org.usfirst.frc.team2340.grip.GripPipeline;
import org.usfirst.frc.team2340.robot.Robot;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.VisionThread;

public class VisionCommand {
	private VisionThread visionThread;
	private final Object imgLock = new Object();
	public int prevNumTargets = 0;

	public VisionCommand(UsbCamera _camera) {
		// when we didn't have the camera open in this resolution 640x480, the grip pipeline was 
		// finding a different number of targets than the computer would find attached to the 
		// same camera in the grip application.
		visionThread = new VisionThread(_camera, new GripPipeline(), grip -> {
			if(!grip.filterContoursOutput().isEmpty()){
				ArrayList<MatOfPoint> contours = grip.filterContoursOutput();
				ArrayList<MatOfPoint> targets = new ArrayList<MatOfPoint>();
				for(MatOfPoint point : contours){
					double expectedRation = 2.54;
					double tolerance = 2;
					Rect r = Imgproc.boundingRect(point);
					double ration = r.height/r.width;

					if(ration < expectedRation + tolerance && ration > expectedRation - tolerance){
						targets.add(point);
					}
				}
				if ( prevNumTargets != targets.size()) { 
					prevNumTargets = targets.size();
					System.out.println(System.currentTimeMillis() + " num targets: " + prevNumTargets);
				}
				if(targets.size() == 2){
					Rect r = Imgproc.boundingRect(grip.filterContoursOutput().get(0));
					Rect q = Imgproc.boundingRect(grip.filterContoursOutput().get(1));
					synchronized(imgLock){
						Robot.drive.centerX = (r.x + (r.width/2) + q.x + (q.width/2))/2.0;

						double leftmost=0.0;
						double rightmost=0.0;
						double pxBetweenTargets=0.0;
						double angleBetweenTargets=0.0;
						double halfAngleBetweenTargets=0.0;
						double lengthOfOpposite=5.125;
						double distanceFromTarget =0.0;
						if ( r.x < q.x) {
							leftmost = r.x;
							rightmost = q.x+ q.width;
						} else {
							leftmost = q.x;
							rightmost = r.x+ r.width;
						}
						pxBetweenTargets = rightmost - leftmost;
						angleBetweenTargets = (Robot.oi.CAM_VIEWING_ANGLE * pxBetweenTargets)/Robot.oi.IMG_WIDTH;
						halfAngleBetweenTargets = angleBetweenTargets/2.0;
						double radians = Math.toRadians(halfAngleBetweenTargets);
						distanceFromTarget = lengthOfOpposite/ (Math.tan(radians));
						if ( distanceFromTarget >= 0 ) {
							Robot.drive.finalDistance = distanceFromTarget;
						}
						System.out.println(System.currentTimeMillis() + " r.x: "+r.x+", r.width: "+r.width
								+", q.x: "+q.x+", q.width: "+q.width
								+", centerX: "+Robot.drive.centerX + ", distance: "+distanceFromTarget);
						//System.out.println("leftmost: " + leftmost + " rightmost: " + rightmost);
					}
				} else {
					Robot.drive.centerX = -1;
				}
				SmartDashboard.putNumber("CenterX", Robot.drive.centerX);
			} else {
				Robot.drive.centerX = -1;
			}
		});
	}
	
	public void start() {
		visionThread.start();
	}
}
