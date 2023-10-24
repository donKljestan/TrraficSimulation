package org.unibl.etf.pj2.composition;

public final class Gadget {

	private Object gadget;
	private String name;
	
	public Gadget() {
		super();
		this.name = "";
		this.gadget = "";
	}
	public Gadget(String name, Object gadget) {
		super();
		this.name = name;
		this.gadget = gadget;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public Object getGadget() {
		return gadget;
	}
	
	public void setGadget(Object gadget) {
		this.gadget = gadget;
	}
	
	@Override
	public String toString() {
		return name + ": " + String.valueOf(gadget);
	}
}
