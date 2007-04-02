/**
 * Copyright (C) 2006, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail.contacts;

/**
 * WebpageModel class represents an individual webpage.
 *
 * @since 1.0
 */
public class WebpageModel extends GenericContactAttribute {

    //type of webpage
    private String type;
    //value of webpage
    private String value;

    public WebpageModel(String defaultType) {
        super();
        type = defaultType;
    }

    public WebpageModel() {
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