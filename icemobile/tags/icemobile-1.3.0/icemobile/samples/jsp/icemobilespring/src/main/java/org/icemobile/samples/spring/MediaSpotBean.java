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

import java.util.Map;

public class MediaSpotBean {

    private String title;
    private String location;
    private String selection;
    private String fileName;
    private String latitude;
    private String longitude;
    private String altitude;
    private String direction;
    

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSelection() {
        return selection;
    }

    public void setSelection(String selection) {
        this.selection = selection;
    }

    public String getLocation() {
        return location;
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

    public void setLocation(String location) {
        try {
            this.location = location;
            String[] locationParts = location.split(",");
            latitude = locationParts[0];
            longitude = locationParts[1];
            altitude = locationParts[2];
            direction = locationParts[3];
        } catch (Exception e)  {
            //allow empty split parts
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("properties ");
        sb.append("title=");
        sb.append("'").append(title).append("', ");
        sb.append("location=");
        sb.append("'").append(location).append("', ");
        return sb.toString();
    }
}
