package colorTracking;

import java.util.List;
import java.util.ArrayList;

import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;


public class ColorTracker {
	public final static int SENSITIVITY_VALUE = 20;
	public final static int BLUR_SIZE = 30;
	int[] theObject = {0,0};
	
	Rect objectBoundingRectangle = new Rect(0,0,0,0);
	
	public void searchForMovement(Mat thresholdImage, Mat cameraFeed) {
		boolean objectDetected = false;
		Mat temp = new Mat();
		thresholdImage.copyTo(temp);
		Imgproc.cvtColor( temp, temp, Imgproc.COLOR_BGR2GRAY);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		if (contours.size() > 0) objectDetected = true;
		else objectDetected = false;
		
		if (objectDetected) {
			List<MatOfPoint> largestContourVec = new ArrayList<MatOfPoint>();
			largestContourVec.add(contours.get(contours.size()-1));
			Rect objectBoundingRectangle = Imgproc.boundingRect(largestContourVec.get(0));
			int xpos = objectBoundingRectangle.x + objectBoundingRectangle.width / 2;
			int ypos = objectBoundingRectangle.y + objectBoundingRectangle.height / 2;
			
			theObject[0] = xpos;
			theObject[1] = ypos;
		}
		
		int x = theObject[0];
		int y = theObject[1];
		Point point = new Point(x,y);
		Scalar color = new Scalar(57, 255, 20);
		Core.circle(cameraFeed, point, 20, color);
		Core.putText(cameraFeed, "Tracking object at (" + 
		x + ", " + y + ")", point, 1, 1, color, 2);
	}

}
