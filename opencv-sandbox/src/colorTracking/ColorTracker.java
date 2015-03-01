package colorTracking;

import java.awt.Color;
import java.util.List;
import java.util.ArrayList;

import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.core.*;

public class ColorTracker {
	//hsv color values for tracking the specific object
	private int H_MIN = 0;
	private int H_MAX = 360;
	private int S_MIN = 0;
	private int S_MAX = 255;
	private int V_MIN = 0;
	private int V_MAX = 255;
	
	private Rect myROI = new Rect(0,0,0,0);
	
	private double x;
	private double y;
	
	//max number of objects to be tracked via this method
	private final int MAX_NUM_OBJECTS=50;
	
	private boolean objectFound = false;
	private boolean useMorphOps = true;
	
	//min and max object area to be tracked
	private final int MIN_OBJECT_AREA = 20*20;
	private final int MAX_OBJECT_AREA = (int)(GraphicsFrame.WIDTH*GraphicsFrame.HEIGHT / 1.5);
	
	public Scalar getMinHSV() {
		return new Scalar(H_MIN, S_MIN, V_MIN);
	}
	
	public Scalar getMaxHSV() {
		return new Scalar(H_MAX, S_MAX, V_MAX);
	}
	
	public void setMinHSV(int h_val, int s_val, int v_val) {
		H_MIN = h_val;
		S_MIN = s_val;
		V_MIN = v_val;
	}
	
	public void setMaxHSV(int h_val, int s_val, int v_val) {
		H_MAX = h_val;
		S_MAX = s_val;
		V_MAX = v_val;
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
		
		System.out.println("myROI data 0: " + myROI.x + ", " + myROI.y +
						   ", " + myROI.width + ", " + myROI.height);
		
		if (myROI.area() > 0) { //not sure if this will work, but adds region of interest to mat
			temp = new Mat(temp, myROI);
		}
		
		System.out.println("myROI data 1: " + myROI.x + ", " + myROI.y +
				   ", " + myROI.width + ", " + myROI.height);
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_EXTERNAL,
							 Imgproc.CHAIN_APPROX_SIMPLE);
		
		int numObjects;
		
		if (contours.size() > 0) {
			objectFound = true;
			numObjects = contours.size();
		}
		else objectFound = false;
		
		if (objectFound) {
			List<MatOfPoint> largestContourVec = new ArrayList<MatOfPoint>();
			
			largestContourVec.add(contours.get(contours.size() - 1));
			
			System.out.println("myROI data 2: " + myROI.x + ", " + myROI.y +
					   ", " + myROI.width + ", " + myROI.height);
			
			myROI = Imgproc.boundingRect(largestContourVec.get(0));
			//this appears to be constricting the myROI by 2 in width and height
			myROI.width  += 2;
			myROI.height += 2; //stopped working here
			
			x = myROI.x + myROI.width / 2;
			y = myROI.y + myROI.height / 2;
			
			System.out.println("myROI data 3: " + myROI.x + ", " + myROI.y +
					   ", " + myROI.width + ", " + myROI.height);
						
			//draws text and bounding rectangle to image
			Core.putText(cameraFeed, "tracking object", new Point(x,y), 2,
						 1 * GraphicsFrame.CAPTURE_SCALE, new Scalar(0,255,0),
						 (int)(2 * GraphicsFrame.CAPTURE_SCALE));
			Core.rectangle(cameraFeed, new Point(myROI.x, myROI.y),
						   new Point(myROI.x + myROI.width, myROI.y + myROI.height), new Scalar(0,255,0));
			
			System.out.println("tracking object\n");
			System.out.println("x: " + x + ", y: " + y);
		}
		else {
			System.out.println("object not found");
			double[] vals = {0,0,0,0};
			myROI.set(vals);
		}
		
	}
}
