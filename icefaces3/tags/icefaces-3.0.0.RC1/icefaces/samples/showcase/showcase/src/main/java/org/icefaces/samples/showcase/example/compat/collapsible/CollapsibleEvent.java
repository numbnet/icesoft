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

package org.icefaces.samples.showcase.example.compat.collapsible;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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
        parent = CollapsibleBean.BEAN_NAME,
        title = "example.compat.collapsible.event.title",
        description = "example.compat.collapsible.event.description",
        example = "/resources/examples/compat/collapsible/collapsibleEvent.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="collapsibleEvent.xhtml",
                    resource = "/resources/examples/compat/"+
                               "collapsible/collapsibleEvent.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CollapsibleEvent.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/collapsible/CollapsibleEvent.java")
        }
)
@ManagedBean(name= CollapsibleEvent.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CollapsibleEvent extends ComponentExampleImpl<CollapsibleEvent> implements Serializable {
	
	public static final String BEAN_NAME = "collapsibleEvent";
	private SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
	private Random randomizer = new Random(System.nanoTime());
	private String lastFired;
	private int lastNumber;
	
	public CollapsibleEvent() {
		super(CollapsibleEvent.class);
	}

                public String getLastFired() {
                    return lastFired;
                }

                public void setLastFired(String lastFired) {
                    this.lastFired = lastFired;
                }
	
	public void setLastNumber(int lastNumber) { this.lastNumber = lastNumber; }
        	public int getLastNumber() { return lastNumber; }
	
	public void actionListener(ActionEvent event) {
	    lastFired = formatter.format(new Date());
	    lastNumber = 1+randomizer.nextInt(99);
	}
}
