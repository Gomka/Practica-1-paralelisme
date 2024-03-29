package e6_Psychedelia;

import javax.swing.*;
import java.util.*;
import java.awt.*;

public class PsychedelicLabel extends JLabel implements Runnable {

	private int speed = 50;
	private Random alea;
	Synchronizer syncronizer;
	
	public PsychedelicLabel (Synchronizer syncronizer) {
		this.syncronizer = syncronizer;
		this.setOpaque(true);
		this.alea = new Random();
		this.setText("PsyLabel");
		this.setBackground(GetRandomColor());
		this.setSize(200, 150);
	}
	
	public void setSpeed (int speed) {this.speed=speed;}
	
	
	public void run () {
		while (true) {
			this.setBackground(GetRandomColor());
			try { Thread.sleep(100-syncronizer.speed); } catch (InterruptedException e) { }
			while (syncronizer.isSuspended) {Thread.yield();}
		}
	}
	
	private Color GetRandomColor() {
		float r = alea.nextFloat();
		float g = alea.nextFloat();
		float b = alea.nextFloat();
		Color randomColor = new Color(r, g, b);
		return randomColor;
	}
}

