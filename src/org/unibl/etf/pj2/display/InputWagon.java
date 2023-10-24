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


public class InputWagon extends JFrame{

	private JPanel contentPane;
	private JTextField length;
	private JTextField mark;
	private JTextField tfMaxCapacity;
	private JTextField tfNumOfPassengerSeats;
	private JTextField tfDescrRestaurant;
	private Wagon wagon;
	private static int k;
	

	public InputWagon(int k) {
		this.k = k;
		
		setTitle("Wagon configuration");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel(String.valueOf(
				InputComposition.getNumberOfWagons() - InputWagon.k + 1) + ". wagon:");
		lblNewLabel.setBounds(10, 11, 91, 14);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Mark: ");
		lblNewLabel_1.setBounds(10, 36, 51, 14);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Type:");
		lblNewLabel_2.setBounds(10, 60, 46, 14);
		contentPane.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Length[m]:");
		lblNewLabel_3.setBounds(10, 85, 73, 14);
		contentPane.add(lblNewLabel_3);
		
		length = new JTextField();
		length.setBounds(244, 82, 91, 20);
		contentPane.add(length);
		length.setColumns(10);
		
		mark = new JTextField();
		mark.setBounds(244, 33, 91, 20);
		contentPane.add(mark);
		mark.setColumns(10);
		
		JComboBox comboBoxTip = new JComboBox();
		comboBoxTip.addItem(" ");
		comboBoxTip.addItem("Passenger");
		comboBoxTip.addItem("Freight");
		comboBoxTip.addItem("Shunting");
		comboBoxTip.setBounds(244, 56, 91, 22);
		contentPane.add(comboBoxTip);
		
		JLabel lblNewLabel_4 = new JLabel("Type of passenger wagon:");
		lblNewLabel_4.setBounds(10, 110, 174, 14);
		contentPane.add(lblNewLabel_4);
		
		JComboBox comboBoxTipPV = new JComboBox();
		comboBoxTipPV.setEnabled(false);
		comboBoxTipPV.addItem(" ");
		comboBoxTipPV.addItem("With seats");
		comboBoxTipPV.addItem("With bearings");
		comboBoxTipPV.addItem("For sleeping");
		comboBoxTipPV.addItem("Restaurant");
		comboBoxTipPV.setBounds(244, 106, 91, 22);
		contentPane.add(comboBoxTipPV);
		comboBoxTip.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxTip.getSelectedItem().equals("Passenger")) {
					comboBoxTipPV.setEnabled(true);
				} else {
					comboBoxTipPV.setEnabled(false);
				}
				
