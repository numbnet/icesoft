package com.icesoft.icefaces.samples.showcase.components.gmap;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.gmap.GMapLatLng;

public class Bean {
	private String geoCoderAddress;
	private String address;
	private boolean locateAddress = false;
	private boolean render = true;
	private List point = new ArrayList();
	
	public Bean() {
		point.add(new GMapLatLng("50.992255", "-114.071646"));
		point.add(new GMapLatLng("50.905381", "-114.066651"));
		point.add(new GMapLatLng("51.160609", "-114.064579"));
		point.add(new GMapLatLng("50.985599", "-114.031506"));
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
	
	public void findAddress(ActionEvent event) {
		this.address = ((UICommand)event.getComponent()).getValue().toString();
		locateAddress = true;
	}

	public boolean isRender() {
		return render;
	}

	public void setRender(boolean render) {
		this.render = render;
	}
	
	public void toggle(ActionEvent event) {
		render = !render;
	}

	public List getPoint() {
		return point;
	}

	public void setPoint(List point) {
		this.point = point;
	}

	public String getGeoCoderAddress() {
		return geoCoderAddress;
	}

	public void setGeoCoderAddress(String geoCoderAddress) {
		this.geoCoderAddress = geoCoderAddress;
	}
}
