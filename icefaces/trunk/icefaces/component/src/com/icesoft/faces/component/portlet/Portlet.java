package com.icesoft.faces.component.portlet;

import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;

public class Portlet extends UINamingContainer {


    public String getClientId(FacesContext context) {

        String clientID = super.getClientId(context);
        System.out.println("Portlet.getClientId: " + clientID);

        return clientID;
    }


}
