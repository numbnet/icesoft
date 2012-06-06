package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("geolocationBean")
public class GeolocationBean {

    // Location
    private String location;

    @ModelAttribute("geolocationBean")
    public GeolocationBean createBean() {
        return new GeolocationBean();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLat() {
        if (location == null) return null;
	int delim = location.indexOf(',');
	if (delim == -1) return null;
	return location.substring(0,delim);
    }

    public void setLat(String lat) {
    }

    public String getLon() {
        if (location == null) return null;
	int delim = location.indexOf(',');
	if (delim == -1) return null;
	return location.substring(delim+1);
    }

    public void setLon(String lon) {
    }
}
