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

package org.icefaces.samples.showcase.example.compat.tooltip;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

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
        title = "example.compat.tooltip.title",
        description = "example.compat.tooltip.description",
        example = "/resources/examples/compat/tooltip/tooltip.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="tooltip.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tooltip/tooltip.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TooltipBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tooltip/TooltipBean.java")
        }
)
@Menu(
	title = "menu.compat.tooltip.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.tooltip.subMenu.main",
                    isDefault = true,
                    exampleBeanName = TooltipBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.subMenu.display",
                    exampleBeanName = TooltipDisplay.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.subMenu.hide",
                    exampleBeanName = TooltipHide.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.subMenu.draggable",
                    exampleBeanName = TooltipDraggable.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.subMenu.delay",
                    exampleBeanName = TooltipDelay.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.subMenu.move",
                    exampleBeanName = TooltipMove.BEAN_NAME),
            @MenuLink(title = "menu.compat.tooltip.subMenu.preview",
                    exampleBeanName = TooltipPreview.BEAN_NAME)
})
@ManagedBean(name= TooltipBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TooltipBean extends ComponentExampleImpl<TooltipBean> implements Serializable {
	
	public static final String BEAN_NAME = "tooltip";
	
	public static final List<PeriodicElement> ELEMENTS = generateElements();
	
	public TooltipBean() {
		super(TooltipBean.class);
	}
	
	public List<PeriodicElement> getElements() { return ELEMENTS; }
	
	private static List<PeriodicElement> generateElements() {
	    List<PeriodicElement> toReturn = new ArrayList<PeriodicElement>(5);
	    
	    toReturn.add(new PeriodicElement(2, "He", "Helium",
	                                     -272.05, -458.0));
	    toReturn.add(new PeriodicElement(8, "O", "Oxygen",
	                                     -222.65, -368.77));
	    toReturn.add(new PeriodicElement(50, "Sn", "Tin",
	                                     232.06, 449.71));
	    toReturn.add(new PeriodicElement(92, "U", "Uranium",
	                                     1132.0, 2070.0));
	    toReturn.add(new PeriodicElement(6, "C", "Carbon",
	                                     3500.0, 6332.0));
	    
	    return toReturn;
	}
}
