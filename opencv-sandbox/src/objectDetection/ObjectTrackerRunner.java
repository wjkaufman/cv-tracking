package objectDetection;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class ObjectTrackerRunner {
	static final public int WIDTH = 640, HEIGHT = 480;
	static final public double CAPTURE_SCALE = 0.4;
	public static int FRAME = 0;
	
	public static void main(String arg[]) throws InterruptedException{
		
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      

      //make the JFrame
      JFrame frame = new JFrame("WebCam Capture - Object Tracking");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      
      ObjectTracker tracker = new ObjectTracker();
      ObjectPanel panel = new ObjectPanel();
      
      frame.setSize(WIDTH, HEIGHT);
      frame.setBackground(Color.BLACK);
      frame.add(panel);
      frame.setVisible(true);
    
      //Open and Read from the video stream
       Mat webcam_image=new Mat();
       VideoCapture capture =new VideoCapture(0);
       
       capture.set(3, WIDTH * CAPTURE_SCALE);
       capture.set(4, HEIGHT * CAPTURE_SCALE);
 
        if( capture.isOpened())
          {
           Thread.sleep(500); /// This one-time delay allows the Webcam to initialize itself
           while( true )
           {
        	 capture.read(webcam_image);
             if( !webcam_image.empty() )
              { 
            	  Thread.sleep(30); /// This delay eases the computational load .. with little performance leakage
                   // frame.setSize(webcam_image.width()*2+40,webcam_image.height()*2+60);
                   //Apply the classifier to the captured image
                   webcam_image = tracker.detect(webcam_image);
                  //Display the image
                   panel.matToBufferedImage(webcam_image);
                   panel.repaint();
                   ObjectTrackerRunner.FRAME ++;
              }
              else
              {
                   System.out.println(" --(!) No captured frame from webcam !"); 
                   break; 
              }
             }
            }
           capture.release(); //release the capture
 
      } //end main 
	
}