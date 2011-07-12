package org.icefaces.samples.showcase.example.compat.map;

import java.io.Serializable;
import java.util.Random;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.zoom.title",
        description = "example.compat.map.zoom.description",
        example = "/resources/examples/compat/map/mapZoom.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapZoom.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapZoom.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapZoom.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapZoom.java")
        }
)
@ManagedBean(name= MapZoom.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapZoom extends ComponentExampleImpl<MapZoom> implements Serializable {
	
	public static final String BEAN_NAME = "mapZoom";
	
	private Random randomizer;
	private String[] availableLevels = generateLevels(21);
	private String zoomLevel = availableLevels[3];

	public MapZoom() {
		super(MapZoom.class);
	}
	
	public String getZoomLevel() { return zoomLevel; }
	public String[] getAvailableLevels() { return availableLevels; }
	
	public void setZoomLevel(String zoomLevel) { this.zoomLevel = zoomLevel; }
	
	private String[] generateLevels(int max) {
	    String[] toReturn = new String[max+1];
	    
	    for (int i = 0; i <= max; i++) {
	        toReturn[i] = String.valueOf(i);
	    }
	    
	    return toReturn;
	}
	
    public void applyChanges(ActionEvent event) {
        NavigationController.refreshPage();
    }
    
    public void randomZoom(ActionEvent event) {
        if (randomizer == null) {
            randomizer = new Random(System.nanoTime());
        }
        
        setZoomLevel(availableLevels[randomizer.nextInt(availableLevels.length)]);
        applyChanges(event);
    }
}
