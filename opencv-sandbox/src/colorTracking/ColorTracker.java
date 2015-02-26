package colorTracking;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.*;

public class ColorTracker {
	//hsv color values for tracking the specific object
	private int H_MIN = 100;
	private int H_MAX = 120;
	private int S_MIN = 102;
	private int S_MAX = 204;
	private int V_MIN = 204;
	private int V_MAX = 255;
	
	private double x;
	private double y;
	
	
	
	//max number of objects to be tracked via this method
	private final int MAX_NUM_OBJECTS=50;
	
	private boolean objectFound = false;
	private boolean useMorphOps = true;
	
	//min and max object area to be tracked
	private final int MIN_OBJECT_AREA = 20*20;
	private final int MAX_OBJECT_AREA = (int)(ColorTrackerRunner.WIDTH*ColorTrackerRunner.HEIGHT / 1.5);
	
	
	public final static int SENSITIVITY_VALUE = 20;
	public final static int BLUR_SIZE = 30;
	int[] theObject = {0,0};
	
	Rect objectBoundingRectangle = new Rect(0,0,0,0);
	
	public Scalar getMinHSV() {
		return new Scalar(H_MIN, S_MIN, V_MIN);
	}
	
	public Scalar getMaxHSV() {
		return new Scalar(H_MAX, S_MAX, V_MAX);
	}
	
	public boolean useMorphOps() {
		return useMorphOps;
	}
	
	public void morphOps(Mat thresh) {
		Mat erodeElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3,3));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8));
		
		Imgproc.erode(thresh, thresh, erodeElement);
		Imgproc.erode(thresh, thresh, erodeElement);
		
		Imgproc.dilate(thresh, thresh, dilateElement);
		Imgproc.dilate(thresh, thresh, dilateElement);
	}
	
	public void trackColor(Mat thresholdImage, Mat cameraFeed) {
		Mat temp = new Mat();
		thresholdImage.copyTo(temp);
		//Imgproc.cvtColor( temp, temp, Imgproc.COLOR_BGR2GRAY);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
		
		double refArea = 0;
		
		int numObjects = 0;
		
		if (contours.size() > 0) {
			objectFound = true;
			numObjects = contours.size();
		}
		else objectFound = false;
		
		if (objectFound) {
			if (numObjects < MAX_NUM_OBJECTS) {
				for (int index = 0; index < numObjects; index ++) { //may break here
					Moments moment = Imgproc.moments(contours.get(index));
					double area = moment.get_m00();
					
					if (area > MIN_OBJECT_AREA && area < MAX_OBJECT_AREA && area > refArea) {
						x = moment.get_m10();
						y = moment.get_m01();
						
						objectFound = true;
						refArea = area;
					}
					else objectFound = false;
				}
				
				if (objectFound) {
					Core.putText(cameraFeed, "tracking object", new Point(x,y), 2, 1, new Scalar(0,255,0), 2);
					System.out.println("tracking object\n");
					System.out.println("x: " + x + ", y: " + y);
				}
				else System.out.println("object not found, "
						+ "too much noise.  Adjust filter?");
			}
		}
	}
}
