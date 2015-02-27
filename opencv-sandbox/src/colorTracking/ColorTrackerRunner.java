package colorTracking;


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

public class ColorTrackerRunner implements ActionListener{
	
	GraphicsFrame window1;
	GraphicsFrame window2;
	
	HSVFrame hsv_min;
	HSVFrame hsv_max;
	
	ColorTracker tracker;
	
	Mat videoFrame;
	Mat hsvFrame;
	Mat threshFrame;
	
	VideoCapture capture;
	
	public ColorTrackerRunner() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//make the JFrame
		window1 = new GraphicsFrame("webcam capture - color tracking");
		window2 = new GraphicsFrame("webcam capture - threshold");
		
		hsv_min = new HSVFrame(HSVFrame.HSV_MIN, "min hsv", 0, 0, 0);
		hsv_max = new HSVFrame(HSVFrame.HSV_MAX, "max hsv", 360, 255, 255);
		hsv_min.addActionListener(this);
		hsv_max.addActionListener(this);
		
		tracker = new ColorTracker();
		
		videoFrame = new Mat();
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
				capture.read(videoFrame);
				//converts videoFrame to hsv colorspace
				Imgproc.cvtColor(videoFrame, hsvFrame, Imgproc.COLOR_BGR2HSV);
				
				//what does this do?
				Core.inRange(hsvFrame, tracker.getMinHSV(), tracker.getMaxHSV(), threshFrame);
				
				if (tracker.useMorphOps()) {
					tracker.morphOps(threshFrame);
				}
				
				if (true) { //change to "if tracking is on"
					tracker.trackColor(threshFrame, videoFrame);
				}
				
				window1.updateImage(videoFrame);
				window2.updateImage(threshFrame);
				
				
				GraphicsFrame.FRAME++;
				
			}
		}
		
		else {
			System.out.println("Problem initializing videocapture");
		}
	}
	
	public static void main(String arg[]) {
		
		ColorTrackerRunner runner = new ColorTrackerRunner();
		
	} //end of main

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof HSVFrame) {
			if (e.getSource() == hsv_min) {
				tracker.setMinHSV(hsv_min.getH(), hsv_min.getS(), hsv_min.getV());
			}
			else if (e.getSource() == hsv_max) {
				tracker.setMaxHSV(hsv_max.getH(), hsv_max.getS(), hsv_max.getV());
			}
		}
	}
	
} //end of runner