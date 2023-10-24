package org.unibl.etf.pj2.vechile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Simulation;



public abstract class Vechile extends Thread {
	
	protected String mark;
	protected String model;
	protected int age;
	protected int speed;
	protected String road;
	protected int positionX;
	protected int positionY;
	protected boolean finish = false;
	
	public Vechile(String mark, String model, int age, String road) throws Exception {
		super();
		this.mark = mark;
		this.model = model;
		this.age = age;
		this.road = road;
		setStartCoordinates();
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	protected void setStartCoordinates() throws Exception {
		
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("COORDINATES OF ROADS DIRECTORY")) {
				if(data.split("::").length == 1) {
					bf.close();
					throw new Exception("In the 'configuration.txt' document there is no path to the "
							+ "folder with the coordinates of the roads.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "folder with the coordinates of the roads.");
		}
		
		try {
		bf = new BufferedReader(new FileReader(
				new File(data + File.separator + road + ".txt")));
		} catch (Exception e) {
			throw new Exception("Failed opening '" + data + File.separator
					+ road + "'.");
		}
		try {
			String tmp[] = (bf.readLine()).split(",");
			positionX = Integer.valueOf(tmp[0]);
			positionY = Integer.valueOf(tmp[1]);
			bf.close();
			return;
		} catch (Exception e) {
		  throw new Exception("The coordinates in the '" + data + File.separator + road
				  + "' document are not specified in the correct format.");
		}
	}
	protected void incrementPosition() throws Exception {
	
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("COORDINATES OF ROADS DIRECTORY")) {
				if(data.split("::").length == 1) {
					bf.close();
					throw new Exception("In the 'configuration.txt' document there is no path to the "
							+ "folder with the coordinates of the roads.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "folder with the coordinates of the roads.");
		}
		
		try {
		bf = new BufferedReader(new FileReader(
				new File(data + File.separator + road + ".txt")));
		} catch (Exception e) {
			throw new Exception("Failed opening '" + data + File.separator
					+ road + "'.");
		}
		
		try {
		
		while((data = bf.readLine()) != null) {
			String[] tmp = data.split(",");
			if(
				Integer.valueOf(tmp[0]) == positionX && 
				Integer.valueOf(tmp[1]) == positionY) {
				data = bf.readLine();
				if(data == null) {
					finish = true;
					bf.close();
					return;
				}
				//finish = (data == null) ? true : false;
				tmp = data.split(",");
				positionX = (Integer.valueOf(tmp[0]));
				positionY = (Integer.valueOf(tmp[1]));
				
				bf.close();
				return;
			}
		}
		
		} catch (Exception e) {
		  throw new Exception("The coordinates in the '" + data + File.separator + road
				  + "' document are not specified in the correct format.");
		}
		
		throw new Exception("Vechile " + mark + " " + model +  " is not on the road.");
	}
	
	protected void checkRailwayCrossing() throws Exception {
		

		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("LEVEL CROSSINGS ON ROADS DIRECTORY")) {
				if(data.split("::").length == 1) {
					bf.close();
					throw new Exception("In the 'configuration.txt' document there is no path to the "
							+ "folder with the informations of the level crossings on roads.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "folder with the informations of the level crossings on roads.");
		}
		
		try {
			File f = new File(data + File.separator + road);
			
			for(int i = 1; i <= f.list().length; i++) {
				 bf = new BufferedReader(new FileReader(
						new File(f.getPath() + File.separator +
								 String.valueOf(i) + File.separator +"Coordinates.txt")));
				
				String[] str = (bf.readLine()).split(",");
				if(str[0].equals(String.valueOf(positionX)) && 
					str[1].equals(String.valueOf(positionY))) {
					bf.close();
					bf = new BufferedReader(new FileReader(
							new File(f.getPath()  + File.separator + 
								String.valueOf(i) + File.separator + "LevelCrossing.txt")));
					
					String tmp;
					while((tmp = bf.readLine()) != null) {

						(Simulation.LevelCrossing.get(tmp)).await();
					}
				}
			}
		} catch (Exception e) {
			Logger.getLogger(Vechile.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	public void run() {
		int previousX = this.positionX, previousY = this.positionY;
		try {
			while(!finish) {
				while((Simulation.map[this.positionX][this.positionY]) != null) {
					System.out.print("");
					Thread.sleep(100);
				}
				Simulation.map[previousX][previousY] = null;
				Simulation.map[this.positionX][this.positionY] = this;
				Thread.sleep(speed);
				checkRailwayCrossing();
				previousX = this.positionX;
				previousY = this.positionY;
				incrementPosition();
				
			}
		} catch(Exception e) {
			Logger.getLogger(Vechile.class.getName()).log(Level.SEVERE,e.toString());
		}
		System.out.println("Vechile " + this.mark + " " + model + " has completed"
				+ " its movement on the road " + this.road +  ".");
		Simulation.map[this.positionX][this.positionY] = null;
	}
}
