package ballTracking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.opencv.core.Rect;

public class Obj {
	
	private int[] position = new int[4]; //0: x, 1: y, 2: width, 3: height
	private ArrayList<int[]> movement = new ArrayList<int[]>();
	
	private Color color;
	private boolean drawObj = true;
	
	public Obj() {
		position[0] = 0;
		position[1] = 0;
		position[2] = 0;
		position[3] = 0;
		
		color = new Color(0, 255, 0);
	}
	
	public Obj(int x, int y, int width, int height) {
		position[0] = x;
		position[1] = y;
		position[2] = width;
		position[3] = height;
	}
	
	public Obj(Rect rect) {
		position[0] = rect.x;
		position[1] = rect.y;
		position[2] = rect.width;
		position[3] = rect.height;
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
	
	public int[] getPosition() {
		return position;
	}
	
	public void setX(int x) {
		position[0] = x;
	}
	
	public void setY(int y) {
		position[1] = y;
	}
	
	public void setWidth(int width) {
		position[2] = width;
	}
	
	public void setHeight(int height) {
		position[3] = height;
	}
	
	public void setPosition(int x, int y, int width, int height) {
		position[0] = x;
		position[1] = y;
		position[2] = width;
		position[3] = height;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color newColor) {
		color = newColor;
	}
	
	public void drawObj(boolean b) {
		drawObj = b;
	}
	
	public boolean drawObj() {
		return drawObj;
	}
	
	public void draw(Graphics g) {
		if (drawObj) {
			g.setColor(color);
			g.drawRect(getX(), getY(), getWidth(), getHeight());
			
			int dotsBack = 5;
			int dotSize = 2;
			
			if (dotsBack > movement.size()) dotsBack = movement.size();
			
			if (movement.size() > 0) {
				for (int i = movement.size() - 1; i > movement.size() - dotsBack; i--) {
					int[] prevPos = movement.get(i);
					double[] middle = getMiddle(prevPos);
					
					g.drawRect((int)middle[0], (int)middle[1], dotSize, dotSize);
				}
			}
		}
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
	
	/**
	 * adds current position to movement history
	 */
	
	public void addPosition() {
		movement.add(position);
	}
	
	public void addPosition(int x, int y, int width, int height) {
		setPosition(x, y, width, height);
		addPosition();
	}
	
	public void addPosition(Obj obj2) {
		addPosition(obj2.getX(), obj2.getY(), obj2.getWidth(), obj2.getHeight());
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
	
	public int area() {
		return position[0] * position[1];
	}
	
	public double[] getMiddle(int[] position) {
		double[] middle = new double[2];
		
		middle[0] = (position[0] + position[2]) / 2;
		middle[1] = (position[1] + position[3]) / 2;
		
		return middle;
	}
	
	public double[] getMiddle() {
		double[] middle = new double[2];
		middle[0] = (getX() + getWidth()) / 2;
		middle[1] = (getY() + getHeight()) / 2;
		return middle;
	}
	
	public double getDistance(Obj obj) {
		double distance = Math.sqrt(Math.pow(this.getMiddle()[0] + obj.getMiddle()[0], 2) + 
									Math.pow(this.getMiddle()[1] + obj.getMiddle()[1], 2));
		return distance;
	}
	
	public Obj getClosestObj(List<Obj> objects) {
		Obj closestObj;
		if (objects.size() > 0) {
			closestObj = objects.get(0);
			for (Obj nextObj : objects) {
				if (nextObj.getDistance(this) < closestObj.getDistance(this)) closestObj = nextObj;
			}
		}
		else closestObj = new Obj();
		
		
		return closestObj;
	}
	
	public String toString() {
		String string = "object: (" + getX() + ", " + getY() + "), width: " + getWidth() + ", height: " + getHeight();
		return string;
	}
	
	public String getData() {
		String string = "=====object data=====\n";
		
		string += this;
		
		return string;
	}
	
	public void printData() {
		System.out.println(getData());
	}
	
}
