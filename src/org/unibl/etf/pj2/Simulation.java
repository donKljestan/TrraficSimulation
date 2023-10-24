package org.unibl.etf.pj2;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import org.unibl.etf.pj2.railwayStation.*;
import org.unibl.etf.pj2.vechile.*;
import org.unibl.etf.pj2.composition.LoadComposition;
import org.unibl.etf.pj2.coordinates.*;
import org.unibl.etf.pj2.display.StartFrame;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.EventQueue;
import java.io.*;

public class Simulation {
	
	public static final int SIZE_OF_MAP_X = 30;
	public static final int SIZE_OF_MAP_Y = 30;
	public static Object[][] map = new Object[SIZE_OF_MAP_X][SIZE_OF_MAP_Y];
	public static final String CONFIGURATION_FILE_PATH = System.getProperty("user.dir") + File.separator + "configuration.txt";
	public static HashMap<String, CountDownLatch> Lines = new HashMap<String, CountDownLatch>();
	public static HashMap<String, CountDownLatch> LevelCrossing = new HashMap<String, CountDownLatch>();
	public static final HashMap<String, RailwayStation> RailwayStations = loadRailwayStations();
			
	/*Ucitava zeljeznicke stanice u HashMapu. */
	private static HashMap<String,RailwayStation> loadRailwayStations() {
		HashMap<String, RailwayStation> railwayStations = new HashMap<String, RailwayStation>();
		
		try {
			
			BufferedReader bf = null;
			try {
				bf = new BufferedReader(new FileReader(
					new File(Simulation.CONFIGURATION_FILE_PATH)));
			} catch (Exception e) {
				throw new Exception("Failed opening 'configuration.txt'.");
			}

			String data;
			while((data = bf.readLine()) != null) {
				if((data.split("::")[0]).equalsIgnoreCase("COORDINATES OF RAILWAY STATIONS DIRECTORY")) {
					if(data.split("::").length == 1) {
						bf.close();
						throw new Exception("In the 'configuration.txt' document there is no path to the "
								+ "folder with the coordinates of the railways.");
					} 
					data = (data.split("::")[1]);
					break;
				}
			}
			if(data == null) {
				bf.close();
				throw new Exception("In the 'configuration.txt' document there is no path to the "
						+ "folder with the coordinates of the railways.");
			}
			
			try {
				File f = new File(data);
				for(String s : f.list()) {
					bf = new BufferedReader(new FileReader(
							new File(data + File.separator + s)));
					String tmp[] = (bf.readLine()).split(":");
					Coordinates coordinate = new Coordinates(tmp.length);
					for(String st : tmp) {
						coordinate.addX(Integer.valueOf(st.split(",")[0]));
						coordinate.addY(Integer.valueOf(st.split(",")[1]));
					}
					railwayStations.put(s.substring(0, s.indexOf(".")), 
					new RailwayStation(Station.valueOf(s.substring(0, s.indexOf("."))), coordinate));
				}
				return railwayStations;
			} catch (Exception e) {
				throw new Exception("Error opening documents with train station coordinates.");
			}	
		} catch (Exception e) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
		}
		return null;
	}
	
	/*Ucitava dionice u HashMapu. */
	private static void loadLines() throws Exception {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}
		
		String data;
		try {
			while((data = bf.readLine()) != null) {
				if((data.split("::")[0]).equalsIgnoreCase("RAILWAY COORDINATE DIRECTORY")) {
					if(data.split("::").length == 1) {
						bf.close();
						throw new Exception("In the 'configuration.txt' document there is no path to the "
								+ "folder with the coordinates of the railways.");
					} 
					data = (data.split("::")[1]);
					break;
				}
			}
			if(data == null) {
				bf.close();
				throw new Exception("In the 'configuration.txt' document there is no path to the "
						+ "folder with the coordinates of the railways.");
			}
			
			File f = new File(data);
			for(String s : f.list()) {
				Simulation.Lines.put(s.substring(0, s.indexOf(".")), new CountDownLatch(0));
			}
			Simulation.Lines.put(null, new CountDownLatch(0));
		} catch (Exception e) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	
	/*Ucitava pruzne prelaze u HashMapu.*/
	private static void loadLevelCrossing() throws Exception{
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}
		
		String data;
		try {
			while((data = bf.readLine()) != null) {
				if((data.split("::")[0]).equalsIgnoreCase("LEVEL CROSSINGS DIRECTORY")) {
					if((data.split("::").length == 1)) {
						bf.close();
						throw new Exception("In the 'configuration.txt' document there is no path to the "
								+ "folder with the coordinates of the level crossings.");
					} 
					data = (data.split("::")[1]);
					break;
				}
			}
			if(data == null) {
				bf.close();
				throw new Exception("In the 'configuration.txt' document there is no path to the "
						+ "folder with the coordinates of the level crossings.");
			}
			
			File f = new File(data);
			for(String s : f.list()) {
				File fd = new File(data + File.separator + s);
				for(String sd : fd.list()) {
					Simulation.LevelCrossing.put(s + ":" + sd.substring(0, sd.indexOf(".")), new CountDownLatch(0));
				}
			}
			Simulation.LevelCrossing.put(null, new CountDownLatch(0));
		} catch (Exception e) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	
	/*Brise stare konfiguracije kompozicija.*/
	private static void clearTrainsFolder() throws Exception {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("TRAINS")) {
				if(data.split("::").length == 1) {
					bf.close();
					throw new Exception("In the 'configuration.txt' document there is no path to the "
							+ "folder 'TRAINS'.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "folder 'TRAINS'.");
		}
		File dir = new File(data);
		for(File f : dir.listFiles()) {
			f.delete();
		}
	}

	/*Brise stara serijalizovana kretanja.*/
	private static void clearMovementsFolder() throws Exception {
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
							+ "folder 'MOVEMENTS'.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "folder 'MOVEMENTS'.");
		}
		File dir = new File(data);
		for(File f : dir.listFiles()) {
			f.delete();
		}
	}
	
	public static void main(String[] args) {
	
		
		try {
			clearTrainsFolder();
			clearMovementsFolder();
			loadLines();
			loadLevelCrossing();
			
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						StartFrame frame = new StartFrame();
						frame.setVisible(true);
					} catch (Exception e) {
						Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
					}
				}
			});
			
			LoadComposition lc = new LoadComposition();
			lc.start();
				
			LoadVechile lv = new LoadVechile();
			lv.start();

		} catch(Exception e) {
			Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	

}
