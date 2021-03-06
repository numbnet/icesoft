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

package org.icefaces.samples.showcase.example.compat.series;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = SeriesBean.BEAN_NAME,
        title = "example.compat.series.content.title",
        description = "example.compat.series.content.description",
        example = "/resources/examples/compat/series/seriesContent.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="seriesContent.xhtml",
                    resource = "/resources/examples/compat/"+
                               "series/seriesContent.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="SeriesContent.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/series/SeriesContent.java")
        }
)
@ManagedBean(name= SeriesContent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class SeriesContent extends ComponentExampleImpl<SeriesContent> implements Serializable {
	
	public static final String BEAN_NAME = "seriesContent";
	
	private static final String TYPE_CALENDAR = "Calendars";
	private static final String TYPE_AREA = "Text Areas";
	private static final String TYPE_TEXT = "Example Text";
	private static final String TYPE_IMAGES = "Images";
	
	private String[] items = new String[] {
	    "1", "2", "3", "4", "5"
	};
	private String[] availableTypes = new String[] {
	    TYPE_CALENDAR,
	    TYPE_AREA,
	    TYPE_TEXT,
	    TYPE_IMAGES
	};
	private String type = availableTypes[0];
	
	public SeriesContent() {
		super(SeriesContent.class);
	}
	
	public boolean getTypeCalendar() { return TYPE_CALENDAR.equals(type); }
	public boolean getTypeArea() { return TYPE_AREA.equals(type); }
	public boolean getTypeText() { return TYPE_TEXT.equals(type); }
	public boolean getTypeImages() { return TYPE_IMAGES.equals(type); }
	
	public String[] getItems() { return items; }
	public String[] getAvailableTypes() { return availableTypes; }
	public String getType() { return type; }
	
	public void setType(String type) { this.type = type; }
}
