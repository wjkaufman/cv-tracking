package colorTracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSVFrame extends JFrame implements ActionListener, ChangeListener {
	
	JPanel mainPanel;
	
	public static final int HSV_MIN = 0;
	public static final int HSV_MAX = 1;
	
	private int HSVBound;
	
	private int h;
	private int s;
	private int v;
	
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	private JSlider h_slider;
	private JSlider s_slider;
	private JSlider v_slider;
	
	public HSVFrame() {
		this("HSVFrame");
	}
	
	public HSVFrame(String title) {
		this(-1, title, 0,0,0);
	}
	
	public HSVFrame(int HSVBound, String title, int h, int s, int v) {
		super(title);
		
		this.HSVBound = HSVBound;
		
		mainPanel = new JPanel();
		
		h_slider = new JSlider(SwingConstants.VERTICAL, 0, 360, h);
		s_slider = new JSlider(SwingConstants.VERTICAL, 0, 255, s);
		v_slider = new JSlider(SwingConstants.VERTICAL, 0, 255, v);
		
		h_slider.addChangeListener(this);
		s_slider.addChangeListener(this);
		v_slider.addChangeListener(this);
		
		
		this.setSize(100,250);
		
		mainPanel.setLayout(new FlowLayout());
		
		mainPanel.add(h_slider);
		mainPanel.add(s_slider);
		mainPanel.add(v_slider);
		
		this.add(mainPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		this.setVisible(true);
	}
	
	public int getH() { return h; }
	public int getS() { return s; }
	public int getV() { return v; }
	
	public boolean isHSVMin() {
		if (HSVBound == HSV_MIN) return true;
		else return false;
	}
	
	public boolean isHSVMax() {
		if (HSVBound == HSV_MAX) return true;
		else return false;
	}
	
	public int[] getHSV() {
		int[] vals = {h, s, v};
		return vals;
	}
	
	public String getHSVAsString() {
		String string = "" + getH() + ", " + getS() + ", " + getV();
		return string;
	}
	
	public void stateChanged(ChangeEvent e) {
		//System.out.println("state changed");
		h = h_slider.getValue();
		s = s_slider.getValue();
		v = v_slider.getValue();
		fireActionPerformed();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("action performed");
		
	}
	
	/**
     * adds actionlistener to subscribd listener list
     * @param al - actionlistener to add
     */
	public void addActionListener( ActionListener al) {
		listeners.add(al);
	}
	
	
	/**
     * removes actionlistener from subscribed listener list
     * @param al - actionlistener to remove
     */
	public void removeActionListener(ActionListener al) {
		listeners.remove(al);
	}
	
	/**
     * sends an actionevent to subscribed listeners
     * @param ae - actionevent to use as parameter to all subscribed listeners
     */
	public void fireActionPerformed(ActionEvent ae) {
		for(ActionListener al : listeners) {
			al.actionPerformed(ae);
		}
	}
	
	public void fireActionPerformed() {
		fireActionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, getHSVAsString()));
	}
}
