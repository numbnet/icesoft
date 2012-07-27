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
    private String latitude;
    private String longitude;
    private String altitude;
    private String direction;

    @ModelAttribute("geolocationBean")
    public GeolocationBean createBean() {
        return new GeolocationBean();
    }

    public String getGeo1() {
        return location;
    }

    public void setGeo1(String location) {
        this.location = location;
        try {
            String[] parts = location.split(",");
            latitude = parts[0];
            longitude = parts[1];
            altitude = parts[2];
            direction = parts[3];
        } catch (Exception e)  {
            //always expect four comma-separtated parts
        }
    }

    public String getLat() {
        return latitude;
    }

    public String getLon() {
        return longitude;
    }

    public String getAlt() {
        return altitude;
    }

    public String getDir() {
        return direction;
    }

}
