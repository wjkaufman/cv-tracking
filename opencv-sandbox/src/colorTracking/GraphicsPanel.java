package colorTracking;
/*  
 * Captures the camera stream with OpenCV
 * Search for the faces
 * Display a circle around the faces using Java
 */
import java.awt.*;

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

class GraphicsPanel extends JPanel{
     private static final long serialVersionUID = 1L;
     private BufferedImage image;
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
     
     public boolean updateImage(Mat matrix) {
    	 if (matToBufferedImage(matrix)) {
    		 this.repaint();
    		 return true;
    	 }
    	 else return false;
     }
     
     public void paintComponent(Graphics g){
          super.paintComponent(g);
          if (this.image==null) return;
        
          g.drawImage(this.image, 0, 0, GraphicsFrame.WIDTH, GraphicsFrame.HEIGHT, null);
          g.setColor(Color.WHITE);
          g.setFont(new Font("", 0, 20));
          g.drawString("Frame: " + GraphicsFrame.FRAME, 50, 50);
     }
      
}  