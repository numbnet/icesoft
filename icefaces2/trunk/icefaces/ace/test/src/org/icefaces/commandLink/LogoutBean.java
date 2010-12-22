package org.icefaces.commandLink;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PortableRenderer;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;


@ManagedBean (name="logoutBean")
@ViewScoped
public class LogoutBean implements Serializable {


    public LogoutBean() {
    }

    public String logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sess = (HttpSession) ec.getSession(false);
        sess.invalidate();
        return "logout";
    }

    public String addToGroup() {
        PushRenderer.addCurrentView("myGroup");
        return "";
    }

    public String renderGroup() {
        PushRenderer.render( "myGroup" );

        final PortableRenderer pr = PushRenderer.getPortableRenderer();

        new Thread( new Runnable() {
            public void run() {
                try {
                    System.out.println("OK GOING TO SLEEP");
                    synchronized(Thread.currentThread()) {
                    Thread.currentThread().wait(10000);
                    } 
                } catch (Exception e) {
                    System.out.println("e");
                }
                System.out.println("RENDERING");
                pr.render("myGroup");
                System.out.println("DONE");                
            }
        }).start(); 

        return "";
    } 
}