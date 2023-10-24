package org.unibl.etf.pj2.vechile;

public final class Car extends Vechile {

	private int numberOfDoors;
	
	public Car(String mark, String model, int age, int numberOfDoors, String road) throws Exception {
		super(mark,model,age,road);
		this.numberOfDoors = numberOfDoors;
	}
	
	
	public void setBrojVrata(int numberOfDoors) {
		this.numberOfDoors = numberOfDoors;
	}
	public int getBrojVrata() {
		return this.numberOfDoors;
	}
	@Override
	public int hashCode() {
		return 6;
	}
}
