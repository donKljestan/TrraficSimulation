package org.unibl.etf.pj2.railwayStation;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.unibl.etf.pj2.Simulation;
import org.unibl.etf.pj2.coordinates.*;

public class RailwayStation {

	private Station station;
	private final Coordinates COORDINATES;
	private static int sl = 1000;
	
	public RailwayStation(Station station, Coordinates COORDINATES) {
		super();
		this.station = station;
		this.COORDINATES = COORDINATES;
	}
	public Station getStation() {
		return this.station;
	}
	
	public Coordinates getCoordinates() {
		return this.COORDINATES;
	}
	
	/*Postavljaju se koordinate za izlaz voza sa trenutne stanice prema sledecoj stanici.
	 * Trenutna stanica poziva funkciju i kao argument prosledjuje sledecu stanicu.
	 * Argument 'length' oznacava duzinu kompozicije. */
	//positionX = positionsX.getLast()
	
	public synchronized Coordinates setExitCoordination(RailwayStation nextStation, int length) throws Exception {
		
		Coordinates coordinates = new Coordinates(length);
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
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
		try {
				bf = new BufferedReader(new FileReader(
					new File(data + File.separator + String.valueOf(this.getStation()) + 
					 "-" + String.valueOf(nextStation.getStation()) + ".txt")));
		} catch (Exception e) {
			throw new Exception("Falied opening '" +  String.valueOf(this.getStation()) + 
					 "-" + String.valueOf(nextStation.getStation()) + "'." );
		}
			
			try {
				String[] tmp = (bf.readLine()).split(",");
				if(tmp.length != 2)
				{
					throw new Exception();
				}
				for(int i=0; i < length; i++)
				{
					coordinates.setPositionsX(i, Integer.valueOf(tmp[0]));
					coordinates.setPositionsY(i, Integer.valueOf(tmp[1]));
				}
				bf.close();
				return coordinates;
			
		} catch (Exception e) {
		  throw new Exception("The coordinates in the '" + String.valueOf(this.getStation()) + 
					 "-" + String.valueOf(nextStation.getStation()) + "' document are not specified in "
					 		+ "the correct format.");
		}
	}

	/*Postavljaju se nove koordinate kompozicije na zadatoj putanji. Sledeca stanica
	 * poziva funkciju i kao argument prosledjuje stanicu sa koje je kompozicija krenula. 
	 * Argumenti 'positionX' i 'positionY' pokazuju trenutnu poziciju kompozicije. */
	//x = positionsX.getLast()
	public synchronized Coordinates incrementPosition(RailwayStation previousStation, int positionX, int positionY) throws Exception {
		
		Coordinates coordinates = new Coordinates(1);
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
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
		
		try {
			bf = new BufferedReader(new FileReader(
				new File(data + File.separator + String.valueOf(previousStation.getStation()) + 
				 "-" + String.valueOf(this.getStation()) + ".txt")));
		} catch (Exception e) {
			throw new Exception("Falied opening '" +  String.valueOf(previousStation.getStation()) + 
					 "-" + String.valueOf(this.getStation()) + "'." );
		}
		
		try {
		while((data = bf.readLine()) != null) {
			String[] tmp = data.split(",");
			if(tmp.length != 2)
			{
				bf.close();
				throw new Exception();
			}
			if(
				Integer.valueOf(tmp[0]) == positionX && 
				Integer.valueOf(tmp[1]) == positionY) {
				data = bf.readLine();

				tmp = data.split(",");
				coordinates.addX(Integer.valueOf(tmp[0]));
				coordinates.addY(Integer.valueOf(tmp[1]));
				
				bf.close();
				return coordinates;
			}
		}
		} catch (Exception e) {
		  throw new Exception("The coordinates in the '" + String.valueOf(previousStation.getStation()) + 
					 "-" + String.valueOf(this.getStation()) + "' document are not specified in "
				 		+ "the correct format.");
		}
		throw new Exception("Coordinates (" + String.valueOf(positionX) + "," + String.valueOf(positionY) +
				" are not located on the railway line.");
	}


	/*Sledeca stanica koju kompozicija treba da poseti poziva funkciju i vraca 'true' 
	 * ako je kompozicija pristigla na nju.*/
	//positionX = positionsX.getLast()
	public synchronized boolean atTheStation(Integer positionX, Integer positionY) {
		return this.COORDINATES.checkCoordinates(positionX, positionY);
	}
	

