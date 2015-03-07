package ballTracking;

import java.util.List;
import java.util.ArrayList;

import org.opencv.imgproc.Imgproc;
import org.opencv.core.*;


public class MotionTracker {
	public static final int SENSITIVITY_VALUE = 20;
	public static final int BLUR_SIZE = 30;
	
	private int numObjects = 0;
	private int MAX_NUM_OBJECTS = 10;
	private int MIN_OBJECT_AREA = 600;
	private int MAX_OBJECT_AREA = GraphicsFrame.WIDTH * GraphicsFrame.HEIGHT / 2;
	
	private Mat difference, threshold;
	
	public MotionTracker() {
		difference = new Mat();
		threshold = new Mat();
	}
	
	private List<Rect> myROIs = new ArrayList<Rect>();
	
	public void resetMyROIs() {
		myROIs.clear();
	}
	
	public Mat getDifference() {
		return difference;
	}
	
	public Mat getThreshold() {
		return threshold;
	}
	
	public void trackMotion(Mat frame1, Mat frame2, Mat cameraFeed) {
		difference = new Mat();
		threshold  = new Mat();
		
		Core.absdiff(frame1, frame2, difference);
		
		Imgproc.threshold(difference, threshold, SENSITIVITY_VALUE, 255, Imgproc.THRESH_BINARY);
		
		Size mySize = new Size(BLUR_SIZE, BLUR_SIZE);
		Imgproc.blur(threshold, threshold, mySize);
		
		Imgproc.threshold(threshold, threshold, SENSITIVITY_VALUE, 255, Imgproc.THRESH_BINARY);
		//redundant from above?
		
		trackMotion(threshold, cameraFeed);
	}
	
	public void trackMotion(Mat threshold, Mat cameraFeed) {
		boolean objectFound = false;
		Mat temp = new Mat();
		threshold.copyTo(temp);
		Imgproc.cvtColor( temp, temp, Imgproc.COLOR_BGR2GRAY);
		
		resetMyROIs();
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_EXTERNAL,
							 Imgproc.CHAIN_APPROX_SIMPLE);
		
		if (contours.size() > 0) objectFound = true;
		else objectFound = false;
		
		if (objectFound) {
			System.out.println("object(s) found");
			double largestArea = 0;
			double smallestArea = 1000000000;
			
			
			for (MatOfPoint mop : contours) {
				Rect nextRect = Imgproc.boundingRect(mop);
				if (nextRect.area() > MIN_OBJECT_AREA && nextRect.area() < MAX_OBJECT_AREA) {
					if (nextRect.area() > largestArea) largestArea = nextRect.area();
					if (nextRect.area() < smallestArea) smallestArea = nextRect.area();
					myROIs.add(nextRect);
					numObjects++;
				}
			}
			
			int rectCounter = 0;
			
			for (Rect boundingRect : myROIs) {
				double x = boundingRect.x + boundingRect.width / 2;
				double y = boundingRect.y + boundingRect.height / 2;
				
				Core.putText(cameraFeed, "tracking object: " + rectCounter, new Point(x,y), 2,
						 .67 * GraphicsFrame.CAPTURE_SCALE, new Scalar(0,255,0),
						 (int)(2 * GraphicsFrame.CAPTURE_SCALE));
				
				Core.rectangle(cameraFeed, new Point(boundingRect.x, boundingRect.y),
						   	   new Point(boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height),
						   	   new Scalar(0,255,0));
				rectCounter++;
			}
			
			System.out.println("tracking " + myROIs.size() + " object(s)\n");
		}//end if Object found
	}//end trackMotion

}
