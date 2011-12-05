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

package org.icefaces.samples.showcase.example.compat.calendar;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        title = "example.compat.calendar.title",
        description = "example.compat.calendar.description",
        example = "/resources/examples/compat/calendar/calendar.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="calendar.xhtml",
                    resource = "/resources/examples/compat/"+
                               "calendar/calendar.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CalendarBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/calendar/CalendarBean.java")
        }
)
@Menu(
	title = "menu.compat.calendar.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.calendar.subMenu.main",
                    isDefault = true,
                    exampleBeanName = CalendarBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.calendar.subMenu.popup",
                    exampleBeanName = CalendarPopupBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.calendar.subMenu.pattern",
                    exampleBeanName = CalendarPatternBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.calendar.subMenu.timezone",
                    exampleBeanName = CalendarTimezoneBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.calendar.subMenu.highlight",
                    exampleBeanName = CalendarHighlightBean.BEAN_NAME)
})
@ManagedBean(name= CalendarBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CalendarBean extends ComponentExampleImpl<CalendarBean> implements Serializable {
	
	public static final String BEAN_NAME = "calendar";
	public static final String DEFAULT_PATTERN = "MM/dd/yyyy"; 
	
	private Date date = getDefaultDate();

	public CalendarBean() {
		super(CalendarBean.class);
	}
	
	public Date getDate() { return date; }
	public String getPattern() { return DEFAULT_PATTERN; }
	
	public void setDate(Date date) { this.date = date; }
	
	/**
	 * Convenience method to get a default current date
	 */
	public static Date getDefaultDate() {
	    return getDefaultDate(TimeZone.getDefault());
	}
	
	/**
	 * Convenience method to get a default current date in the specified timezone
	 */
	public static Date getDefaultDate(TimeZone timezone) {
	    return Calendar.getInstance(timezone).getTime();
	}
}
