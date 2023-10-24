package org.unibl.etf.pj2.vechile;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;



public class CreateVechile extends Thread {
	
	private HashMap <String, Integer> speed = new HashMap<String, Integer>();
	private HashMap <String, Integer> numberOfVechiles = new HashMap<String, Integer>();
	private HashMap <String, Integer> createdVechiles = new HashMap<String, Integer>();
	private boolean stop = false;
	
	public CreateVechile( HashMap <String, Integer> speed, 
			 				HashMap <String, Integer> numberOfVechiles,
			 				HashMap <String, Integer> createdVechiles) throws Exception {
		super();
		this.speed = speed;
		this.numberOfVechiles = numberOfVechiles;
		this.createdVechiles = createdVechiles;
	}
	public void setStop(boolean stop) {
		this.stop = stop;
	}
	public HashMap <String, Integer> getCreatedVechiles() {
		return this.createdVechiles;
	}

	public void run() {
		Random rand = new Random();
		List<String> keysAsArray = new ArrayList<String>(speed.keySet());
		while(!stop) {
			String randomKey = keysAsArray.get(rand.nextInt(keysAsArray.size()));
			try {
				if(this.createdVechiles.get(randomKey) < this.numberOfVechiles.get(randomKey)) {
					/*Creating vechile*/
					if(rand.nextBoolean() == true) { //create car
						Car car = new Car("Ford", "Focus", 10, 4, randomKey.substring(1 +
								randomKey.indexOf("_")));
						car.setSpeed(rand.nextInt(2000) + this.speed.get(randomKey));
						car.start();
					} else { //create truck
						Truck truck = new Truck("Mercedes", "100", 10, randomKey.substring(1 +
								randomKey.indexOf("_")), 5000);
						truck.setSpeed(rand.nextInt(2000) + this.speed.get(randomKey));
						truck.start();
					}
					this.createdVechiles.put(randomKey, this.createdVechiles.get(randomKey) + 1);
				}
				Thread.sleep(2000);
			}
			catch (Exception e) {
				Logger.getLogger(CreateVechile.class.getName()).log(Level.SEVERE,e.toString());
			}

		}
	
	}
}
