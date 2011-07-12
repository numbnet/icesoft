package org.icefaces.samples.showcase.example.compat.dataTable;

public class Car {
	protected int id = -1;
	protected String name;
	protected String chassis;
	protected int weight;
	protected int acceleration;
	protected double mpg;
	protected double cost;
	
	public Car() {
	}
	
	public Car(int id,
	           String name, String chassis,
	           int weight, int acceleration, 
	           double mpg, double cost) {
		this.id = id;
		this.name = name;
		this.chassis = chassis;
		this.weight = weight;
		this.acceleration = acceleration;
		this.mpg = mpg;
		this.cost = cost;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getChassis() {
		return chassis;
	}
	public void setChassis(String chassis) {
		this.chassis = chassis;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getAcceleration() {
		return acceleration;
	}
	public void setAcceleration(int acceleration) {
		this.acceleration = acceleration;
	}
	public double getMpg() {
		return mpg;
	}
	public void setMpg(double mpg) {
		this.mpg = mpg;
	}
	public double getCost() {
	    return cost;
	}
	public void setCost(double cost) {
	    this.cost = cost;
	}
	
	public void applyValues(Car parent) {
	    setId(parent.getId());
	    setName(parent.getName());
	    setChassis(parent.getChassis());
	    setWeight(parent.getWeight());
	    setAcceleration(parent.getAcceleration());
	    setMpg(parent.getMpg());
	    setCost(parent.getCost());
	}
	
	public String toString() {
	    return getName();
	}
}
