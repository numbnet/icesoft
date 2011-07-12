package org.icefaces.samples.showcase.example.compat.popup;

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
        parent = PopupBean.BEAN_NAME,
        title = "example.compat.popup.position.title",
        description = "example.compat.popup.position.description",
        example = "/resources/examples/compat/popup/popupPosition.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="popupPosition.xhtml",
                    resource = "/resources/examples/compat/"+
                               "popup/popupPosition.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PopupPosition.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/popup/PopupPosition.java")
        }
)
@ManagedBean(name= PopupPosition.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PopupPosition extends ComponentExampleImpl<PopupPosition> implements Serializable {
	
	public static final String BEAN_NAME = "popupPosition";
	
	private boolean opened = false;
	private int coordinateX = 10;
	private int coordinateY = 200;
	
	public PopupPosition() {
		super(PopupPosition.class);
	}
	
	public boolean isOpened() { return opened; }
	public int getCoordinateX() { return coordinateX; }
	public int getCoordinateY() { return coordinateY; }
	
	public String getCoordinates() {
	    return coordinateX + "," + coordinateY;
	}
	
	public void setOpened(boolean opened) { this.opened = opened; }
	public void setCoordinateX(int coordinateX) { this.coordinateX = coordinateX; }
	public void setCoordinateY(int coordinateY) { this.coordinateY = coordinateY; }
	
	public void toggleOpened(ActionEvent event) {
	    opened = !opened;
	}
	
	public void openEvent(ActionEvent event) {
	    opened = true;
	}
	
	public void closeEvent(ActionEvent event) {
	    opened = false;
	}
}
