/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

/**
 * EmailModel class represents an individual email address of the contact
 *
 * @since 1.0
 */
public class EmailModel extends GenericContactAttribute {

    //type of email address
    private String type;
    //value of email address
    private String value;

    public EmailModel(String defaultType) {
        super();
        type = defaultType;
    }

    public EmailModel() {
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