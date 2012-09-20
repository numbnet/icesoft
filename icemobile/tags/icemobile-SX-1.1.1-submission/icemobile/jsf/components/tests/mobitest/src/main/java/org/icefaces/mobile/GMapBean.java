package org.icefaces.mobile;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = GMapBean.BEAN_NAME)
@SessionScoped
public class GMapBean implements Serializable {

	public static final String BEAN_NAME = "gMapBean";

	private double latitude = 0.0;
    private double longitude = 0.0;
    private double altitude = 0.0;
    private double direction = 0.0;
    private int zoom = 4;
    private String type = "map";
    
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
    	this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
    	this.latitude = latitude;
    }

    public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	public double getDirection() {
		return direction;
	}

	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getZoom() {
        return zoom;
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
    }


}
