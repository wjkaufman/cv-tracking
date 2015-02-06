import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

public class VideoStream {
    	
    public static void main (String args[]){

    System.out.println("Hello, OpenCV");
    // Load the native library.
    System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

    VideoCapture camera = new VideoCapture(0);
    
    try{
    	Thread.sleep(1000);
    }
    catch(Exception e){
    	e.printStackTrace();
    }
    
    if(!camera.isOpened()){
        System.out.println("Camera Error");
    }
    else{
        System.out.println("Camera OK");
	    Mat frame = new Mat();
	
	    //camera.grab();
	    //System.out.println("Frame Grabbed");
	    //camera.retrieve(frame);
	    //System.out.println("Frame Decoded");
	
	    for (int i = 0; i < 100; i++) {
		    while (true) {
		    	camera.set(3, 1000);
		    	for (int j = 0; j < 38; j++) {
	    			System.out.println("" + j + ": " + camera.get(j)); //***WORK ON IT HERE
	    		}
		    	if (camera.read(frame)) {
		    		System.out.println("Frame Obtained");
		    		System.out.println("Captured Frame Width " + 
		    		frame.width() + " Height " + frame.height());
		    		break;
		    	}
		    }
		    Highgui.imwrite("camera" + i + ".jpg", frame);
	    }
	    
	    System.out.println("OK");
    }
    camera.release();
    }
}