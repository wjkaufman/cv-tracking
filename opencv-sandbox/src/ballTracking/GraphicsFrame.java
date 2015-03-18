package ballTracking;

import java.awt.Color;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import org.opencv.core.Mat;

public class GraphicsFrame extends JFrame implements ActionListener{
	public static final int WIDTH = 600, HEIGHT = 400;
	public static final double CAPTURE_SCALE = 1;
	public static int FRAME = 0;
	
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();
	
	private GraphicsPanel panel;

	public GraphicsFrame() throws HeadlessException {
		setup();
	}

	public GraphicsFrame(GraphicsConfiguration gc) {
		super(gc);
		setup();
	}

	public GraphicsFrame(String title) throws HeadlessException {
		super(title);
		setup();
	}

	public GraphicsFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
		setup();
	}
	
	public void setup() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.BLACK);
		this.setSize(WIDTH, HEIGHT);
		
		panel = new GraphicsPanel();
		panel.addActionListener(this);
		
		this.add(panel);
		
		this.setVisible(true);
	}
	
	public boolean updateImage(Mat image) {
		return panel.updateImage(image);
	}
	
	public GraphicsPanel getPanel() {
		return panel;
	}
	
	public void addActionListener(ActionListener al) {
		listeners.add(al);
	}
	
	public void removeActionListener(ActionListener al) {
		listeners.remove(al);
	}
	
	public void fireActionPerformed(ActionEvent ae) {
		for(ActionListener al : listeners) {
			al.actionPerformed(ae);
		}
	}
	
	public void fireActionPerformed() {
		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED,
							panel.getAverageHSVAsString()));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		fireActionPerformed(e);
	}
}
