/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

/**
 * PhoneModel class represents an individual phone number. 
 *
 * @since 1.0
 */
public class PhoneModel extends GenericContactAttribute {

    //type of the phone number
    private String type;
    //value of the phone number
    private String value;

    public PhoneModel(String defaultType) {
        super();
        type = defaultType;
    }

    public PhoneModel() {
        super();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


}