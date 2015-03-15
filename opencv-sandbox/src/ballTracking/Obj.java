package ballTracking;

import java.awt.List;
import java.util.ArrayList;
import org.apache.commons.math3.stat.regression.SimpleRegression;

public class Obj {
	
	private int[] position; //0: x, 1: y, 2: width, 3: height
	private ArrayList<int[]> movement = new ArrayList<int[]>();
	
	public Obj() {
		
	}
	
	public Obj(int x, int y, int width, int height) {
		position[0] = x;
		position[1] = y;
		position[2] = width;
		position[3] = height;
	}
	
	public int getX() {
		return position[0];
	}
	
	public int getY() {
		return position[1];
	}
	
	public int getWidth() {
		return position[2];
	}
	
	public int getHeight() {
		return position[3];
	}
	
	public void setLocation(int x, int y, int width, int height) {
		position[0] = x;
		position[1] = y;
		position[2] = width;
		position[3] = height;
	}
	
	public boolean contains(int x, int y) {
		boolean val = (x > position[0] && x < position[0] + position[2])
					   && (y > position[1] && y < position[1] + position[3]);
		
		return val;
	}
	
	public boolean overlap(Obj obj2) {
		boolean b = false;
		b = b || this.contains(obj2.getX(), obj2.getY());
		b = b || this.contains(obj2.getX() + obj2.getWidth(), obj2.getY());
		b = b || this.contains(obj2.getX(), obj2.getY() + obj2.getHeight());
		b = b || this.contains(obj2.getX() + obj2.getWidth(), obj2.getY() + obj2.getHeight());
		
		return b;
	}
	
	public void addLocation() {
		movement.add(position);
	}
	
	public double angle(int sample) {
		SimpleRegression regression = new SimpleRegression();
		for (int i = movement.size(); i > movement.size() - sample; i--) {
			int[] nextPos = movement.get(i);
			regression.addData(nextPos[0], nextPos[1]);
		}
		
		double slope = regression.getSlope();
		double theta = Math.atan(slope);
		double angle = Math.toDegrees(theta);
		return angle;
	}
	
}
