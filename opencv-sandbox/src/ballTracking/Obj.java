package ballTracking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.opencv.core.Rect;

public class Obj {
	
	private int[] position = new int[4]; //0: x, 1: y, 2: width, 3: height
	private ArrayList<int[]> movements = new ArrayList<int[]>();
	private int movementHistory = 25;
	
	private int samplesForMovement = 3;
	
	private boolean debug = true;
	private boolean wasMovingUp = false;
	
	private double maxAngle; //implement things here, in movingUp and movingDown
	
	private List<Double> maxAngles = new ArrayList<Double>();
	
	private Color color;
	private boolean drawObj = true;
	
	private int number;
	private String objName;
	
	public Obj() {
		setX(0);
		setY(0);
		setWidth(0);
		setHeight(0);
		
		color = new Color(0, 255, 0);
	}
	
	public Obj(int x, int y, int width, int height) {
		setX(x);
		setY(y);
		setWidth(width);
		setHeight(height);
	}
	
	public Obj(Rect rect) {
		setX(rect.x);
		setY(rect.y);
		setWidth(rect.width);
		setHeight(rect.height);
	}
	
	public Obj(Obj obj2) {
		obj2.copyTo(this);
	}
	
	public void copyTo(Obj obj2) {
		obj2.setX(getX());
		obj2.setY(getY());
		obj2.setWidth(getWidth());
		obj2.setHeight(getHeight());
		
	}
	
	public int getMovementHistory() {
		return movementHistory;
	}
	
	public void setMovementHistory(int mh) {
		movementHistory = mh;
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
	
	public void setNumber(int n) {
		number = n;
	}
	
	public int getNumber() {
		return number;
	}
	
	public void setName(String string) {
		objName = string;
	}
	
	public String getName() {
		return objName;
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
			
			g.drawString("(" + number + ") " + objName, getX(), getY());
			
			int dotsBack = 10;
			int dotSize = 2;
			
			if (dotsBack > movements.size()) dotsBack = movements.size();
			
			if (movements.size() > 0) {
				for (int i = movements.size() - 1; i > movements.size() - dotsBack; i--) {
					int[] prevPos = movements.get(i);
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
	 * checks if the object is moving up (y motion) over sample number of frames
	 * @param sample number of previous positions to check if it's moving up
	 * @return true if moving up, false if not moving up
	 */
	public boolean movingUp(int sample) {
		if (!hasHistory()) return false;
		
		if (sample > movements.size()) sample = movements.size();
		int startIndex = movements.size() - 1, endIndex = startIndex - sample + 1;
		
		boolean movingUp = movements.get(startIndex)[1] <
						   movements.get(endIndex)[1];
		
		return movingUp;
	}
	/**
	 * checks if the object is moving down (y motion) over sample number of frames
	 * @param sample number of previous positions to check
	 * @return true if moving up, false if not moving up
	 */
	public boolean movingDown(int sample) {
		if (!hasHistory()) return false;
		
		if (sample > movements.size()) sample = movements.size();
		int startIndex = movements.size() - 1, endIndex = startIndex - sample + 1;
		
		
		boolean movingUp = movements.get(startIndex)[1] >
						   movements.get(endIndex)[1];
		
		return movingUp;
	}
	
	/**
	 * adds current position to movement history
	 */
	public void addPosition() {
		int[] newPosition = Arrays.copyOf(position, position.length);
		movements.add(newPosition);
		if (movements.size() > movementHistory) movements.remove(0);
	}
	
	public void addPosition(int x, int y, int width, int height) {
		setPosition(x, y, width, height);
		addPosition();
	}
	
	public void addPosition(Obj obj2) {
		addPosition(obj2.getX(), obj2.getY(), obj2.getWidth(), obj2.getHeight());
	}
	
	public void transitionTo(Obj obj2, int weight) {
		int x = (getX() + obj2.getX() * weight) / (weight + 1),
			y = (getY() + obj2.getY() * weight) / (weight + 1),
			width = (getWidth() + obj2.getWidth() * weight) / (weight + 1),
			height = (getHeight() + obj2.getHeight() * weight) / (weight + 1);
		
		addPosition(x, y, width, height);
	}
	
	public void transitionTo(Obj obj2) {
		transitionTo(obj2, 1);
	}
	
	public boolean hasHistory() {
		return movements.size() > 0;
	}
	
	/**
	 * determines the angle of trajectory of the object from the
	 * linear regression of the past samples
	 * @param sample number of previous positions that will be sampled to determine angle
	 * @return angle of trajectory of object
	 */
	
	public double angle(int startSample, int endSample) {
		if (!hasHistory()) return -1;
		SimpleRegression regression = new SimpleRegression();
		
		if (startSample > movements.size() - 1) startSample = movements.size();
		if (startSample < 0) startSample = 0;
		if (endSample > movements.size() - 1) endSample = movements.size();
		if (endSample < startSample) endSample = startSample;
		
		for (int i = startSample; i < endSample; i++) {
			int[] nextPos = movements.get(i);
			regression.addData(nextPos[0], nextPos[1]);
		}
		
		double slope = regression.getSlope();
		double theta = Math.atan(slope);
		double angle = Math.toDegrees(theta);
		
//		if (debug) {
//			System.out.println("in angle, Obj");
//			System.out.println("slope: " + slope);
//			System.out.println("theta: " + theta);
//			System.out.println("angle: " + angle);
//		}
		
		return Math.abs(angle);
	}
	
	public double angle(int sample) {
		return angle(movements.size() - sample, movements.size() - 1);
	}
	
	/**
	 * checks the motion of the object. If it's moving up, it updates the maxAngle
	 * if it's moving down, it adds the maxAngle to the list of maxAngles, resets the maxAngle
	 */
	public void checkAngle() { //***WORK HERE, booleans are weird
		if (movingUp(samplesForMovement) && wasMovingUp) { //if it's continuing to move up
			double angle = angle(5);
			if (angle > maxAngle) maxAngle = angle;
		}
		else if (movingDown(samplesForMovement) && wasMovingUp){ //it's beginning to move down
			if (debug) {
				System.out.println("changing trajectory");
				System.out.println(maxAngle);
			}
			
			maxAngles.add(maxAngle);
			maxAngle = -1;
		}
		wasMovingUp = movingUp(samplesForMovement);
	}
	
	public int area() {
		return position[0] * position[1];
	}
	
	public double[] getMiddle(int[] position) {
		double[] middle = new double[2];
		
		middle[0] = position[0] + (position[2] / 2);
		middle[1] = position[1] + (position[3] / 2);
		
		return middle;
	}
	
	public double[] getMiddle() {
		double[] middle = new double[2];
		middle[0] = getX() + (getWidth() / 2);
		middle[1] = getY() + (getHeight() / 2);
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
		else closestObj = new Obj(this);
		
		
		return closestObj;
	}
	
	public String toString() {
		String string = "object: (" + getX() + ", " + getY() + "), width: " +
								  getWidth() + ", height: " + getHeight();
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
