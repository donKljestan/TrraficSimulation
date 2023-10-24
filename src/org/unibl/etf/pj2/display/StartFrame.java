package org.unibl.etf.pj2.display;


import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.awt.GridLayout;

public class StartFrame extends JFrame {

	private JPanel contentPane;

	
	


	public StartFrame() {
		setTitle("Traffic simulation");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 343, 240);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JToggleButton addComposition = new JToggleButton("Add train");
		addComposition.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							InputComposition fr = new InputComposition();
							fr.setVisible(true);
						} catch (Exception e) {
							Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE,e.toString());
						}
					}
				});
			}
		});
		
		JToggleButton startSimulation = new JToggleButton("Start simulation");
		startSimulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Display d = new Display();
				d.start();
				
			}
		});
		
		JToggleButton movements = new JToggleButton("Movements");
		movements.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							Movements fr = new Movements();
							fr.setVisible(true);
						} catch (Exception e) {
							Logger.getLogger(StartFrame.class.getName()).log(Level.SEVERE,e.toString());
						}
					}
				});
			}
		});
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(addComposition);
		contentPane.add(startSimulation);
		contentPane.add(movements);
		
	
		
		
		
	}
}
