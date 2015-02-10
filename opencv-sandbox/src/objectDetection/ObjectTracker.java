package objectDetection;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.opencv.highgui.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;
import org.opencv.objdetect.CascadeClassifier;

public class ObjectTracker {
	public final static int SENSITIVITY_VALUE = 20;
	public final static int BLUR_SIZE = 10;
	int[] theObject = {0,0};
	
	Rect objectBoundingRectangle = new Rect(0,0,0,0);
	
	public void searchForMovement(Mat thresholdImage, Mat cameraFeed) {
		boolean objectDetected = false;
		Mat temp = new Mat();
		thresholdImage.copyTo(temp);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, 0, 1);
		
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
	
	public static void main (String args) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		ObjectTracker myTracker = new ObjectTracker();
		
		boolean objectDetected = false;
		boolean debugMode = false;
		boolean trackingEnabled = false;
		boolean pause = false;
		
		Mat frame1, frame2;
		Mat grayImage1, grayImage2;
		Mat differenceImage;
		Mat thresholdImage;
		
		frame1 = new Mat();
		frame2 = new Mat();
		grayImage1 = new Mat();
		grayImage2 = new Mat();
		differenceImage = new Mat();
		thresholdImage = new Mat();
		
		VideoCapture capture = new VideoCapture();
		
		while (true) {
			
			capture.open("bouncingBall.avi"); // *** will need to update this
			
			if (!capture.isOpened()) {
				System.out.println("VideoCapture error");
				
			}
			
			else {
				
				for (int i = 0; i < 38; i++) {
					System.out.println("" + i + ": " + capture.get(i));
				}
				
				while (capture.get(1) < capture.get(7) - 1) { // there may be problems here ***
					capture.read(frame1);
					Imgproc.cvtColor(frame1, grayImage1, Imgproc.COLOR_BGR2GRAY);
					
					capture.read(frame2);
					Imgproc.cvtColor(frame2, grayImage2, Imgproc.COLOR_BGR2GRAY);
					
					Core.absdiff(frame1, frame2, differenceImage);
					Imgproc.threshold(differenceImage, thresholdImage, SENSITIVITY_VALUE,
								      255, Imgproc.THRESH_BINARY);
					
					if (debugMode) {
						//can add things here later
					}
					else {
						//may need to destroy the other windows
					}
					
					Size mySize = new Size(BLUR_SIZE, BLUR_SIZE);
					Imgproc.blur(thresholdImage, thresholdImage, mySize);
					Imgproc.threshold(thresholdImage, thresholdImage, SENSITIVITY_VALUE,
						      255, Imgproc.THRESH_BINARY);
					
					if (debugMode) {
						//more stuff here that I'll add later
					}
					else {
						//yep, more stuff
					}
					
					if (trackingEnabled) {
						myTracker.searchForMovement(thresholdImage, frame1);
					}
					
					//show captured frame frame1
				}
			}
			capture.release();
		}
	}
}
