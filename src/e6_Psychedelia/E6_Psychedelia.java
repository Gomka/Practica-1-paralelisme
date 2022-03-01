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
		
		final Synchronizer syncronizer = new Synchronizer();
		final Thread[] labels = new Thread[5];
		
		JFrame frame = new JFrame("My First GUI");
		//frame.setLayout(new FlowLayout());
		frame.setLayout(new GridLayout(2, 0));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(450, 450);
	
		for (int i = 0; i < labels.length; i++) {
			PsychedelicLabel label = new PsychedelicLabel(syncronizer);
			labels[i] = new Thread(label);
			frame.add(label);
		}

		JSlider slider = new JSlider();		
		final JButton btnGO = new JButton("GO");
		final JButton btnSUSPEND = new JButton("SUSPEND");
		final JButton btnRESUME = new JButton("RESUME");
		final JButton btnSTOP = new JButton("STOP");

		btnSUSPEND.setEnabled(false);
		btnRESUME.setEnabled(false);
		btnSTOP.setEnabled(false);

		slider.addChangeListener(new SliderListener(syncronizer));
		frame.add(slider);

		btnGO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnGO.setEnabled(false);
				System.out.println("GO");
				for (int i = 0; i < labels.length; i++) {
					labels[i].start();
				}
				btnSUSPEND.setEnabled(true);
				btnSTOP.setEnabled(true);
			}
		});
		frame.add(btnGO);

		btnSUSPEND.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSUSPEND.setEnabled(false);
				btnRESUME.setEnabled(true);
				System.out.println("SUSPEND");
				syncronizer.isSuspended = true;
			}
		});
		frame.add(btnSUSPEND);

		btnRESUME.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSUSPEND.setEnabled(true);
				btnRESUME.setEnabled(false);
				System.out.println("RESUME");
				syncronizer.isSuspended = false;
			}
		});
		frame.add(btnRESUME);

		btnSTOP.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnSUSPEND.setEnabled(false);
				btnRESUME.setEnabled(false);
				System.out.println("STOP");
				for (int i = 0; i < labels.length; i++) {
					labels[i].stop();
				}
				btnSTOP.setEnabled(false);
			}
		});
		frame.add(btnSTOP);
		frame.setVisible(true);
	}
}

class SliderListener implements ChangeListener {
	
	Synchronizer syncronizer;

	public SliderListener(Synchronizer syncronizer) {
		this.syncronizer = syncronizer;
	}

	public void stateChanged(ChangeEvent e) {
		JSlider source = (JSlider) e.getSource();
		if (!source.getValueIsAdjusting()) {
			int value = (int) source.getValue();
			System.out.println("New Slider Value: " + value);
			syncronizer.speed = value;
		}
	}
}

class Synchronizer {
	// No se ha realizado ning�n tipo de protecci�n porqu� los "PsychedelicLabel" solo leen y los botones solo escriben
	protected boolean isSuspended = false;
	public int speed = 50;
}