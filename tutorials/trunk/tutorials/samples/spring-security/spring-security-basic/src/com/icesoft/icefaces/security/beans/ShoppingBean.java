package com.icesoft.icefaces.security.beans;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;


public class ShoppingBean {

    // properties
    private String productId;

    /**
     * default empty constructor
     */
    public ShoppingBean() {
    }

    /**
     * Method that is backed to a submit button of a form.
     */
    public String send() {
        return ("success");
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void logout(ActionEvent e) throws java.io.IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect("/spring-authentication/j_spring_security_logout");
    }
}