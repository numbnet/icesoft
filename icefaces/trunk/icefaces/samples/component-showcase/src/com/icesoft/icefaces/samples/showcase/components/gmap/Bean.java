package com.icesoft.icefaces.samples.showcase.components.gmap;

import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UICommand;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import com.icesoft.faces.component.gmap.GMapLatLng;

public class Bean {
	private String address;
	private boolean locateAddress = false;
	private String from;
	private String to;
	private String from2;
	private String to2;
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

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
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

	public String getFrom2() {
		return from2;
	}

	public void setFrom2(String from2) {
		this.from2 = from2;
	}

	public String getTo2() {
		return to2;
	}

	public void setTo2(String to2) {
		this.to2 = to2;
	}
}
