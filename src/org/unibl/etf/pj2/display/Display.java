package org.unibl.etf.pj2.display;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Simulation;
public class Display extends Thread {

	public Display() {
		super();
	}
	
	
	public void run() {
		try {
			DisplayMap displayMap = new DisplayMap();
			displayMap.setVisible(true);
			while(true) {
				displayMap.removeAll();
				for(int i =0 ; i < Simulation.SIZE_OF_MAP_X; i++) {
					for(int j = 0; j < Simulation.SIZE_OF_MAP_Y; j++) {
						if(Simulation.map[i][j] != null) {
							if(Simulation.map[i][j].hashCode() == 1) {
								displayMap.showPoint(i, j, "electricLocomotive");
							} else if(Simulation.map[i][j].hashCode() == 2) {
								displayMap.showPoint(i, j, "locomotive");
							} else if(Simulation.map[i][j].hashCode() == 3 || 
									Simulation.map[i][j].hashCode() == 4 || 
									Simulation.map[i][j].hashCode() == 5) {
								displayMap.showPoint(i, j, "wagon");
							} else if(Simulation.map[i][j].hashCode() == 6) {
								displayMap.showPoint(i, j, "car");
							} else if(Simulation.map[i][j].hashCode() == 7) {
								displayMap.showPoint(i, j, "truck");
							}
							Thread.sleep(20);
						}
					}
				}
				Thread.sleep(100);
			}
		} catch(Exception e) {
			Logger.getLogger(Display.class.getName()).log(Level.SEVERE,e.toString());
		}
	}
}
