package org.unibl.etf.pj2.composition;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.coordinates.*;
import org.unibl.etf.pj2.railwayStation.*;


public class Composition extends Thread {

	private static int index = 1;
	private String name;
	private Coordinates coordinates; //set i get
	private ArrayList<Locomotive> locomotives = new ArrayList<>();
	private ArrayList<Wagon> wagons = new ArrayList<>();
	private int speed;
	private int normalSpeed;
	private int slowedSpeed = 2000;
	private int lengthOfTheComposition;
	private int numberOfWagons;
	private int numberOfLocomotives;
	private RailwayStation destinationStation;
	private RailwayStation nextStation;
	private RailwayStation previousStation;
	private Trajectory trajectory = new Trajectory();
	private boolean isElectric = false;
	private boolean finish = false;
	
	public Composition() {
		super();
		this.name = "Composition " + String.valueOf(index++);
	}
	
	public Composition(BufferedReader bf) throws Exception {
		super();
		readComposition(bf);
		this.name = "Composition " + String.valueOf(index++);
	}
	
	public void addLocomotive(Locomotive l) {
		if(l != null) 
			locomotives.add(l);
	}
	public void addWagon(Wagon w) {
		if(w != null)
			wagons.add(w);
	}
	public void removeWagon() {
		wagons.remove(wagons.size() - 1);
	}
	public void removeLocomotive() {
		locomotives.remove(locomotives.size() - 1);
	}
	public int getNumberOfWagons() {
		return this.numberOfWagons;
	}
	public void setNumberOfWagons(int numberOfWagons) {
		this.numberOfWagons = numberOfWagons;
	}
	public void setNumberOfLocomotives(int numberOfLocomotives) {
		this.numberOfLocomotives = numberOfLocomotives;
	}
	public int getNumberOfLocomotives() {
		return this.numberOfLocomotives;
	}
	public ArrayList<Locomotive> getLocomotives() {
		return this.locomotives;
	}
	public ArrayList<Wagon> getWagons()  {
		return this.wagons;
	}
	public int getSpeed() {
		return this.speed;
	}
	public void setStartStation(RailwayStation startStation) {
		this.nextStation = startStation;
	}
	public RailwayStation getStartStation() {
		return this.nextStation;
	}
	public RailwayStation getDestinationStation() {
		return this.destinationStation;
	}
	public void setDestinationStation(RailwayStation destinationStation) {
		this.destinationStation = destinationStation;
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	
	
	/*Proverava validnost konfiguracije.*/
	public boolean configurationValidity() {
		HashMap <String, Boolean> cv = new HashMap<String, Boolean>();
		
		if(this.locomotives != null) {
			for(Locomotive l : this.locomotives) {
				if(l.getType() == LocomotiveType.PASSENGER) {
					cv.put("PASSENGER", true);
				} else if(l.getType() == LocomotiveType.FREIGHT) {
					cv.put("FREIGHT", true);
				} else if(l.getType() == LocomotiveType.SHUNTING) {
					cv.put("SHUNTING", true);
				}
			}
		} else {
			return false;
		}
		
		for(Wagon v : this.wagons) {
			if(v.hashCode() == 3) {
				cv.put("PASSENGER", true);
			} else if(v.hashCode() == 4) {
				cv.put("FREIGHT", true);
			} else if(v.hashCode() == 5) {
				cv.put("SHUNTING", true);
			}
		}
		
		if(cv.size() == 1 || cv.size() == 0) {
			return true;
		}
		return false;
	}
	
	/*Ucitava novu kompoziciju iz fajla na koji pokazuje 'bf'. Ako konfiguracija nije ispravna
	 * bacice izuzetak.  */
	private void readComposition(BufferedReader bf) throws Exception {
		
		String[] data;
		try {
			data = (bf.readLine()).split(",");
		} catch (Exception e) {
			throw new Exception("Unable to load new composition.");
		}
		
		if(Integer.valueOf(data[0]) < 1 || Integer.valueOf(data[0]) > 20) 
		{
			throw new Exception("The specified number of locomotives must not be less than 1 or more than 20.");
		}
		
		if((data[1].split("-")).length != Integer.valueOf(data[0])) {
			throw new Exception("The specified number of locomotives is not defined.");
		}
		
		
		for(String locomotive : data[1].split("-")) {
					
			String[] tmp = locomotive.split(";");
			
			if(tmp.length != 4) {
				throw new Exception("Not enough data on the locomotive is given.");
			}
					
			if(Integer.valueOf(tmp[0]) < 0 || Integer.valueOf(tmp[0]) > 10000) {
				throw new Exception("The stated amount of locomotive power is not appropriate.");
			}
				
			if(tmp[1].length() == 0 || tmp[1].length() > 100) {
				throw new Exception("The specified locomotive mark is not appropriate.");
			}
			if((tmp[2].matches("PASSENGER") || tmp[2].matches("FREIGHT") ||  
				tmp[2].matches("UNIVERSAL") || tmp[2].matches("SHUNTING")) == false) {
				throw new Exception("The specified locomotive type is incorrect.");
			}
					
			if((tmp[3].matches("STEAM") || tmp[3].matches("DIESEL") || 
					tmp[3].matches("ELECTRIC")) == false) {
				throw new Exception("The specified locomotive engine is incorrect.");
			}
			if((tmp[3]).matches("ELECTRIC")) {
				this.isElectric = true;
			}
			this.locomotives.add(new Locomotive(Integer.valueOf(tmp[0]), tmp[1],
					LocomotiveType.valueOf(tmp[2]), LocomotiveEngine.valueOf(tmp[3])));
		}				
		
		if(Integer.valueOf(data[2]) < 0 || Integer.valueOf(data[2]) > 50) 
		{
			throw new Exception("The specified wagon number is not valid.");
		}
		
		if(Integer.valueOf(data[2]) != 0) {
			for(String wagon : data[3].split("-")) {
				
				String[] tmp = wagon.split(";");
				
				if(tmp.length < 3 || tmp.length > 5) {
					throw new Exception("Wagon data is not entered well.");
				}
				
				if(Integer.valueOf(tmp[0]) < 0 || Integer.valueOf(tmp[0]) > 100) {
					throw new Exception("Wagon length is invalid.");
				}
					
				if(tmp[1].length() == 0 || tmp[1].length() > 100) {
					throw new Exception("The specified wagon designation is not appropriate.");
				}
				
				if((tmp[2].matches("PW") || tmp[2].matches("FW") || tmp[2].matches("SW")) == false) {
					throw new Exception("The specified wagon type is incorrect.");
				} else {

					if(tmp[2].matches("PW")) {
						
						if((tmp[3].matches("WITH_SEATS") || tmp[3].matches("WITH_BEARINGS") ||
							tmp[3].matches("FOR_SLEEPING") || tmp[3].matches("RESTAURANT")) == false) {
							throw new Exception("The specified passenger wagon type is incorrect.");
						} else {
							
							if(tmp[3].matches("WITH_SEATS") || tmp[3].matches("WITH_BEARINGS")) {
								if(Integer.valueOf(tmp[4]) <= 0 || Integer.valueOf(tmp[4]) > 500) {
									throw new Exception("The specified number of seats in the wagon is not appropriate.");
								} else {
									this.wagons.add(new PassengerWagon(Integer.valueOf(tmp[0]), 
											tmp[1], new Gadget("Number of seats", Integer.valueOf(tmp[4])), 
											TypeOfPassengerWagon.valueOf(tmp[3])));
								}
							}
							if(tmp[3].matches("RESTAURANT")) {
								if(tmp[4].length() <= 0 || tmp[4].length() > 1000) {
									throw new Exception("The above restaurant description is not appropriate.");
								} else {
									this.wagons.add(new PassengerWagon(Integer.valueOf(tmp[0]), 
											tmp[1], new Gadget("Description: ", tmp[4]), 
											TypeOfPassengerWagon.valueOf(tmp[3])));
								}
							}
							if(tmp[3].matches("FOR_SLEEPING")) {
								this.wagons.add(new PassengerWagon(Integer.valueOf(tmp[0]),
										tmp[1], new Gadget(), 
										TypeOfPassengerWagon.valueOf(tmp[3])));
							}
						}
					} else if(tmp[2].matches("FW")) {
						if(Integer.valueOf(tmp[3]) <= 0 || Integer.valueOf(tmp[3]) > 10000) {
							throw new Exception("The specified maximum load capacity of the freight wagon is not appropriate.");
						}	else {
							this.wagons.add(new FreightWagon(Integer.valueOf(tmp[0]),
									tmp[1], Integer.valueOf(tmp[3])
									));
						}
					} else if(tmp[2].matches("SW")) {
						this.wagons.add(new ShuntingWagon(Integer.valueOf(tmp[0]), tmp[1]));
					}
				}
			}
		}

		
		
		if(Integer.valueOf(data[4]) < 500 || Integer.valueOf(data[4]) > 5000) {
			throw new Exception("The specified speed of movement of the composition is not appropriate.");
		} else {
			this.normalSpeed = Integer.valueOf(data[4]);
		}
		
		this.lengthOfTheComposition = Integer.valueOf(data[0]) + Integer.valueOf(data[2]);
		this.coordinates = new Coordinates(this.lengthOfTheComposition);

		if((data[5].matches("F") || data[5].matches("G") ||data[5].matches("A") 
				|| data[5].matches("B") || data[5].matches("C")
				|| data[5].matches("D") || data[5].matches("E")) == false) {
			throw new Exception("The starting station is incorrect.");
		} else {
			this.nextStation = (Simulation.RailwayStations.get(data[5]));
			this.coordinates.addX(this.nextStation.getCoordinates().getFirstX());
			this.coordinates.addY(this.nextStation.getCoordinates().getFirstY());
		}
		
		if((data[6].matches("A") || data[6].matches("B") || data[6].matches("C")
				|| data[6].matches("D") || data[6].matches("E")) == false) {
			throw new Exception("The destination station is incorrect.");
		} else {
			this.destinationStation = (Simulation.RailwayStations.get(data[6]));
		}
		
		if(this.configurationValidity() == false) {
			throw new Exception("Configuration is invalid. "
					+ "Check what types of locomotives and wagons you can assemble.");
		}
	}

	private void setCountDownLatchLevelCrossing() throws Exception {
		for(int i = 1; i <= nextStation.numberRailwayCrossing(previousStation); i++) {
			
			Simulation.LevelCrossing.put(String.valueOf(previousStation.getStation()) +
					"-" + String.valueOf(nextStation.getStation()) + ":" + String.valueOf(i), 
					new CountDownLatch((int) (Simulation.LevelCrossing.get(
							String.valueOf(previousStation.getStation()) +
					"-" + String.valueOf(nextStation.getStation()) + ":" + String.valueOf(i))).
							getCount() + 1 ));
		}
	}
	
	private boolean ArrivedAtDestination() throws Exception {
		if(nextStation.atTheStation(this.coordinates.getLastX(), this.coordinates.getLastY())) {
			if(destinationStation.getStation().equals(nextStation.getStation())) {
				return true;
			}
		}
		return false;
	}
	
	private void writeOnMap() {
		for(int i = 0; i < this.wagons.size(); i++) {
			if(this.coordinates.getX(i) != null && this.coordinates.getY(i) != null) {
				try {
					Simulation.map[this.coordinates.getX(i)][this.coordinates.getY(i)] = this.wagons.get(i);
				} catch (Exception e) {
					Logger.getLogger(Composition.class.getName()).log(Level.SEVERE,e.toString());
				}
			}
		}
		for(int i = this.wagons.size(); i < this.lengthOfTheComposition; i++) {
			if(this.coordinates.getX(i) != null && this.coordinates.getY(i) != null) {
				try {
					Simulation.map[this.coordinates.getX(i)][this.coordinates.getY(i)] = this.locomotives.get(i - this.wagons.size());
				} catch (Exception e) {
					Logger.getLogger(Composition.class.getName()).log(Level.SEVERE,e.toString());
				}
			}
		}
	}
	
	private void removeFromMap() {
		for(int i = 0; i < this.lengthOfTheComposition; i++) {
			if(this.coordinates.getX(i) != null && this.coordinates.getY(i) != null) {
				try {
					Simulation.map[this.coordinates.getX(i)][this.coordinates.getY(i)] = null;
				} catch (Exception e) {
					Logger.getLogger(Composition.class.getName()).log(Level.SEVERE,e.toString());
				}
			}
		}
	}
	
	private void enteringTheStation() throws Exception {
		for(int i = 0; i < this.lengthOfTheComposition; i++) {
			if(this.coordinates.getX(i) != null && this.coordinates.getY(i) != null) {
				try {
					if(previousStation != null) {
						nextStation.checkRailwayCrossing(previousStation, this.coordinates.getX(i), 
								this.coordinates.getY(i), speed, isElectric);
						}
					Simulation.map[this.coordinates.getX(i)][this.coordinates.getY(i)] = null;
					if(((i + this.wagons.size()) < this.lengthOfTheComposition) && 
							((i + this.locomotives.size()) < this.lengthOfTheComposition)) {
						Simulation.map[this.coordinates.getX(i + this.wagons.size())]
								[this.coordinates.getY(i + this.wagons.size())] = 
								 this.wagons.isEmpty() ? null : this.wagons.get(i);
					}
					Thread.sleep(speed);
				} catch (Exception e) {
					Logger.getLogger(Composition.class.getName()).log(Level.SEVERE,e.toString());
				}
			}
		}
	}
	
	private void serializeTrajectory() throws Exception {
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

		try
        {   
            FileOutputStream file = new FileOutputStream(data + File.separator + this.name + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject(this.trajectory);
            out.close();
            file.close();
        }
        catch(IOException ex)
        {
            throw new Exception("Object serialization failed.");
        }
		
	}

	private void setTrajectory() {
		if(previousStation == null) {
			this.trajectory.add("START->" + String.valueOf(nextStation.getStation()), 
					this.coordinates.getLastX(), this.coordinates.getLastY());
		} else {
			this.trajectory.add(String.valueOf(previousStation.getStation() + "->" + 
				String.valueOf(nextStation.getStation())), this.coordinates.getLastX(), 
				this.coordinates.getLastY());
		}
	}
	
	public void run() {
		String line = null;
		speed = normalSpeed;
		this.trajectory.startTime();
		try {
			while(!finish) {
				
				setTrajectory();
				Thread.sleep(speed);
				
				/*Provera da li je kompozicija stigla na stanicu prema kojoj se kretala.*/
				if(nextStation.atTheStation(this.coordinates.getLastX(), this.coordinates.getLastY())) {
					
					enteringTheStation();
					previousStation = nextStation;
					
					/*Ovim kompozicija govori ostalim objektima da je zavrsila kretanje po odgovarajucoj liniji. */
					(Simulation.Lines.get(line)).countDown(); 
					
					speed = normalSpeed;
					this.coordinates = new Coordinates(this.lengthOfTheComposition);
					
					/*Kompozicija dobija upute prema kojoj narednoj zeljeznickoj stanici treba da krene.*/
					nextStation = previousStation.getDirections(destinationStation);
					line = String.valueOf(previousStation.getStation() + "-" +
							String.valueOf(nextStation.getStation()));
					
					/*Kompozicija govori da krece na odgovarajucu deonicu i da se pruzni prelazi na toj deonici zatvore za vozila.*/
					setCountDownLatchLevelCrossing();
					
			
					/*Kompozicija dobija koordinate za izlaz iz trenutne zeljeznicke stanice. */
					this.coordinates = previousStation.setExitCoordination(nextStation, lengthOfTheComposition);
					
			
					Thread.sleep(previousStation.getSleep());
					
					/*Provera da li je deonica zauzeta.*/
					if((Simulation.Lines.get(line)).getCount() != 0) { 
						//deonica zauzeta, kreni usporenom brzinom
						while(Simulation.map[this.coordinates.getLastX()][this.coordinates.getLastY()] != null) {
							Thread.sleep(100);
						}
						speed = slowedSpeed;
					} /*Voz dolazi iz suprotnog pravca*/
					else if((Simulation.Lines.get
							(new StringBuilder(line).reverse().toString())).getCount() != 0) {
						(Simulation.Lines.get(new StringBuilder(line).reverse().toString())).await();
					
					}
					/*Kompozicija obavestava ostale objekte da zauzima odgovarajucu deonicu. */
					Simulation.Lines.put(line, new CountDownLatch(((int)(Simulation.Lines.get(line)).getCount() + 1)));
					
					writeOnMap();
					Thread.sleep(2000);
				}
				/*Proverava se da li je kompozicija presla pruzni prelaz ako on postoji na deonici kojom se kompozicija krece. */
				nextStation.checkRailwayCrossing(previousStation, this.coordinates.getFirstX(), this.coordinates.getFirstY(), speed, isElectric);
				
				removeFromMap();
				/*Kompozicija od zeljeznicke stanice prema kojoj se krece dobija koordinate za kretanje. */
				this.coordinates.add(nextStation.incrementPosition(previousStation, this.coordinates.getLastX(), this.coordinates.getLastY()));
				writeOnMap();
				
				/*Provera da li je kompozicija stigla na odrediste. */
				if(ArrivedAtDestination()) {
					enteringTheStation();
					finish = true;
					(Simulation.Lines.get(String.valueOf(line))).countDown();
					this.trajectory.stopTime();
					serializeTrajectory();
				}
			}
		} catch (Exception e) {
			Logger.getLogger(Composition.class.getName()).log(Level.SEVERE,e.toString());
		}
		System.out.println(this.name + " has finished.");
	}
	
	
}
