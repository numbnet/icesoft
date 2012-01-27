package org.icefaces.mobile;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;

@ManagedBean(name="messagingHub")
@ApplicationScoped
public class MessagingHub implements Serializable  {
    private String message = "";

    public MessagingHub() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
