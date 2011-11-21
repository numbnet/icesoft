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

package org.icefaces.samples.showcase.example.ace.linkButton;


import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.linkButton.title",
        description = "example.ace.linkButton.description",
        example = "/resources/examples/ace/linkButton/linkbutton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="linkbutton.xhtml",
                    resource = "/resources/examples/ace/linkButton/linkbutton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="LinkButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/"+
                                "example/ace/linkButton/LinkButtonBean.java")
        }
)
@Menu(
	title = "menu.ace.linkButton.subMenu.title",
	menuLinks = 
                {
                    @MenuLink(title = "menu.ace.linkButton.subMenu.main", isDefault = true,
                                     exampleBeanName = LinkButtonBean.BEAN_NAME)
                }
)

@ManagedBean(name=LinkButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LinkButtonBean extends ComponentExampleImpl<LinkButtonBean> implements Serializable {

    public static final String BEAN_NAME = "linkButton";

    private String navActionValue = "overview";
    private String staticNavigation = "showcase.jsf?grp=aceMenu&exp=accordionPanelBean";
    private String listenerNavigation = "showcase.jsf?grp=aceMenu&exp=accordionPanelBean";
    private String requiredField =  "";

    public void addMessage() {
        FacesUtils.addInfoMessage("This button didn't submit or validate the rest of the form.");
    }

    public void navListener(ActionEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            FacesUtils.getExternalContext().dispatch(listenerNavigation);
        } catch (IOException e1) {
            FacesUtils.addErrorMessage("Path \""+listenerNavigation+"\" could not be read.");
            e1.printStackTrace();
        }
    }

    public String navAction() {
        if (navActionValue.equals("overview")) return "accordionPanelBean";
        if (navActionValue.equals("checkbox")) return "checkboxButton";
        if (navActionValue.equals("push")) return "pushButton";
        return "linkButton";
    }

    public String getNavAction() {
        return navAction();
    }

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    public void setNavAction(String navAction) {
        this.navActionValue = navAction;
    }

    public String getNavActionValue() {
        return navActionValue;
    }

    public void setNavActionValue(String navAction) {
        this.navActionValue = navAction;
    }

    public String getListenerNavigation() {
        return listenerNavigation;
    }

    public void setListenerNavigation(String listenerNavigation) {
        this.listenerNavigation = listenerNavigation;
    }

    public String getStaticNavigation() {
        return staticNavigation;
    }

    public void setStaticNavigation(String staticNavigation) {
        this.staticNavigation = staticNavigation;
    }

    public LinkButtonBean() {
        super(LinkButtonBean.class);
    }
}
