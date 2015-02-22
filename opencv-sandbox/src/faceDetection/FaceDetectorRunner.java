package faceDetection;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

public class FaceDetectorRunner {
	static final public int WIDTH = 1200, HEIGHT = 1000;
	static final public double CAPTURE_SCALE = 0.2;
	public static int FRAME = 0;
	
	public static void main(String arg[]) throws InterruptedException{
      // Load the native library.
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      //or ...     System.loadLibrary("opencv_java244");

      //make the JFrame
      JFrame frame = new JFrame("WebCam Capture - Face detection");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   
      FaceDetector faceDetector = new FaceDetector();
      FacePanel facePanel = new FacePanel();
      frame.setSize(WIDTH, HEIGHT);
      frame.setBackground(Color.BLACK);
      frame.add(facePanel);
      frame.setVisible(true);
    
      //Open and Read from the video stream
       Mat webcam_image=new Mat();
       VideoCapture webCam =new VideoCapture(0);
       webCam.set(3, WIDTH * CAPTURE_SCALE);
       webCam.set(4, HEIGHT * CAPTURE_SCALE);
 
        if( webCam.isOpened())
          {
           Thread.sleep(500); /// This one-time delay allows the Webcam to initialize itself
           while( true )
           {
        	 webCam.read(webcam_image);
             if( !webcam_image.empty() )
              { 
            	  Thread.sleep(30); /// This delay eases the computational load .. with little performance leakage
                   // frame.setSize(webcam_image.width()*2+40,webcam_image.height()*2+60);
                   //Apply the classifier to the captured image
                   webcam_image = faceDetector.detect(webcam_image);
                  //Display the image
                   facePanel.matToBufferedImage(webcam_image);
                   facePanel.repaint();
                   FaceDetectorRunner.FRAME ++;
              }
              else
              {
                   System.out.println(" --(!) No captured frame from webcam !"); 
                   break; 
              }
             }
            }
           webCam.release(); //release the webcam
 
      } //end main 
	
}