import java.util.List;
import java.util.ArrayList;

import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

public class ObjectTracker {
	final int SENSITIVITY_VALUE = 20;
	final int BLUR_SIZE = 10;
	int[] theObject = {0,0};
	
	Rect objectBoundingRectangle = new Rect(0,0,0,0);
	
	public void searchForMovement(Mat thresholdImage, Mat cameraFeed) {
		boolean objectDetected = false;
		Mat temp = null;
		thresholdImage.copyTo(temp);
		
		List<MatOfPoint> contours = new List<MatOfPoint>(); // ***why is this giving me errors?
		Mat hierarchy = null;
		Imgproc.findContours(temp, contours, hierarchy, 0, 1);
		
		if (contours.size() > 0) objectDetected = true;
		else objectDetected = false;
		
		if (objectDetected) {
			List<MatOfPoint> largestContourVec;
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
	
	public static void main (String args) {
		
		// actually add stuff here ***
		
	}
}
