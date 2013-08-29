/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobile;

import java.io.Serializable;
import java.lang.Double;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;


/**
 * <p>used for geolocation and for augmentedReality</p>
 */
@ManagedBean(name="positionBean")
@SessionScoped
public class PositionBean implements Serializable {

    private Double latitude = 0.00;
    private Double longitude=0.00;
    private Double altitude = 0.00;
    private boolean singleSubmit=false;
    private int errorCode = -1;
    private List<LocationItem> locations = new ArrayList<LocationItem>();

    public PositionBean(){
        locations.add(new LocationItem("Calgary", 51.040733, -114.079556, 1048.0000, null));
        locations.add(new LocationItem("New York", 43.658813, -79.487382, 1.0000, null));
        locations.add(new LocationItem("Edmonton", 53.538198, -113.502964, 668.0000, null ));
    }
 
    public void submitForm(ActionEvent event){
    	//System.out.println("submitting form");
    }

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

    public boolean isSingleSubmit() {
        return singleSubmit;
    }

    public Double getAltitude() {
        return altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public void setSingleSubmit(boolean singleSubmit) {
        this.singleSubmit = singleSubmit;
    }
    public void clear(ActionEvent ae){
        this.latitude=0.00;
        this.longitude=0.00;
        this.errorCode = -1;
        this.altitude = 0.00;
    }

    public List<LocationItem> getLocations() {
        return locations;
    }

    public void setLocations(List<LocationItem> locations) {
        this.locations = locations;
    }

    public class LocationItem implements Serializable {
        private String label;
        private Double lat;
        private Double lon;
        private Double alt;
        private String icon;

        public LocationItem(String label, Double lat, Double lon, Double alt, String icon){
            this.label=label;
            this.lat = lat;
            this.lon = lon;
            this.alt = alt;
            this.icon = icon;
        }

        public String getLabel(){
             return this.label;
        }
        public void setLabel(String inlab){
            this.label=inlab;
        }

        public Double getLat() {
            return lat;
        }

        public void setLat(Double lat) {
            this.lat = lat;
        }

        public Double getLon() {
            return lon;
        }

        public void setLon(Double lon) {
            this.lon = lon;
        }

        public Double getAlt() {
            return alt;
        }

        public void setAlt(Double alt) {
            this.alt = alt;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }
}
