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
        title = "example.compat.popup.center.title",
        description = "example.compat.popup.center.description",
        example = "/resources/examples/compat/popup/popupCenter.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="popupCenter.xhtml",
                    resource = "/resources/examples/compat/"+
                               "popup/popupCenter.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PopupCenter.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/popup/PopupCenter.java")
        }
)
@ManagedBean(name= PopupCenter.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PopupCenter extends ComponentExampleImpl<PopupCenter> implements Serializable {
	
	public static final String BEAN_NAME = "popupCenter";
	
	private boolean opened = false;
	private boolean autoCentre = true;
	
	public PopupCenter() {
		super(PopupCenter.class);
	}

	public boolean isOpened() { return opened; }
	public boolean getAutoCentre() { return autoCentre; }
	
	public void setOpened(boolean opened) { this.opened = opened; }
	public void setAutoCentre(boolean autoCentre) { this.autoCentre = autoCentre; }
	
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