	/*Zeljeznicka stanica na kojoj se nalazi kompozicija poziva funkciju. Funkcija vraca 
	 * sledecu zeljeznicku stanicu koju kompozicija treba da poseti na putu do odredista.*/
	public synchronized RailwayStation getDirections(RailwayStation finalDestination) throws Exception {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("DIRECTIONS FOR THE NEXT STATIONS")) {
				if(data.split("::").length == 1) {
					bf.close();
					throw new Exception("In the 'configuration.txt' document there is no path to the "
							+ "file with directions to the next stations.");
				} 
				data = (data.split("::")[1]);
				break;
			}
		}
		if(data == null) {
			bf.close();
			throw new Exception("In the 'configuration.txt' document there is no path to the "
					+ "file with directions to the next stations.");
		}
		
		try {
			bf = new BufferedReader(new FileReader(
				new File(data)));
		} catch (Exception e) {
			throw new Exception("Falied opening '"
					+ "/DIRECTIONS FOR THE NEXT STATIONS/Directions.txt'");
		}
		
		try {
		while((data = bf.readLine()) != null) {
		String[] tmp = data.split("->");
		if((String.valueOf(this.getStation())).equals(String.valueOf(tmp[0])) &&
			(String.valueOf(finalDestination.getStation())).equals(String.valueOf((tmp[1]).charAt(0)))) {
					
				tmp = data.split(":");
				bf.close();
				return Simulation.RailwayStations.get(tmp[1]);		
				}
			}
		} catch (Exception e) {
		  throw new Exception("The coordinates in the 'Directions' document are not specified in "
				 		+ "the correct format.");
		}
		bf.close();
		return null;
	}

	/*Vozovi izlaze sa stanice na svaku sekundu.*/
	public synchronized long getSleep() {
		sl = sl > 3000 ? 1000 : (sl + 1000); 
		return sl;
	}
	
	
	/*Vraca broj pruznih prelaza na odgovarajucoj liniji. 
	 * Funkciju poziva zeljeznicka stanica prema kojoj se kompozicija krece a kao argument
	 * prosledjuje poslednja stanica na kojoj je kompozicija bila.*/
	public synchronized int numberRailwayCrossing(RailwayStation previousStation) throws Exception {
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("LEVEL CROSSINGS DIRECTORY")) {
				if(data.split("::").length == 1) {
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
		
		File f = null;
		try { 
			f = new File(data + File.separator + String.valueOf(previousStation.getStation()) + "-" +
					String.valueOf(this.getStation()));
		} catch (Exception e) {
			throw new Exception("Failed open '" +String.valueOf(previousStation.getStation()) + "-" +
					String.valueOf(this.getStation()) + "'");
		}
		return f.list().length;
	}

	/*Funkciju poziva zeljeznicka stanica prema kojoj se kompozicija krece.
	 * Kao argument prosledjuje se poslednja zeljeznicka stanica na kojoj je kompozcija bila.
	 * Argumenti 'x' i 'y' predstavljaju trenutnu lokaciju kompozicije a 'speed' brzinu
	 * kojom se kompozicija krece. Funkcija ispituje da li je kompozicija presla pruzni 
	 * prelaz a ako nije, postavice zabranu kretanja za automobile koji cekaju na 
	 * odgovarajucem pruznom prelazu.*/
	public synchronized void checkRailwayCrossing(RailwayStation previousStation,
			Integer x, Integer y, int speed, boolean isElectric) throws Exception {
		
		BufferedReader bf = null;
		try {
			bf = new BufferedReader(new FileReader(
				new File(Simulation.CONFIGURATION_FILE_PATH)));
		} catch (Exception e) {
			throw new Exception("Failed opening 'configuration.txt'.");
		}

		String data;
		while((data = bf.readLine()) != null) {
			if((data.split("::")[0]).equalsIgnoreCase("LEVEL CROSSINGS DIRECTORY")) {
				if(data.split("::").length == 1) {
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
		for(int i = 1; i <= this.numberRailwayCrossing(previousStation); i++) {
			try {
				bf = new BufferedReader(new FileReader(
						new File(data + File.separator + String.valueOf(previousStation.getStation()) + 
								"-" + String.valueOf(this.getStation()) + 
								File.separator + String.valueOf(i)) + ".txt"));
				} catch (Exception e) {
					bf.close();
					throw new Exception("Failed opening '" + data + File.separator + String.valueOf(previousStation.getStation()) + 
							"-" + String.valueOf(this.getStation()) + 
							File.separator + String.valueOf(i) + "'.");
				}
			try {
				String[] tmp = (bf.readLine()).split(",");
				bf.close();
				if(x == Integer.valueOf(tmp[0]) && y == Integer.valueOf(tmp[1])) {
					Thread.sleep(speed);
					if(isElectric) {
						Thread.sleep(speed);
					}
					(Simulation.LevelCrossing.get(String.valueOf(previousStation.getStation()) + 
						"-" + String.valueOf(this.getStation()) + ":" +
							String.valueOf(i))).countDown();
				}
				
			} catch(Exception e) {
				bf.close();
				throw new Exception("The coordinates in the '" + data + File.separator + String.valueOf(previousStation.getStation()) + 
						"-" + String.valueOf(this.getStation()) + 
						File.separator + String.valueOf(i) + "' document are not specified in "
				 		+ "the correct format.");
			}
			bf.close();
		}
	}

}
