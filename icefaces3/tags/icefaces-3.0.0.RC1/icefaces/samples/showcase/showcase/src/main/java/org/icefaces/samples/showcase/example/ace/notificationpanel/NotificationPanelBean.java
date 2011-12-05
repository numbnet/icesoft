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

package org.icefaces.samples.showcase.example.ace.notificationpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.notificationpanel.title",
        description = "example.ace.notificationpanel.description",
        example = "/resources/examples/ace/notificationpanel/notificationPanel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="notificationPanel.xhtml",
                    resource = "/resources/examples/ace/notificationpanel/notificationPanel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="NotificationPanel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/notificationpanel/NotificationPanelBean.java")
        }
)
@Menu(
	title = "menu.ace.notificationpanel.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.notificationpanel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = NotificationPanelBean.BEAN_NAME)
    }
)
@ManagedBean(name= NotificationPanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class NotificationPanelBean extends ComponentExampleImpl<NotificationPanelBean> implements Serializable {
    public static final String BEAN_NAME = "notificationPanelBean";
    
    private String imageLocation;
    private String imageDescription;
    private boolean render;
    
    public NotificationPanelBean() 
    {
        super(NotificationPanelBean.class);
        initializeBeanVariables();
    }
    
   public void showAppropriateButton(ActionEvent e)
   {
       if(render)
       { render = false; }
       else
       { render = true; }
   }

   private void initializeBeanVariables() 
    {
        imageLocation = "/resources/css/images/dragdrop/vwBeatle.png";
        imageDescription = "The Volkswagen Type 1, widely known as the Volkswagen Beetle and Volkswagen Bug, is an economy car"
                                + " produced by the German auto maker Volkswagen (VW) from 1938."
                                + " With over 21 million manufactured in an air-cooled, rear-engined, rear-wheel drive configuration,"
                                + " the Beetle is the longest-running and most-manufactured automobile of a single design platform anywhere in the world.";
        render = true;
    }
   
    public String getImageDescription() {
        return imageDescription;
    }

    public void setImageDescription(String imageDescription) {
        this.imageDescription = imageDescription;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }
}