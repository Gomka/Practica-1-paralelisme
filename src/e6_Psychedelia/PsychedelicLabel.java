package e6_Psychedelia;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class PsychedelicLabel extends JLabel implements Runnable {

	private int speed = 50;
	private Random alea;
	private Color color;
	
	public PsychedelicLabel () {
		this.setOpaque(true);
		this.alea = new Random();
		this.setText("PsyLabel");
	}
	
	public void setSpeed (int speed) {this.speed=speed;}
	
	
	public void run () {
		
	}
}

