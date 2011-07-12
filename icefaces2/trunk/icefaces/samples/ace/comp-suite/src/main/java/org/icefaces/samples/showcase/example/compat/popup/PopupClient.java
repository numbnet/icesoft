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
        title = "example.compat.popup.client.title",
        description = "example.compat.popup.client.description",
        example = "/resources/examples/compat/popup/popupClient.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="popupClient.xhtml",
                    resource = "/resources/examples/compat/"+
                               "popup/popupClient.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PopupClient.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/popup/PopupClient.java")
        }
)
@ManagedBean(name= PopupClient.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PopupClient extends ComponentExampleImpl<PopupClient> implements Serializable {
	
	public static final String BEAN_NAME = "popupClient";
	
	private boolean opened = false;
	private boolean clientOnly = true;
	
	public PopupClient() {
		super(PopupClient.class);
	}
	
	public boolean isOpened() { return opened; }
	public boolean getClientOnly() { return clientOnly; }
	
	public void setOpened(boolean opened) { this.opened = opened; }
	public void setClientOnly(boolean clientOnly) { this.clientOnly = clientOnly; }
	
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
