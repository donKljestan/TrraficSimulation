package org.unibl.etf.pj2.vechile;

public class Truck extends Vechile {

	private int capacity;
	
	public Truck(String mark, String model, int age, String road, int capacity) throws Exception {
		super(mark,model,age,road);
		this.capacity = capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getCapacity() {
		return capacity;
	}
	@Override
	public int hashCode() {
		return 7;
	}
}
