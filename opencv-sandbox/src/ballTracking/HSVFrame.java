package ballTracking;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class HSVFrame extends JFrame implements ActionListener, ChangeListener {
	
	JPanel mainPanel;
	
	public static final int HSV_MIN = 0;
	public static final int HSV_MAX = 1;
	
	private int HSVBound;
	
	private boolean reactStateChanged = true;
	
	private float h;
	private float s;
	private float v;
	
	private ArrayList<ActionListener> listeners = new ArrayList<ActionListener>();

	private JSlider h_slider;
	private JSlider s_slider;
	private JSlider v_slider;
	
	private JTextField h_field;
	private JTextField s_field;
	private JTextField v_field;
	
	
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
		
		h_field = new JTextField("" + h, 2);
		s_field = new JTextField("" + s, 2);
		v_field = new JTextField("" + v, 2);
		
		h_slider.addChangeListener(this);
		s_slider.addChangeListener(this);
		v_slider.addChangeListener(this);
		
		h_field.addActionListener(this);
		s_field.addActionListener(this);
		v_field.addActionListener(this);
		
		this.setSize(130,250);
		
		mainPanel.setLayout(new FlowLayout());
		
		mainPanel.add(h_slider);
		mainPanel.add(s_slider);
		mainPanel.add(v_slider);
		
		mainPanel.add(h_field);
		mainPanel.add(s_field);
		mainPanel.add(v_field);
		
		
		this.add(mainPanel);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setBackground(Color.WHITE);
		this.setVisible(true);
	}
	
	public float getH() { return h; }
	public float getS() { return s; }
	public float getV() { return v; }
	
	public boolean isHSVMin() {
		if (HSVBound == HSV_MIN) return true;
		else return false;
	}
	
	public boolean isHSVMax() {
		if (HSVBound == HSV_MAX) return true;
		else return false;
	}
	
	public float[] getHSV() {
		float[] vals = {h, s, v};
		return vals;
	}
	
	public String getHSVAsString() {
		String string = "" + getH() + ", " + getS() + ", " + getV();
		return string;
	}
	
	public void setHSV(float[] hsv) {
		System.out.println("\n\nsetHSV array: " + Arrays.toString(hsv));
		h = hsv[0];
		s = hsv[1];
		v = hsv[2];
		
		updateFrame();
	}
	
	public void updateFrame() {
		System.out.println("\n" + h + " " + s + " " + v);
		reactStateChanged = false;
		h_slider.setValue((int)h);
		s_slider.setValue((int)s);
		v_slider.setValue((int)v);
		
		h_field.setText("" + (int)h);
		s_field.setText("" + (int)s);
		v_field.setText("" + (int)v);
		
		reactStateChanged = true;
		
		fireActionPerformed();
	}
	
	public void stateChanged(ChangeEvent e) {
//		System.out.println("state changed in HSVFrame");
		//the problem is here!!!
		if (reactStateChanged) {
			h = h_slider.getValue();
			s = s_slider.getValue();
			v = v_slider.getValue();
			
			updateFrame();
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JTextField) {
			try {
				h = Integer.parseInt(h_field.getText());
				s = Integer.parseInt(s_field.getText());
				v = Integer.parseInt(v_field.getText());
			}
			catch (NumberFormatException ex) {
				ex.printStackTrace();
			}
			
			updateFrame();
		}
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
