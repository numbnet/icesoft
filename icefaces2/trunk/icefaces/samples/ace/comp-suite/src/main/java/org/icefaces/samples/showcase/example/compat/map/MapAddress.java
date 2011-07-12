package org.icefaces.samples.showcase.example.compat.map;

import java.io.Serializable;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.util.FacesUtils;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.address.title",
        description = "example.compat.map.address.description",
        example = "/resources/examples/compat/map/mapAddress.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapAddress.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapAddress.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapAddress.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapAddress.java")
        }
)
@ManagedBean(name= MapAddress.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapAddress extends ComponentExampleImpl<MapAddress> implements Serializable {
	
	public static final String BEAN_NAME = "mapAddress";
	private String from;
	private String to;
	private boolean showTextDirections = true;
	private boolean locateAddress = false;
	private boolean showDirections = false;

	public MapAddress() {
		super(MapAddress.class);
	}
	
	public String getFrom() { return from; }
	public String getTo() { return to; }
	public boolean getShowTextDirections() { return showTextDirections; }
	public boolean getLocateAddress() {
	    if (locateAddress) {
	        locateAddress = false;
	        
	        return true;
	    }
	    
	    return locateAddress;
	}
	public boolean getShowDirections() { return showDirections; }
	
	public void setFrom(String from) { this.from = from; }
	public void setTo(String to) { this.to = to; }
	public void setShowTextDirections(boolean showTextDirections) { this.showTextDirections = showTextDirections; }
	public void setLocateAddress(boolean locateAddress) { this.locateAddress = locateAddress; }
	public void setShowDirections(boolean showDirections) { this.showDirections = showDirections; }
	
	public void lookup(ActionEvent event) {
	    if (FacesUtils.isBlank(from)) {
	        if (!FacesUtils.isBlank(to)) {
                from = new String(to);
                to = null;
            }
            else {
                from = MapBean.DEFAULT_ADDRESS;
                to = null;
            }
	    }
	    
	    locateAddress = true;
	    showDirections = !FacesUtils.isBlank(to);
	}
}