				if(comboBoxTip.getSelectedItem().equals("Freight")) {
					tfMaxCapacity.setEnabled(true);
				} else {
					tfMaxCapacity.setEnabled(false);
				}
			}
		});
		
		JLabel lblNewLabel_5 = new JLabel("Number of seats passenger wagon:");
		lblNewLabel_5.setBounds(10, 135, 220, 14);
		contentPane.add(lblNewLabel_5);
		
		JLabel lblNewLabel_6 = new JLabel("Maximum load capacity of the freight wagon: ");
		lblNewLabel_6.setBounds(10, 160, 229, 14);
		contentPane.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("Restaurant description:");
		lblNewLabel_7.setBounds(10, 195, 170, 14);
		contentPane.add(lblNewLabel_7);
		
		tfMaxCapacity = new JTextField();
		tfMaxCapacity.setEnabled(false);
		tfMaxCapacity.setBounds(244, 157, 91, 20);
		contentPane.add(tfMaxCapacity);
		tfMaxCapacity.setColumns(10);
		
		tfNumOfPassengerSeats = new JTextField();
		tfNumOfPassengerSeats.setEnabled(false);
		tfNumOfPassengerSeats.setBounds(244, 132, 91, 20);
		contentPane.add(tfNumOfPassengerSeats);
		tfNumOfPassengerSeats.setColumns(10);
		
		tfDescrRestaurant = new JTextField();
		tfDescrRestaurant.setEnabled(false);
		tfDescrRestaurant.setBounds(10, 210, 291, 20);
		contentPane.add(tfDescrRestaurant);
		tfDescrRestaurant.setColumns(10);
		
		comboBoxTipPV.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(comboBoxTipPV.getSelectedItem().equals("With seats") 
						|| comboBoxTipPV.getSelectedItem().equals("With bearings")) {
					tfNumOfPassengerSeats.setEnabled(true);
				} else {
					tfNumOfPassengerSeats.setEnabled(false);
				}
				
				if(comboBoxTipPV.getSelectedItem().equals("Restaurant")) {
					tfDescrRestaurant.setEnabled(true);
				} else {
					tfDescrRestaurant.setEnabled(false);
				}
			}
		});
		
		JButton btnOkButton = new JButton("OK");
		btnOkButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(comboBoxTip.getSelectedItem().equals("Passenger")) {
					wagon = new PassengerWagon();
				} else if(comboBoxTip.getSelectedItem().equals("Freight")) {
					wagon = new FreightWagon();
				} else if(comboBoxTip.getSelectedItem().equals("Shunting")) {
					wagon = new ShuntingWagon();
				} else {
					JOptionPane.showMessageDialog(contentPane, "Wagon type not selected.");
					return;
				}
				
				if(mark.getText().length() == 0 || (mark.getText()).length() > 100) {
					JOptionPane.showMessageDialog(contentPane, "Wrong wagon mark.");
					return;
				} else {
					wagon.setMark(mark.getText());
				}
				
				
				
				if(length.getText().length() == 0 || 
						(Integer.valueOf(length.getText()) <= 0 || Integer.valueOf(length.getText()) > 100)) {
					JOptionPane.showMessageDialog(contentPane, "Wrong wagon length.");
					return;
				} else {
					wagon.setLength(Integer.valueOf(length.getText()));
				}
				
				
				
				if(comboBoxTipPV.getSelectedItem().equals("With seats")) {
					((PassengerWagon)wagon).setType(TypeOfPassengerWagon.WITH_SEATS);
				} else if(comboBoxTipPV.getSelectedItem().equals("With bearings")) {
					((PassengerWagon)wagon).setType(TypeOfPassengerWagon.WITH_BEARINGS);
				} else if(comboBoxTipPV.getSelectedItem().equals("For sleeping")) {
					((PassengerWagon)wagon).setType(TypeOfPassengerWagon.FOR_SLEEPING);
				} else if(comboBoxTipPV.getSelectedItem().equals("Restaurant")) {
					((PassengerWagon)wagon).setType(TypeOfPassengerWagon.RESTAURANT);
				} else if(comboBoxTip.getSelectedItem().equals("Passenger")) {
					JOptionPane.showMessageDialog(contentPane, "Type of passenger wagon not selected.");
					return;
				}
				
				if((comboBoxTipPV.getSelectedItem().equals("With seats") 
						|| comboBoxTipPV.getSelectedItem().equals("With bearings")) &&
					(tfNumOfPassengerSeats.getText().length() == 0 ||
					(Integer.valueOf(tfNumOfPassengerSeats.getText()) <= 0 || 
					Integer.valueOf(tfNumOfPassengerSeats.getText()) > 500))) {
					JOptionPane.showMessageDialog(contentPane, "Wrong input of seat number..");
					return;
				} else if(comboBoxTipPV.getSelectedItem().equals("With seats") 
						|| comboBoxTipPV.getSelectedItem().equals("With bearings")) {
					((PassengerWagon)wagon).setGadget(
					new Gadget("Number of seats", Integer.valueOf(tfNumOfPassengerSeats.getText())));
				}
				
				if(comboBoxTip.getSelectedItem().equals("Freight") &&
						(tfMaxCapacity.getText().length() == 0 ||
					(Integer.valueOf(tfMaxCapacity.getText()) <= 0 || 
					Integer.valueOf(tfMaxCapacity.getText()) > 10000))) {
					JOptionPane.showMessageDialog(contentPane, "Incorrect maximum load capacity.");
					return;
				} else if(comboBoxTip.getSelectedItem().equals("Freight")) {
					((FreightWagon)wagon).setMaxCapacity(Integer.valueOf(tfMaxCapacity.getText()));
				}
				
				if(comboBoxTipPV.getSelectedItem().equals("Restaurant") &&
						(tfDescrRestaurant.getText().length() == 0 || 
								tfDescrRestaurant.getText().length() > 1000)) {
					JOptionPane.showMessageDialog(contentPane, "Incorrect restaurant description.");
					return;
				} else if(comboBoxTipPV.getSelectedItem().equals("Restaurant")) {
					((PassengerWagon)wagon).setGadget(
							new Gadget("Opis", tfDescrRestaurant.getText()));
				}
				
				InputComposition.addWagon(wagon);
				if(InputComposition.getComposition().configurationValidity() == false) {
					InputComposition.removeWagon();
					JOptionPane.showMessageDialog(contentPane, "Configuration is invalid."
							+ "\nCheck which wagons and locomotives can be assembled.");
					return;
				}
				
				
				
				if(InputWagon.k-- > 1) {
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							InputWagon fr = new InputWagon(InputWagon.k);
							fr.setVisible(true);
						} catch (Exception e) {
							Logger.getLogger(InputWagon.class.getName()).log(Level.SEVERE,e.toString());
						}
					}
				});
				} else {
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
