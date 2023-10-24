package org.unibl.etf.pj2.composition;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.unibl.etf.pj2.Simulation;

public class LoadComposition extends Thread {

	public LoadComposition() {
		super();
	}
	
	public String getPathDirectory() throws Exception {
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
		return data;
	}
	
	public void run() {
		try {
			WatchService watchService = FileSystems.getDefault().newWatchService();
			Path watchDir = Paths.get(getPathDirectory());
			watchDir.register(watchService, ENTRY_CREATE);
			
			while(true) {
				WatchKey key = null;
				try {
					key = watchService.take();
				} catch (InterruptedException ex) {
					Logger.getLogger(LoadComposition.class.getName()).log(Level.SEVERE,ex.toString());
				}
				
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();
					BufferedReader bf = new BufferedReader(new FileReader(new File(
							getPathDirectory() + File.separator + event.context())));
					Composition c = new Composition(bf);
					c.start();
					Thread.sleep(100);
					bf.close();
				}
				
				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}
		} catch (Exception e) {
			Logger.getLogger(LoadComposition.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
}
