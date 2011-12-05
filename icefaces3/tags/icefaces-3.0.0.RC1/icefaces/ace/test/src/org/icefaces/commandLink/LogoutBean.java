/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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


    final String myGroup = "myGroup"; 

    public LogoutBean() {
    }

    public String logout() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sess = (HttpSession) ec.getSession(false);
        sess.invalidate();
        return "logout";
    }

     public String logoutViaNavigationOutcome() {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        HttpSession sess = (HttpSession) ec.getSession(false);
        sess.invalidate();
        return "logout";
    }

    public String logoutViaImplicitNavigation() {
           FacesContext fc = FacesContext.getCurrentInstance();
           ExternalContext ec = fc.getExternalContext();

           HttpSession sess = (HttpSession) ec.getSession(false);
           sess.invalidate();
           return "login.html";
       }


    public String addViewToGroup() {
        PushRenderer.addCurrentView(myGroup);
        return "";
    }

    public String addSessionToGroup() {
        PushRenderer.addCurrentSession(myGroup);
        return "";
    }


    public String renderGroup() {

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
                pr.render(myGroup);
                System.out.println("DONE");                
            }
        }).start(); 

        return "";
    } 
}