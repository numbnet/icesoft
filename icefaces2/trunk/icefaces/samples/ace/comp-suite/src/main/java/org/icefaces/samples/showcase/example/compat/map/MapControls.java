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
        parent = MapBean.BEAN_NAME,
        title = "example.compat.map.controls.title",
        description = "example.compat.map.controls.description",
        example = "/resources/examples/compat/map/mapControls.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="mapControls.xhtml",
                    resource = "/resources/examples/compat/"+
                               "map/mapControls.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapControls.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/map/MapControls.java")
        }
)
@ManagedBean(name= MapControls.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapControls extends ComponentExampleImpl<MapControls> implements Serializable {
	
	public static final String BEAN_NAME = "mapControls";
	
	private boolean smallMap = false;
	private boolean largeMap = true;
	private boolean zoom = false;
	private boolean scale = true;
	private boolean type = false;
	private boolean overview = false;
	
	public MapControls() {
		super(MapControls.class);
	}
	
	public boolean getSmallMap() { return smallMap; }
	public boolean getLargeMap() { return largeMap; }
	public boolean getZoom() { return zoom; }
	public boolean getScale() { return scale; }
	public boolean getType() { return type; }
	public boolean getOverview() { return overview; }
	
	public void setSmallMap(boolean smallMap) { this.smallMap = smallMap; }
	public void setLargeMap(boolean largeMap) { this.largeMap = largeMap; }
	public void setZoom(boolean zoom) { this.zoom = zoom; }
	public void setScale(boolean scale) { this.scale = scale; }
	public void setType(boolean type) { this.type = type; }
	public void setOverview(boolean overview) { this.overview = overview; }
}
