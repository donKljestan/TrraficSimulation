package org.unibl.etf.pj2.display;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.*;

public class DisplayMap extends JFrame {

	static Container c;
	static BackgroundPanel back;
	static JLabel[][] matrix = new JLabel[Simulation.SIZE_OF_MAP_X][Simulation.SIZE_OF_MAP_Y];
	static private ImageIcon truckImageIcon;
	static private ImageIcon carImageIcon;
	static private ImageIcon locomotiveImageIcon;
	static private ImageIcon electricLocomotiveImageIcon;
	static private ImageIcon wagonImageIcon;
	static private File TRAFFIC_SIMULATION;
	
	public DisplayMap() throws Exception {
		
		c = getContentPane();
		setUndecorated(true);
		setBackground(new Color(0,0,0,0));
		
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}
		
		String data;
		
			while((data = bf.readLine()) != null) {
				if((data.split("::")[0]).equalsIgnoreCase("TRAFFIC SIMULATION DIRECTORY")) {
					if((data.split("::").length == 1)) {
						bf.close();
						throw new Exception("In the 'configuration.txt' document there is no path to the "
								+ "folder 'TRAFFIC SIMULATION'.");
					} 
					data = (data.split("::")[1]);
					break;
				}
			}
			if(data == null) {
				bf.close();
				throw new Exception("In the 'configuration.txt' document there is no path to the "
						+ "folder 'TRAFFIC SIMULATION'.");
			}
		
		TRAFFIC_SIMULATION = new File(data);
		if(((Arrays.asList(TRAFFIC_SIMULATION.list())).contains("map.png")) == false) {
			throw new Exception("Unable to load map.");
		}
		back = new BackgroundPanel(new ImageIcon(TRAFFIC_SIMULATION.getPath() + File.separator + "map.png"));
		back.setBackground(new Color(0,0,0,0));
		back.setLayout(new GridLayout(4,1));
		
		setSize(617,554);
		setLocation(400, 100);	
		c.add(back);
		
		
		if(((Arrays.asList(TRAFFIC_SIMULATION.list())).contains("car.png")) == false) {
			throw new Exception("Unable to load car symbol.");
		}
		carImageIcon = new ImageIcon(TRAFFIC_SIMULATION.getPath() + File.separator + "car.png");
		
		if(((Arrays.asList(TRAFFIC_SIMULATION.list())).contains("truck.png")) == false) {
			throw new Exception("Unable to load truck symbol.");
		}
		truckImageIcon = new ImageIcon(TRAFFIC_SIMULATION.getPath() + File.separator + "truck.png");
		
		if(((Arrays.asList(TRAFFIC_SIMULATION.list())).contains("electricLocomotive.png")) == false) {
			throw new Exception("Unable to load electric locomotive symbol.");
		}
		
		electricLocomotiveImageIcon = new ImageIcon(TRAFFIC_SIMULATION.getPath() + File.separator + "electricLocomotive.png");
		
		if(((Arrays.asList(TRAFFIC_SIMULATION.list())).contains("locomotive.png")) == false) {
			throw new Exception("Unable to load locomotive symbol.");
		}
		
		locomotiveImageIcon = new ImageIcon(TRAFFIC_SIMULATION.getPath() + File.separator + "locomotive.png");
		
		if(((Arrays.asList(TRAFFIC_SIMULATION.list())).contains("wagon.png")) == false) {
			throw new Exception("Unable to load wagon symbol.");
		}
		
		wagonImageIcon = new ImageIcon(TRAFFIC_SIMULATION.getPath() + File.separator + "wagon.png");
	}
	
	public synchronized void removeAll() {
		for(Integer i = 0; i < Simulation.SIZE_OF_MAP_X; i++) {
			for(Integer j = 0; j < Simulation.SIZE_OF_MAP_Y; j++)
			{
				if(matrix[i][j] != null) {
					try {
						removePoint(i,j);
					} catch (Exception e) {
						Logger.getLogger(DisplayMap.class.getName()).log(Level.SEVERE,e.toString());
					}
				}
			}
		}
	}
	
	public  synchronized void removePoint(Integer x, Integer y) throws Exception {
		if(x > Simulation.SIZE_OF_MAP_X || y > Simulation.SIZE_OF_MAP_Y || x < 0 || y < 0) {
			System.out.println("The given coordinates are outside the map.");
			return;
		}
		try {
			c.remove(matrix[x][y]);
			c.revalidate();
			c.repaint();
		} catch(Exception e) {
			throw new Exception("There is no element on the matrix with the given coordinates.");
		}
	}

	public synchronized void showPoint(int x, int y, String object) throws Exception {
		
		if(x > Simulation.SIZE_OF_MAP_X || y > Simulation.SIZE_OF_MAP_Y || x < 0 || y < 0) {
			System.out.println("The given coordinates are outside the map.");
			return;
		}
		if(("car".equals(object.toLowerCase()) || 
				"truck".equals(object.toLowerCase()) ||
				"locomotive".equals(object.toLowerCase()) || 
				"electricLocomotive".equals(object) ||
				"wagon".equals(object.toLowerCase())) == false) {
			System.out.println("You must specify what you want to display on the map. "
					+ "Car, truck,  locomotive or wagon. ");
			return;
		}
		
		
		try {
			BufferedReader bf = new BufferedReader(new FileReader(new File(
					TRAFFIC_SIMULATION.getPath() + File.separator +
					"mappingCoordinates.txt")));
			String data;
			while((data = bf.readLine()) != null) {
				String[] tmp = data.split(":");
				String[] xy = tmp[0].split(",");
				String[] XY = tmp[1].split(",");
				if(Integer.valueOf(xy[0]) == x && Integer.valueOf(xy[1]) == y) {
					
					matrix[x][y] = new JLabel("");
					
					if("car".equals(object)) {
						matrix[x][y].setIcon(carImageIcon);
					} else if("electricLocomotive".equals(object)) {
						matrix[x][y].setIcon(electricLocomotiveImageIcon);
					} else if("wagon".equals(object)) {
						matrix[x][y].setIcon(wagonImageIcon);
					} else if("locomotive".equals(object)) {
						matrix[x][y].setIcon(locomotiveImageIcon);
					} else if("truck".equals(object)) {
						matrix[x][y].setIcon(truckImageIcon);
					}
					matrix[x][y].setBounds(Integer.valueOf(XY[0]), Integer.valueOf(XY[1]), 10, 10);
					
					c.remove(back);
					c.add(matrix[x][y]);
					c.add(back);
					c.revalidate();
					c.repaint();
				}
			}
			bf.close();
		} catch (FileNotFoundException e) {
			Logger.getLogger(DisplayMap.class.getName()).log(Level.SEVERE,e.toString());
		}	
	}	
	class BackgroundPanel extends JPanel {
		
		ImageIcon icon;
		
		public BackgroundPanel(ImageIcon icon) {
			this.icon = icon;
		}
		
		public void setIcon(ImageIcon icon) {
			this.icon = icon;
		}
		
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if(this.icon != null) {
				g.drawImage(icon.getImage(), 0, 0, this);
			}
		}
	}

}
