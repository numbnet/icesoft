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

package org.icefaces.samples.showcase.example.compat.menuPopup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.FacesEvent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import com.icesoft.faces.component.DisplayEvent;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MenuPopupBean.BEAN_NAME,
        title = "example.compat.menuPopup.events.title",
        description = "example.compat.menuPopup.events.description",
        example = "/resources/examples/compat/menuPopup/menuPopupEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuPopupEvents.xhtml",
                    resource = "/resources/examples/compat/"+
                               "menuPopup/menuPopupEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuPopupEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/menuPopup/MenuPopupEvents.java")
        }
)
@ManagedBean(name= MenuPopupEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuPopupEvents extends ComponentExampleImpl<MenuPopupEvents> implements Serializable {
	
	public static final String BEAN_NAME = "menuPopupEvents";
	
	private static final int ROW_SIZE = 10;
	private static final String PARAM_NAME = "name";
	
	private List<String> eventLog = new ArrayList<String>(ROW_SIZE);
	
	public MenuPopupEvents() {
		super(MenuPopupEvents.class);
	}
	
	public int getRowSize() { return ROW_SIZE; }
	public String getParamName() { return PARAM_NAME; }
	public List<String> getEventLog() { return eventLog; }
	
	public void setEventLog(List<String> eventLog) { this.eventLog = eventLog; }
	
	public void displayListener(DisplayEvent event) {
	    addEvent(event, "Display");
	}
	
	public void actionListener(ActionEvent event) {
	    addEvent(event, FacesUtils.getRequestParameter(PARAM_NAME));
	}
	
	private void addEvent(FacesEvent event, String text) {
	    eventLog.add(0, "Fired event " + event.getClass() + " for '" + text + "'");
	    
	    // Cap the list at the displayed row size
	    if (eventLog.size() > ROW_SIZE) {
	        eventLog = eventLog.subList(0, ROW_SIZE);
	    }
	}
}
