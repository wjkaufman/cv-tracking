package ballTracking;

import java.util.List;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class TrackerRunner implements ActionListener{
	
	GraphicsFrame window1;
	GraphicsFrame window2;
	GraphicsFrame window3;
	
	HSVFrame hsv_min;
	HSVFrame hsv_max;
	
	ColorTracker colorTracker;
	MotionTracker motionTracker;
	
	List<Obj> colorBalls = new ArrayList<Obj>();
	List<Obj> motionBalls = new ArrayList<Obj>();
	
	Obj colorBall, motionBall;
	
	Mat videoFrame1;
	Mat videoFrame2;
	Mat displayFrame;
	Mat hsvFrame;
	Mat threshFrame;
	
	VideoCapture capture;
	
	public TrackerRunner() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//make the JFrame
		window1 = new GraphicsFrame("webcam capture");
		window2 = new GraphicsFrame("color tracking threshold");
		window3 = new GraphicsFrame("motion tracking threshold");
		
		
		hsv_min = new HSVFrame(HSVFrame.HSV_MIN, "min hsv", 0, 0, 0);
		hsv_max = new HSVFrame(HSVFrame.HSV_MAX, "max hsv", 360, 255, 255);
		hsv_min.addActionListener(this);
		hsv_max.addActionListener(this);
		
		window1.addActionListener(this);
		window2.addActionListener(this);
		window3.addActionListener(this);
		
		colorTracker = new ColorTracker();
		motionTracker = new MotionTracker();
		colorTracker.addGraphics(false);
		motionTracker.addGraphics(false);
		
		colorBall = new Obj();
		motionBall = new Obj();
		motionBall.drawObj(false);
		
		colorBall.setColor(new Color(0,255,0));
		motionBall.setColor(new Color(255,0,0));
		
		videoFrame1 = new Mat();
		videoFrame2 = new Mat();
		displayFrame = new Mat();
		hsvFrame = new Mat();
		threshFrame = new Mat();
		
		capture = new VideoCapture(0);
		
		capture.set(3, GraphicsFrame.WIDTH * GraphicsFrame.CAPTURE_SCALE);
		capture.set(4, GraphicsFrame.HEIGHT * GraphicsFrame.CAPTURE_SCALE);
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
				colorBalls.clear();
				motionBalls.clear();
				
				capture.read(videoFrame1);
				
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				capture.read(videoFrame2);
				videoFrame1.copyTo(displayFrame);
				
				if (true) { //change to "if tracking is on"
					colorTracker.trackColor(videoFrame1, displayFrame, colorBalls);
					motionTracker.trackMotion(videoFrame1, videoFrame2, displayFrame, motionBalls);
				}
				
				colorBall.addPosition(colorBall.getClosestObj(colorBalls));
				motionBall.addPosition(motionBall.getClosestObj(motionBalls));
				
				window1.getPanel().addObjects(colorBalls);
				window1.addObject(colorBall);
//				window1.addObject(motionBall);
				
				window1.updateImage(displayFrame);
				window2.updateImage(colorTracker.getThreshold());
				window3.updateImage(motionTracker.getThreshold());
				
				window1.getPanel().clearObjects();
				
				colorBall.printData();
				
				GraphicsFrame.FRAME++;
				
			}//end infinite while loop
		}//end if capture is opened
		
		else {
			System.out.println("Problem initializing videocapture");
		}
	}
	
	public static void main(String arg[]) {
		
		TrackerRunner runner = new TrackerRunner();
		runner.start();
		
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() instanceof HSVFrame) {
			if (event.getSource() == hsv_min) {
				colorTracker.setMinHSV(hsv_min.getH(), hsv_min.getS(), hsv_min.getV());
			}
			else if (event.getSource() == hsv_max) {
				colorTracker.setMaxHSV(hsv_max.getH(), hsv_max.getS(), hsv_max.getV());
			}
		}
		else if (event.getSource() instanceof GraphicsPanel) {
			try {
				hsv_min.setHSV(((GraphicsPanel) event.getSource()).getMinHSV());
				hsv_max.setHSV(((GraphicsPanel) event.getSource()).getMaxHSV());
				System.out.println("\n\n");
				((GraphicsPanel)(event.getSource())).printRectData();
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}
	}
	
} //end of runner