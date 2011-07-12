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
        title = "example.compat.popup.title",
        description = "example.compat.popup.description",
        example = "/resources/examples/compat/popup/popup.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="popup.xhtml",
                    resource = "/resources/examples/compat/"+
                               "popup/popup.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PopupBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/popup/PopupBean.java")
        }
)
@Menu(
	title = "menu.compat.popup.subMenu.title",
	menuLinks = {
            @MenuLink(title = "menu.compat.popup.subMenu.main",
                    isDefault = true,
                    exampleBeanName = PopupBean.BEAN_NAME),
            @MenuLink(title = "menu.compat.popup.subMenu.draggable",
                    exampleBeanName = PopupDraggable.BEAN_NAME),
            @MenuLink(title = "menu.compat.popup.subMenu.modal",
                    exampleBeanName = PopupModal.BEAN_NAME),
            @MenuLink(title = "menu.compat.popup.subMenu.center",
                    exampleBeanName = PopupCenter.BEAN_NAME),
            @MenuLink(title = "menu.compat.popup.subMenu.position",
                    exampleBeanName = PopupPosition.BEAN_NAME),
            @MenuLink(title = "menu.compat.popup.subMenu.client",
                    exampleBeanName = PopupClient.BEAN_NAME)
})
@ManagedBean(name= PopupBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PopupBean extends ComponentExampleImpl<PopupBean> implements Serializable {
	
	public static final String BEAN_NAME = "popup";
	
	private boolean opened = false;
	
	public PopupBean() {
		super(PopupBean.class);
	}
	
	public boolean isOpened() { return opened; }
	
	public void setOpened(boolean opened) { this.opened = opened; }
	
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
