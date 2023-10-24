package org.unibl.etf.pj2.composition;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

import org.unibl.etf.pj2.coordinates.*;

public class Trajectory implements Serializable {
	
	private Instant start;
	private Instant stop;
	public Duration elapsedTime;
	public HashMap<String, ArrayList<Coordinates>> trajectory = new HashMap<String, ArrayList<Coordinates>>();
	
	public Trajectory() {
		super();
	}
	
	public void add(String line, Integer x, Integer y) {
		ArrayList<Coordinates> arr = (this.trajectory.get(line) == null) ? 
				new ArrayList<Coordinates>() : this.trajectory.get(line);
		Coordinates coordinates = new Coordinates(x,y);
		arr.add(coordinates);
		this.trajectory.put(line, arr);
	}
	
	public void startTime() {
		start = Instant.now();
	}
	
	public void stopTime() {
		stop = Instant.now();
		elapsedTime = Duration.between(start, stop);
	}
	
	@Override
	public String toString() {
		return "\n" + this.trajectory.toString() + "\nElapsed time: " + 
				elapsedTime.getSeconds();
	}
	
}
