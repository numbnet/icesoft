package com.icesoft.icefaces.tutorial.component.panelSeries.basic;


/**
 * <p>
 * A basic backing bean for a ice:panelSeries component.  The only instance variable
 * needed is an array of Strings which is bound to the icefaces tree
 * component in the jspx code.</p>
 */
public class BasicPanelSeriesBean{

	private String[] colorList = new String[]{
		"Black",
		"White",
		"Yellow",
		"Green",
		"Red"
	};


	public String[] getColorList(){
		return this.colorList;
	}

	public void setColorList(String[] colorList){
		this.colorList = colorList;
	}

}