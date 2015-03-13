package ballTracking;

public class Ball {
	
	private int x, y, width, height;
	
	public Ball() {
		
	}
	
	public Ball(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public void setLocation(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public boolean contains(int x, int y) {
		boolean val = (x > this.x && x < this.x + this.width)
					   && (y > this.y && y < this.y + this.height);
		
		return val;
	}
	
	public boolean overlap(Ball ball2) {
		boolean b = false;
		b = b || this.contains(ball2.getX(), ball2.getY());
		b = b || this.contains(ball2.getX() + ball2.getWidth(), ball2.getY());
		b = b || this.contains(ball2.getX(), ball2.getY() + ball2.getHeight());
		b = b || this.contains(ball2.getX() + ball2.getWidth(), ball2.getY() + ball2.getHeight());
		
		return b;
	}
	
	
	
	

}
