package org.icemobile.samples.mediacast;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;
import org.icefaces.application.PushMessage;

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
        portableRenderer.render("mobi", new PushMessage("SMSBean Test Message", "This is a message from an SMSBean sent @ " + System.currentTimeMillis()));
        System.out.println("SMSBean.push: sent PushMessage");
    }

}
