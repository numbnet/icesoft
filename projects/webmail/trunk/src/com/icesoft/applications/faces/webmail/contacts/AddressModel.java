/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
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