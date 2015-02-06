import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class FaceDetector {  
     private CascadeClassifier face_cascade;  
     // Create a constructor method  
     public FaceDetector(){  
         // face_cascade=new CascadeClassifier("./cascades/lbpcascade_frontalface_alt.xml");  
         //..didn't have not much luck with the lbp
         
        face_cascade=new CascadeClassifier("./cascades/haarcascade_frontalface_alt.xml"); 
          if(face_cascade.empty())  
          {  
               System.out.println("--(!)Error loading A\n");
                return;
          }  
          else  
          {  
                     System.out.println("Face classifier loooaaaaaded up");
          }  
     }  
     
     public Mat detect(Mat inputframe){  
          Mat mRgba=new Mat();
          Mat mGrey=new Mat();
          MatOfRect faces = new MatOfRect();
          inputframe.copyTo(mRgba);
          inputframe.copyTo(mGrey);
          Imgproc.cvtColor( mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);
          Imgproc.equalizeHist( mGrey, mGrey );  
          face_cascade.detectMultiScale(mGrey, faces);
          System.out.println(String.format("Detected %s faces", faces.toArray().length));  
          for(Rect rect:faces.toArray())  
          {  
               Point center= new Point(rect.x + rect.width*0.5, rect.y + rect.height*0.5 );  
               //draw a blue eclipse around face
               Size s = new Size( rect.width*0.5, rect.height*0.5); //, 0, 0, 360, new Scalar( 0, 0, 255 )
               Scalar scale = new Scalar(57, 255, 20);
               Core.ellipse( mRgba, center,s , 4, 0, 360, scale );//Color(57,255,20));
          }  
          return mRgba;
     }  
}  