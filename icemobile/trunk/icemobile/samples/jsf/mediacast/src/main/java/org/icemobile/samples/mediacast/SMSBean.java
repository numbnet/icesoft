package org.icemobile.samples.mediacast;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ManagedBean(name = "smsBean")
@ViewScoped
public class SMSBean implements Serializable {

    PortableRenderer portableRenderer;

    public SMSBean() {
    }

    @PostConstruct
   	public void init() {
   		portableRenderer = PushRenderer.getPortableRenderer();
        System.out.println("SMSBean.init: portableRenderer = " + portableRenderer);
    }

    public void push(ActionEvent event){
        portableRenderer.render("mobi");
    }

}
