package com.icesoft.icefaces.samples.showcase.components.gmap;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.component.gmap.GMapLatLng;

public class Bean {
	private String geoCoderAddress;
	private String address = "";
	private boolean locateAddress = false;
	private List points = new ArrayList();
	
	public Bean() {
		points.add(new GMapLatLng("37.379434", "-121.02203"));
		points.add(new GMapLatLng("33.845449", "-84.368682"));
		points.add(new GMapLatLng("34.05333", "-118.24499"));
		points.add(new GMapLatLng("33.072694", "-97.06234"));
        points.add(new GMapLatLng("37.391278", "-121.952451"));
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public void enterKeyPressed(ActionEvent event) {
		locateAddress = true;
	}
	
	
	public boolean isLocateAddress() {
		if (locateAddress) {
			locateAddress = false;
			return true;
		}
		return false;
	}
	
	public void findAddress(ValueChangeEvent event) {
		locateAddress = true;
	}

	public List getPoints() {
		return points;
	}

	public void setPoints(List points) {
		this.points = points;
	}

	public String getGeoCoderAddress() {
		return geoCoderAddress;
	}

	public void setGeoCoderAddress(String geoCoderAddress) {
		this.geoCoderAddress = geoCoderAddress;
	}
}
