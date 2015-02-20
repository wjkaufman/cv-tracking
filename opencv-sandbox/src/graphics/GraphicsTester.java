package graphics;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

public class GraphicsTester {
	public static final int WIDTH = 480, HEIGHT = 360;
	public static final double CAPTURE_SCALE = .6;
	public static int FRAME = 0;
	
	public static void main(String arg[]) {
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		//make the JFrame
		JFrame frame = new JFrame("WebCam Capture - Object Tracking");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		GraphicsPanel panel = new GraphicsPanel();
		panel.addMouseListener(new GraphicsPanel());
		panel.addMouseMotionListener(new GraphicsPanel());
		
		frame.setSize(WIDTH, HEIGHT);
		frame.setBackground(Color.BLACK);
		frame.add(panel);
		frame.setVisible(true);
		
		Mat frame1;
		
		frame1 = new Mat();
		
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
				capture.read(frame1);
				
				try{
					panel.matToBufferedImage(frame1);
					panel.repaint();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
				FRAME++;
				
			}
		}
		
		else {
			System.out.println("Problem initializing videocapture");
		}

	} //end of main
	
} //end of runner