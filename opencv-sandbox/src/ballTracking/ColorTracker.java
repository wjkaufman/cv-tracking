package ballTracking;

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
	
	private int numObjects = 0;
	
	private List<Rect> myROIs = new ArrayList<Rect>();
	
	//max number of objects to be tracked via this method
	private final int MAX_NUM_OBJECTS = 7;
	
	private boolean objectFound = false;
	private boolean useMorphOps = true;
	
	//min and max object area to be tracked
	private final double MIN_OBJECT_AREA = 700;
	private final double MAX_OBJECT_AREA = GraphicsFrame.WIDTH * GraphicsFrame.HEIGHT;
	
	public ColorTracker () {
//		Rect initRect = new Rect(0, 0, GraphicsFrame.WIDTH, GraphicsFrame.HEIGHT);
//		myROIs.add(initRect);
	}
	
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
	
	public void resetMyROIs () {
		myROIs.clear();
	}
	
	public void trackColor(Mat thresholdImage, Mat cameraFeed) {
		Mat temp = new Mat();
		thresholdImage.copyTo(temp);
		
		resetMyROIs();
		
		//System.out.println("printing myROI 0: " + myROIs.get(0));
		
		List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_EXTERNAL,
							 Imgproc.CHAIN_APPROX_SIMPLE);
		
		
		if (contours.size() > 0) objectFound = true;
		else objectFound = false;
		
		if (objectFound) {
			//List<MatOfPoint> largestContourVec = new ArrayList<MatOfPoint>();
			//largestContourVec.add(contours.get(0)); //add a for loops here?
			
			double largestArea = 0;
			double smallestArea = 100000000;
			
			numObjects = 0;
			
			for (MatOfPoint mop : contours) {
				Rect nextRect = Imgproc.boundingRect(mop);
				if (nextRect.area() > MIN_OBJECT_AREA && nextRect.area() < MAX_OBJECT_AREA
					&& numObjects < MAX_NUM_OBJECTS) {
					if (nextRect.area() > largestArea) largestArea = nextRect.area();
					if (nextRect.area() < smallestArea) smallestArea = nextRect.area();
					myROIs.add(nextRect);
					numObjects++;
				}
			}
			
			System.out.println("===large-area: " + largestArea);
			System.out.println("===small-area: " + smallestArea);
			
			//draws text and bounding rectangle to image
			
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
		}
	}
}
