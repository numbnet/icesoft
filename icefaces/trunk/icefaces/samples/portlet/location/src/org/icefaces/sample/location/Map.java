/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package org.icefaces.sample.location;

import java.util.List;
import java.util.ArrayList;

public class Map {

    private Coordinator coordinator;

    // address to search for
    private String geoCoderAddress = "";

    // city location selected from a preset list
    private String standardAddress = "";

    // whether we should search for an address or not
    private boolean locateAddress = false;

    private List points = new ArrayList();
    private boolean showControls = true;
    private boolean showMarkers = true;
    
    // value bound to the gmap component
    private String address = "";

    public Map() {
    }

    public String getStandardAddress() {
        return standardAddress;
    }

    public void setStandardAddress(String standardAddress) {
        this.standardAddress = standardAddress;
        this.address = standardAddress;
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

    public boolean isShowControls() {
        return showControls;
    }

    public void setShowControls(boolean showControls) {
        this.showControls = showControls;
    }

    public boolean isShowMarkers() {
        return showMarkers;
    }

    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
    }

    public String getAddress() {
        City currentCity = coordinator.getCurrentCity();

        if( currentCity == null ){
            return "";
        }

        return currentCity.toGMapAddress();
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinator getCoordinator() {
        return coordinator;
    }

    public void setCoordinator(Coordinator coordinator) {
        this.coordinator = coordinator;
    }
}
