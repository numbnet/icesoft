package org.icefaces.samples.showcase.example.compat.tooltip;

public class PeriodicElement {
    private int number;
    private String name;
    private String symbol;
    private double meltPointC;
    private double meltPointF;
    
	public PeriodicElement(int number, String symbol, String name,
					       double meltPointC, double meltPointF) {
		this.number = number;
		this.name = name;
		this.symbol = symbol;
		this.meltPointC = meltPointC;
		this.meltPointF = meltPointF;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSymbol() {
		return symbol;
	}
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	public double getMeltPointC() {
		return meltPointC;
	}
	public void setMeltPointC(double meltPointC) {
		this.meltPointC = meltPointC;
	}
	public double getMeltPointF() {
		return meltPointF;
	}
	public void setMeltPointF(double meltPointF) {
		this.meltPointF = meltPointF;
	}
	
	public String toString() {
	    return "#" + getNumber() + " " + getName() + " (" + getSymbol() + ")";
	}
}
