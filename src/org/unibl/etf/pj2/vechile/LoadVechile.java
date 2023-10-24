package org.unibl.etf.pj2.vechile;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Simulation;

public class LoadVechile extends Thread {
	
	private HashMap <String, Integer> speed = new HashMap<String, Integer>();
	private HashMap <String, Integer> numberOfVechiles = new HashMap<String, Integer>();
	private HashMap <String, Integer> createdVechiles = new HashMap<String, Integer>();
	
	public LoadVechile() throws Exception {
		super();
		init();
	}
	
	public HashMap <String, Integer> getSpeed() {
		return this.speed;
	}
	public HashMap <String, Integer> getNumberOfVechiles() {
		return this.numberOfVechiles;
	}

	private void init() throws Exception {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}
		String data = null;
		try {
		while((data = bf.readLine()) != null) {
			if((data.split("#")[0]).equalsIgnoreCase("VECHILES")) {
				while((data = bf.readLine()) != null) {
					String[] tmp = data.split("::");
					speed.put(tmp[0], Integer.valueOf(tmp[1]));
					numberOfVechiles.put(tmp[0], Integer.valueOf(tmp[2]));
					createdVechiles.put(tmp[0], 0);
				}
			}
		}
		} catch (Exception e) {
			Logger.getLogger(LoadVechile.class.getName()).log(Level.SEVERE,e.toString());
		}
		bf.close();
	}

	
	public void run() {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path watchDir = Paths.get(Simulation.CONFIGURATION_FILE_PATH.split("configuration.txt")[0]);
			watchDir.register(watchService, ENTRY_CREATE, ENTRY_MODIFY);

			
			while(true) {
				CreateVechile cv = new CreateVechile(speed, numberOfVechiles, createdVechiles);
				cv.start();
				WatchKey key = null;
				try {
					key = watchService.take();
				} catch (InterruptedException e) {
					Logger.getLogger(LoadVechile.class.getName()).log(Level.SEVERE,e.toString());
				}
				
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					
					if(String.valueOf(event.context()).equals("configuration.txt")) {
						try {
							cv.setStop(true);
							this.createdVechiles = cv.getCreatedVechiles();
							BufferedReader bf = new BufferedReader(new FileReader(new File(
									Simulation.CONFIGURATION_FILE_PATH)));
							String data = null;
							while((data = bf.readLine()) != null) {
								if((data.split("#")[0]).equalsIgnoreCase("VECHILES")) {
									while((data = bf.readLine()) != null) {
										String[] tmp = data.split("::");
										speed.put(tmp[0], Integer.valueOf(tmp[1]));
										if(Integer.valueOf(tmp[2]) > this.numberOfVechiles.get(tmp[0])) {
											numberOfVechiles.put(tmp[0], Integer.valueOf(tmp[2]));
										}
									}
								}
							}
							bf.close();
						} catch(Exception e) {
							throw new Exception("Failed read from 'configuration.txt'");
						}
					}
				}
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(LoadVechile.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
	
}
