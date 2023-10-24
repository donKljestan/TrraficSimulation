package org.unibl.etf.pj2.display;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.pj2.composition.*;


public class InputLocomotive extends JFrame {

	private JPanel contentPane;
	private JTextField power;
	private JTextField mark;
	private Locomotive locomotive = new Locomotive();
	private static int k;
	
	
	public InputLocomotive(int k) {
		this.k = k;
		
		setTitle("Locomotive configuration");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(String.valueOf(InputComposition.getNumberOfLocomotives() -
				InputLocomotive.k + 1) + ". locomotive");
		lblNewLabel.setBounds(10, 11, 91, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Mark: ");
		lblNewLabel_1.setBounds(10, 36, 51, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Type:");
		lblNewLabel_2.setBounds(10, 60, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Engine:");
		lblNewLabel_3.setBounds(10, 85, 46, 14);
		contentPane.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("Power[kW]: ");
		lblNewLabel_4.setBounds(10, 113, 70, 14);
		contentPane.add(lblNewLabel_4);
		
		power = new JTextField();
		power.setBounds(82, 110, 86, 20);
		contentPane.add(power);
		power.setColumns(10);
		
		mark = new JTextField();
		mark.setBounds(82, 33, 86, 20);
		contentPane.add(mark);
		mark.setColumns(10);
		
		JComboBox comboBoxTip = new JComboBox();
		comboBoxTip.addItem("Passenger");
		comboBoxTip.addItem("Freight");
		comboBoxTip.addItem("Universal");
		comboBoxTip.addItem("Shunting");
		comboBoxTip.setBounds(82, 56, 86, 22);
		contentPane.add(comboBoxTip);
		
		JComboBox comboBoxPogon = new JComboBox();
		comboBoxPogon.addItem("Diesel");
		comboBoxPogon.addItem("Steam");
		comboBoxPogon.addItem("Electric");
		comboBoxPogon.setBounds(82, 83, 86, 22);
		contentPane.add(comboBoxPogon);
		
		JButton btnOkButton = new JButton("OK");
		btnOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(comboBoxTip.getSelectedItem().equals("Passenger")) {
					locomotive.setType(LocomotiveType.PASSENGER);
				} else if(comboBoxTip.getSelectedItem().equals("Freight")) {
					locomotive.setType(LocomotiveType.FREIGHT);
				} else if(comboBoxTip.getSelectedItem().equals("Universal")) {
					locomotive.setType(LocomotiveType.UNIVERSAL);
				} else if(comboBoxTip.getSelectedItem().equals("Shunting")) {
					locomotive.setType(LocomotiveType.SHUNTING);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Locomotive type is not selected.");
					return;
				}
				
				if(comboBoxPogon.getSelectedItem().equals("Diesel")) {
					locomotive.setEngine(LocomotiveEngine.DIESEL);
				} else if(comboBoxPogon.getSelectedItem().equals("Steam")) {
					locomotive.setEngine(LocomotiveEngine.STEAM);
				} else if(comboBoxPogon.getSelectedItem().equals("Electric")) {
					locomotive.setEngine(LocomotiveEngine.ELECTRIC);
				} else {
					JOptionPane.showMessageDialog(contentPane, "Locomotive engine is not selected.");
					return;
				}
				
				
				if(mark.getText().length() == 0 || (mark.getText()).length() > 100) {
					JOptionPane.showMessageDialog(contentPane, "Wrong locomotive mark.");
					return;
				} else {
					locomotive.setMark(mark.getText());
				}
				
				if(power.getText().length() == 0 ||
						(Integer.valueOf(power.getText()) <= 0 || Integer.valueOf(power.getText()) > 10000)) {
					JOptionPane.showMessageDialog(contentPane, "Wrong locomotive power input.");
					return;
				} else {
					locomotive.setPower(Integer.valueOf(power.getText()));
				}
				
				InputComposition.addLocomotive(locomotive);
				if(InputComposition.getComposition().configurationValidity() == false) {
					InputComposition.removeLocomotive();
					JOptionPane.showMessageDialog(contentPane, "Configuration is invalid."
							+ "\nCheck what type of locomotive can be assembled. ");
					return;
				}
				
				if(InputLocomotive.k-- > 1) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							InputLocomotive fr = new InputLocomotive(InputLocomotive.k);
							fr.setVisible(true);
						} catch (Exception e) {
							Logger.getLogger(InputLocomotive.class.getName()).log(Level.SEVERE,e.toString());
						}
					}
				});
				} else if(InputComposition.getNumberOfWagons() > 0) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							try {
								InputWagon fr = new InputWagon(InputComposition.getNumberOfWagons());
								fr.setVisible(true);
							} catch (Exception e) {
								Logger.getLogger(InputLocomotive.class.getName()).log(Level.SEVERE,e.toString());
							}
						}
					}); 
				} else if(InputComposition.getNumberOfWagons() == 0) {
					InputComposition.writeInFile();
				}
				
				setVisible(false);
				dispose();
			}
		});
		btnOkButton.setBounds(335, 227, 89, 23);
		contentPane.add(btnOkButton);
	}
}
