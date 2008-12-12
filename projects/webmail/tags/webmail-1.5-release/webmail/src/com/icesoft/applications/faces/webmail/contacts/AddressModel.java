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
package com.icesoft.applications.faces.webmail.contacts;

/**
 * AddressModel is used to represent different address infomation
 *
 * @since 1.0
 */

public class AddressModel extends GenericContactAttribute {

    private String type;
    private String city;
    private String country;
    private String province;
    private String street;
    private String postcode;

    /**
     * constructor that create an AddressModel instance
     * from given type string
     *
     * @param defaultType
     */
    public AddressModel(String defaultType) {
        super();
        type = defaultType;
    }

    /**
     * default constructor
     */
    public AddressModel() {
        super();
    }


    /**
     * getter method of city member variable
     *
     * @return city string
     */
    public String getCity() {
        return city;
    }

    /**
     * setter method of city member variable
     *
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }


    /**
     * getter method of country member variable
     *
     * @return country string
     */
    public String getCountry() {
        return country;
    }

    /**
     * setter method of coutry member variable
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * getter method of postcode member variable
     *
     * @return postcode string
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * setter method of postcode member variable
     *
     * @param postcode
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * getter method of province member variable
     *
     * @return province string
     */
    public String getProvince() {
        return province;
    }

    /**
     * setter method of province member variable
     *
     * @param province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * getter method of street member variable
     *
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * setter method of street member variable
     *
     * @param street
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * getter method of type member variable
     *
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * setter method of type member variable
     * @param type
     */
    public void setType(String type) {
        this.type = type;
	}

}