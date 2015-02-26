package colorTracking;


import java.awt.Color;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class ColorTrackerRunner {
	public static final int WIDTH = 720, HEIGHT = 480;
	public static final double CAPTURE_SCALE = 1;
	public static int FRAME = 0;
	
	public static void main(String arg[]) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//make the JFrame
		JFrame frame = new JFrame("WebCam Capture - Object Tracking");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		ColorTracker tracker = new ColorTracker();
		GraphicsPanel panel = new GraphicsPanel();
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setBackground(Color.BLACK);
		frame.add(panel);
		frame.setVisible(true);
		
		Mat videoFrame;
		Mat hsvImage;
		Mat threshold;
		
		videoFrame = new Mat();
		hsvImage = new Mat();
		threshold = new Mat();
		
		VideoCapture capture = new VideoCapture(0);
		
		capture.set(3, WIDTH * CAPTURE_SCALE);
		capture.set(4, HEIGHT * CAPTURE_SCALE);
		
		if (capture.isOpened()) {
			try {
				Thread.sleep(500);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			while (true) {
				capture.read(videoFrame);
				//converts videoFrame to hsv colorspace
				Imgproc.cvtColor(videoFrame, hsvImage, Imgproc.COLOR_BGR2HSV);
				
				//what does this do?
				Core.inRange(hsvImage, tracker.getMinHSV(), tracker.getMaxHSV(), threshold);
				
				if (tracker.useMorphOps()) {
					tracker.morphOps(threshold);
				}
				
				if (true) { //change to "if tracking is on"
					tracker.trackColor(threshold, videoFrame);
				}
				
				panel.matToBufferedImage(videoFrame);
				panel.repaint();
				
				FRAME++;
				
			}
		}
		
		else {
			System.out.println("Problem initializing videocapture");
		}

	} //end of main
	
} //end of runner