/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.application.showcase.view.bean.examples.component.selectInputText;

import java.io.Serializable;

/**
 * <p>The City class is used to store city information for the Autocomplete
 * (selectInputText) example. The CityDictionary class builds a large List of
 * these objects which is in turn used for the auto complete lookup</p>
 *
 * @see org.icefaces.application.showcase.view.bean.examples.component.selectInputText.CityDictionary
 * @since 1.5
 */
public class City implements Serializable {

    // attributes of each entry
    private String city;
    private String state;
    private String zip;
    private String areaCode;
    private String country;
    private String stateCode;

    /**
     * Creates a new instance of a city object.  All String attributes are empty
     */
    public City() {
        this("", "", "", "", "", "");
    }

    /**
     * Creates a new instance of a city object.
     *
     * @param city      name of city
     * @param state     name of state city resides in
     * @param zip       zip code of city
     * @param areaCode  area code of city
     * @param country   country name that city resides in
     * @param stateCode state name that city resides in
     */
    public City(String city, String state, String zip, String areaCode,
                String country, String stateCode) {
        this.city = city;
        this.state = state;
        this.zip = zip;
        this.areaCode = areaCode;
        this.country = country;
        this.stateCode = stateCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateCode() {
        //only add parentheses if the two letter state code is present
        if (stateCode == null) {
            return stateCode;
        } else if (stateCode.equals("")) {
            return stateCode;
        } else {
            return "(" + stateCode + ")";
        }
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

}