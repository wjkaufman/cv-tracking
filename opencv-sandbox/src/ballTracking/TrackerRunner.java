package ballTracking;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class TrackerRunner implements ActionListener{
	
	GraphicsFrame window1;
	GraphicsFrame window2;
	
	HSVFrame hsv_min;
	HSVFrame hsv_max;
	
	ColorTracker colorTracker;
	MotionTracker motionTracker;
	
	Mat videoFrame1;
	Mat videoFrame2;
	Mat hsvFrame;
	Mat threshFrame;
	
	VideoCapture capture;
	
	public TrackerRunner() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//make the JFrame
		window1 = new GraphicsFrame("webcam capture - color tracking");
		window2 = new GraphicsFrame("webcam capture - threshold");
		
		hsv_min = new HSVFrame(HSVFrame.HSV_MIN, "min hsv", 0, 0, 0);
		hsv_max = new HSVFrame(HSVFrame.HSV_MAX, "max hsv", 360, 255, 255);
		hsv_min.addActionListener(this);
		hsv_max.addActionListener(this);
		
		colorTracker = new ColorTracker();
		motionTracker = new MotionTracker();
		
		videoFrame1 = new Mat();
		videoFrame2 = new Mat();
		hsvFrame = new Mat();
		threshFrame = new Mat();
		
		capture = new VideoCapture(0);
		
		capture.set(3, GraphicsFrame.WIDTH * GraphicsFrame.CAPTURE_SCALE);
		capture.set(4, GraphicsFrame.HEIGHT * GraphicsFrame.CAPTURE_SCALE);
		
		this.start();
	}
	
	public void start() {
		if (capture.isOpened()) {
			try {
				Thread.sleep(500);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			while (true) {
				capture.read(videoFrame1);
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				capture.read(videoFrame2);
				
				if (true) { //change to "if tracking is on"
					//colorTracker.trackColor(videoFrame1, videoFrame1);
					motionTracker.trackMotion(videoFrame1, videoFrame2, videoFrame1);
				}
				
				window1.updateImage(videoFrame1);
				window2.updateImage(motionTracker.getThreshold());
				
				GraphicsFrame.FRAME++;
				
			}//end infinite while loop
		}//end if capture is opened
		
		else {
			System.out.println("Problem initializing videocapture");
		}
	}
	
	public static void main(String arg[]) {
		
		TrackerRunner runner = new TrackerRunner();
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof HSVFrame) {
			if (e.getSource() == hsv_min) {
				colorTracker.setMinHSV(hsv_min.getH(), hsv_min.getS(), hsv_min.getV());
			}
			else if (e.getSource() == hsv_max) {
				colorTracker.setMaxHSV(hsv_max.getH(), hsv_max.getS(), hsv_max.getV());
			}
		}
	}
	
} //end of runner