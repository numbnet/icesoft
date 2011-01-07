/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
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