package org.icefaces.dataView.data;

import java.io.Serializable;

public class Car implements Serializable {

	private static final long serialVersionUID = 1L;
	protected int id = -1;
	protected String model;
	protected String chassis;
	protected int weight;
	protected int acceleration;
	protected double mpg;
	protected double cost;
	
	public Car(){}
	
	public Car(int id, String model, String chassis, int weight, int acceleration, double mpg, double cost) {
		this.id = id;
		this.model = model;
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
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
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
}
