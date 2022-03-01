package e6_Psychedelia;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class E6_Psychedelia {
	public static void main(String args[]) {
		
		Thread[] labels = new Thread[5];
		
		JFrame frame = new JFrame("My First GUI");
		//frame.setLayout(new FlowLayout());
		frame.setLayout(new GridLayout(2, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(450, 450);
	
		for (int i = 0; i < labels.length; i++) {
			PsychedelicLabel label = new PsychedelicLabel();
			labels[i] = new Thread(label);
			frame.add(label);
			labels[i].start();
		}
		
		JSlider slider = new JSlider();
		slider.addChangeListener(new SliderListener());
		frame.add(slider);
		JButton btnGO = new JButton("GO");
		btnGO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("GO");
			}
		});
		frame.add(btnGO);
		JButton btnSUSPEND = new JButton("SUSPEND");
		btnSUSPEND.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("SUSPEND");
			}
		});
		frame.add(btnSUSPEND);
		JButton btnRESUME = new JButton("RESUME");
		btnRESUME.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("RESUME");
			}
		});
		frame.add(btnRESUME);
		JButton btnSTOP = new JButton("STOP");
		btnSTOP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("STOP");
			}
		});
		frame.add(btnSTOP);
		frame.setVisible(true);
	}
}

class SliderListener implements ChangeListener {
	
	
	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			int value = (int) source.getValue();
			System.out.println("New Slider Value: " + value);
		}
	}
}