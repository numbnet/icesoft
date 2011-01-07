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
