package org.unibl.etf.pj2.display;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.composition.Trajectory;

public class Movements extends JFrame {

	private JPanel contentPane;
	
	public Movements() throws Exception {
		super();
		setTitle("Movements");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 465, 404);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JTextPane textPane = new JTextPane();
		contentPane.add(textPane, BorderLayout.CENTER);
		
		JScrollPane scrollBar = new JScrollPane(textPane);
		contentPane.add(scrollBar);
		try {
			textPane.setText(getText());
		} catch (Exception e) {
			Logger.getLogger(Movements.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
private  String getText() throws Exception{
		
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}
		
		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("MOVEMENTS DIRECTORY")) {
				if(data.split("::").length == 1) {
					bf.close();
					throw new Exception("In the 'configuration.txt' document there is no path to the "
							+ "folder in which the serialized movements of the compositions are located.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "folder in which the serialized movements of the compositions are located.");
		}
		bf.close();
		
		String text = "";
		File f = new File(data);
		for(String s : f.list()) {
			try
	        {   
				
				FileInputStream file = new FileInputStream(new File(f.getPath() + File.separator + s));
				ObjectInputStream in = new ObjectInputStream(file);
				Trajectory trajectory = (Trajectory)in.readObject();
				in.close();
				file.close();
				text += "\n\n" + s.substring(0, s.indexOf(".")) + ":\n\n";
				text += trajectory.toString();
				
	        }
	        catch(Exception e)
	        {
	        	Logger.getLogger(Movements.class.getName()).log(Level.SEVERE,e.toString());
	        }
		}
		return text;
	}
}
