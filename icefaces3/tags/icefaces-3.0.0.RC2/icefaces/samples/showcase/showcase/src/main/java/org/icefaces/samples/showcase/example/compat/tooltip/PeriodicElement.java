/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.compat.tooltip;

import java.io.Serializable;

public class PeriodicElement implements Serializable{
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
