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
