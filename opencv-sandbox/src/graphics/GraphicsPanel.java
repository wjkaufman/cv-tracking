package graphics;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class GraphicsPanel extends JPanel implements MouseListener, MouseMotionListener{
	
    private static final long serialVersionUID = 1L;
    private BufferedImage image;
    private static int startClickX, startClickY, endClickX, endClickY;
    private static int upperClickX, upperClickY;
    
    private static boolean drawMouseBox = false;
     
    // Create a constructor method
    public GraphicsPanel(){
         super();
    }
    /*
     * Converts/writes a Mat into a BufferedImage.
     * 
     * @param matrix Mat of type CV_8UC3 or CV_8UC1
     * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
     */
    public boolean matToBufferedImage(Mat matrix) {
         MatOfByte mb = new MatOfByte();
         Highgui.imencode(".jpg", matrix, mb);
         try {
              this.image = ImageIO.read(new ByteArrayInputStream(mb.toArray()));
         } catch (IOException e) {
              e.printStackTrace();
              return false; // Error
         }
      return true; // Successful
    }
    
    public void paintComponent(Graphics g){
         super.paintComponent(g);
         if (this.image==null) return;
       
         g.drawImage(this.image, 0, 0, GraphicsTester.WIDTH, GraphicsTester.HEIGHT, null);
         g.setColor(Color.WHITE);
         g.setFont(new Font("", 0, 20));
         g.drawString("Frame: " + GraphicsTester.FRAME, 50, 50);
         
         if (drawMouseBox) {
        	 g.setColor(new Color(75, 150, 250, 128));
        	 g.fillRect(10, 10, 10, 10);
//        	 this.printClickArea();
        	 
        	 g.fillRect(upperClickX, upperClickY,
        			    Math.abs(startClickX - endClickX), Math.abs(startClickY - endClickY));
         }
    }
    
    public void printClickArea() {
    	System.out.println("start: " + startClickX + ", " + 
    					   startClickY);
    	System.out.println("end: " + endClickX + ", " + 
				   endClickY);
    }
    
    public static void getDragArea() {
    	if (startClickX < endClickX) {
    		upperClickX = startClickX;
    	}
    	else upperClickX = endClickX;
    	
    	if (startClickY < endClickY) {
    		upperClickY = startClickY;
    	}
    	else upperClickY = endClickY;
    }
    
	@Override
	public void mouseClicked(MouseEvent event) {
//		System.out.println("Mouse clicked, location: " + event.getX() +
//						   ", " + event.getY());
		
	}
	@Override
	public void mouseEntered(MouseEvent event) {
//		System.out.println("mouse entered");
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseExited(MouseEvent event) {
//		System.out.println("mouse exited");
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mousePressed(MouseEvent event) {
		this.drawMouseBox = true;
		System.out.println("mouse pressed");
		startClickX = event.getX();
		startClickY = event.getY();
		endClickX = startClickX;
		endClickY = startClickY;
		this.printClickArea();
	}
	@Override
	public void mouseReleased(MouseEvent event) {
		this.drawMouseBox = false;
		System.out.println("mouse released");
		this.printClickArea();
		
	}
	@Override
	public void mouseDragged(MouseEvent event) {
//		System.out.println("mouse dragged");
		endClickX = event.getX();
		endClickY = event.getY();
		
		getDragArea();
		
//		this.drawMouseBox = true;
		
	}
	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
      
}