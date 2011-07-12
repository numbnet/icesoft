package org.icefaces.samples.showcase.example.compat.calendar;

import java.io.Serializable;
import java.util.Date;

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
        parent = CalendarBean.BEAN_NAME,
        title = "example.compat.calendar.popup.title",
        description = "example.compat.calendar.popup.description",
        example = "/resources/examples/compat/calendar/popup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="popup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "calendar/popup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CalendarPopupBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/calendar/CalendarPopupBean.java")
        }
)
@ManagedBean(name= CalendarPopupBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CalendarPopupBean extends ComponentExampleImpl<CalendarPopupBean> implements Serializable {
	
	public static final String BEAN_NAME = "calendarPopup";
	
	private Date date = CalendarBean.getDefaultDate();
	private boolean renderAsPopup = true;
	private boolean renderMonthAsDropdown = false;
	private boolean renderYearAsDropdown = false;

	public CalendarPopupBean() {
		super(CalendarPopupBean.class);
	}
	
	public Date getDate() { return date; }
	public String getPattern() { return CalendarBean.DEFAULT_PATTERN; }
	public boolean getRenderAsPopup() { return renderAsPopup; }
	public boolean getRenderMonthAsDropdown() { return renderMonthAsDropdown; }
	public boolean getRenderYearAsDropdown() { return renderYearAsDropdown; }
	
	public void setDate(Date date) { this.date = date; }
	public void setRenderAsPopup(boolean renderAsPopup) { this.renderAsPopup = renderAsPopup; }
	public void setRenderMonthAsDropdown(boolean renderMonthAsDropdown) { this.renderMonthAsDropdown = renderMonthAsDropdown; }
	public void setRenderYearAsDropdown(boolean renderYearAsDropdown) { this.renderYearAsDropdown = renderYearAsDropdown; }
}
