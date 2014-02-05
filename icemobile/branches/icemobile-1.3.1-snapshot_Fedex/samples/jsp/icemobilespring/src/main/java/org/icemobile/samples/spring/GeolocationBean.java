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

package org.icemobile.samples.spring;

/**
 * This is a sample backing bean for the MVC supported state.
 * The properties should be the same.
 */
public class GeolocationBean {

    // Location
    private String location;
    private String latitude;
    private String longitude;
    private String altitude;
    private String direction;

    private int timeout = 30;
    private int maximumAge = 3600;
    private String enableHighPrecision = "asNeeded";
    private boolean continuousUpdates = true;
    
    public String getGeolocation() {
        return location;
    }

    public void setGeolocation(String location) {
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getAltitude() {
        return altitude;
    }

    public String getDirection() {
        return direction;
    }
    

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getMaximumAge() {
        return maximumAge;
    }

    public void setMaximumAge(int maximumAge) {
        System.out.println("setMaximumAge()="+maximumAge);
        this.maximumAge = maximumAge;
    }

    public String getEnableHighPrecision() {
        return enableHighPrecision;
    }

    public void setEnableHighPrecision(String enableHighPrecision) {
        this.enableHighPrecision = enableHighPrecision;
    }

    public boolean isContinuousUpdates() {
        return continuousUpdates;
    }

    public void setContinuousUpdates(boolean continuousUpdates) {
        this.continuousUpdates = continuousUpdates;
    }

}
