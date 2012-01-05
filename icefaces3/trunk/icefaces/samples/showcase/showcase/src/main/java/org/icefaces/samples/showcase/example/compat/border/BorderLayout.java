/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.compat.border;

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
        parent = BorderBean.BEAN_NAME,
        title = "example.compat.border.layout.title",
        description = "example.compat.border.layout.description",
        example = "/resources/examples/compat/border/borderLayout.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="borderLayout.xhtml",
                    resource = "/resources/examples/compat/"+
                               "border/borderLayout.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="BorderLayout.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/border/BorderLayout.java")
        }
)
@ManagedBean(name= BorderLayout.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class BorderLayout extends ComponentExampleImpl<BorderLayout> implements Serializable {
	
	public static final String BEAN_NAME = "borderLayout";
	
	private String[] availableLayouts = new String[] {
	    "Default",
	    "Center Only",
	    "Horizontal Reverse", "Vertical Reverse",
	    "Hide North", "Hide South", "Hide East", "Hide West"
	};
	private String layout = availableLayouts[0];
	
	public BorderLayout() {
		super(BorderLayout.class);
	}
	
	public String[] getAvailableLayouts() { return availableLayouts; }
	public String getLayout() { return layout; }
	
	public void setLayout(String layout) { this.layout = layout; }
}
