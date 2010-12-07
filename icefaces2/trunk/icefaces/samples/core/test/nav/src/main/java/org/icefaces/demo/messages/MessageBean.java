package org.icefaces.demo.messages;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ActionListener;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.application.FacesMessage;
import javax.annotation.PostConstruct;

@ManagedBean
@RequestScoped
public class MessageBean {

    @PostConstruct
    public void constructionMessage(){
        System.out.println("MessageBean.constructionMessage: CALLED");
        FacesContext ctx = FacesContext.getCurrentInstance();
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Message added via PostConstruct: " + ctx,
                                        "Message added via PostConstruct: " + ctx));
    }

    public void addMessages(ActionEvent ae) {

        FacesContext ctx = FacesContext.getCurrentInstance();
        UIComponent c = UIComponent.getCurrentComponent(ctx);
        ctx.addMessage(null,
                       new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                        "Message added via actionListener: " + c.getClientId(ctx),
                                        "Message added via actionListener: " + c.getClientId(ctx)));
    }

}
