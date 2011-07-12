package org.icefaces.samples.showcase.example.compat.calendar;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = CalendarBean.BEAN_NAME,
        title = "example.compat.calendar.pattern.title",
        description = "example.compat.calendar.pattern.description",
        example = "/resources/examples/compat/calendar/pattern.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="pattern.xhtml",
                    resource = "/resources/examples/compat/"+
                               "calendar/pattern.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CalendarPatternBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/calendar/CalendarPatternBean.java")
        }
)
@ManagedBean(name= CalendarPatternBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CalendarPatternBean extends ComponentExampleImpl<CalendarPatternBean> implements Serializable {
	
	public static final String BEAN_NAME = "calendarPattern";
	
	private static final String TYPE_PATTERN = "Pattern";
	private static final String TYPE_DATESTYLE = "Date Style";
	private static final String TYPE_TIMESTYLE = "Time Style";
	
	private Date date = CalendarBean.getDefaultDate();
	private boolean reloadRequired = false;
	private String patternType = TYPE_PATTERN;
	
	// Selected backings for the radio buttons
	private String optionPattern = CalendarBean.DEFAULT_PATTERN;
	private String optionDateStyle = "default";
	private String optionTimeStyle = "default";
	
	// Available radio button options 
	private String[] patternTypes = new String[] { TYPE_PATTERN,
	                                               TYPE_DATESTYLE,
	                                               TYPE_TIMESTYLE };
	private String[] patternOptions = new String[] { "MM/dd/yyyy",
                                                     "dd/MM/yyyy",
	                                                 "MMMM/dd/yy",
	                                                 "E, MMMM yyyy",
	                                                 "MM/dd/yy hh:mma",
	                                                 "kk:mm:ss:SS" };
    private String[] styleOptions = new String[] { "default",
                                                   "short",
                                                   "medium",
                                                   "long",
                                                   "full" };

	public CalendarPatternBean() {
		super(CalendarPatternBean.class);
	}
	
	public Date getDate() { return date; }
	
	public String getOptionPattern() { return optionPattern; }
	public String getOptionDateStyle() { return optionDateStyle; }
	public String getOptionTimeStyle() { return optionTimeStyle; }
	public String getPatternType() { return patternType; }
	
	public String[] getPatternTypes() { return patternTypes; }
	public String[] getPatternOptions() { return patternOptions; }
	public String[] getStyleOptions() { return styleOptions; }
	
	public boolean getTypePattern() { return TYPE_PATTERN.equals(patternType); }
	public boolean getTypeDateStyle() { return TYPE_DATESTYLE.equals(patternType); }
	public boolean getTypeTimeStyle() { return TYPE_TIMESTYLE.equals(patternType); }
	
	public void setDate(Date date) { this.date = date; }
	public void setOptionPattern(String optionPattern) { this.optionPattern = optionPattern; }
	public void setOptionDateStyle(String optionDateStyle) { this.optionDateStyle = optionDateStyle; }
	public void setOptionTimeStyle(String optionTimeStyle) { this.optionTimeStyle = optionTimeStyle; }
	public void setPatternType(String patternType) { this.patternType = patternType; }
	
	/**
	 * Method called when a reload is required
	 * This happens if the converter is modified by the radio buttons in the page
	 */
	public void reloadChanges(ValueChangeEvent event) {
	    reloadRequired = true;
	}
	
	/**
	 * Method to apply our selected changed to the calendar
	 * In some cases a page reload is required, so we'll use the NavigationController
	 *  to achieve that
	 * This is because the f:converter cannot be dynamically updated and requires a full refresh
	 */
	public void applyChanges(ActionEvent event) {
	    if (reloadRequired) {
	        NavigationController.refreshPage();
	    }
	    reloadRequired = false;
	}
}
