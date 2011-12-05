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

package org.icefaces.samples.showcase.example.compat.map;

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
        title = "example.compat.map.title",
        description = "example.compat.map.description",
        example = "/resources/examples/compat/map/map.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="map.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/map.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapBean.java")
        }
)
@Menu(
	title = "menu.compat.map.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.map.subMenu.main",
                    isDefault = true,
                    exampleBeanName = MapBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.subMenu.address",
                    exampleBeanName = MapAddress.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.subMenu.latlong",
                    exampleBeanName = MapLatLong.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.subMenu.controls",
                    exampleBeanName = MapControls.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.subMenu.zoom",
                    exampleBeanName = MapZoom.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.subMenu.type",
                    exampleBeanName = MapType.BEAN_NAME),
            @MenuLink(title = "menu.compat.map.subMenu.load",
                    exampleBeanName = MapLoad.BEAN_NAME)
})
@ManagedBean(name= MapBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapBean extends ComponentExampleImpl<MapBean> implements Serializable {
	
	public static final String BEAN_NAME = "map";
	public static final String DEFAULT_ADDRESS = "Calgary, Alberta, Canada";
	
	private String address = DEFAULT_ADDRESS;
	private boolean locateAddress = true;

	public MapBean() {
		super(MapBean.class);
	}
	
	public String getAddress() { return address; }
	public boolean getLocateAddress() {
	    if (locateAddress) {
	        locateAddress = false;
	        
	        return true;
	    }
	    
	    return locateAddress;
	}
	
	public void setAddress(String address) { this.address = address; }
	public void setLocateAddress(boolean locateAddress) { this.locateAddress = locateAddress; }
	
	public void lookup(ActionEvent event) {
	    locateAddress = true;
	}
}
