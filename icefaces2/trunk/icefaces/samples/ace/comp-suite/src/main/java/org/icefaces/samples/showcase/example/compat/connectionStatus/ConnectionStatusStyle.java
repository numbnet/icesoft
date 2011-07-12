package org.icefaces.samples.showcase.example.compat.connectionStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.samples.showcase.view.navigation.NavigationController;
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = ConnectionStatus.BEAN_NAME,
        title = "example.compat.connectionStatus.labels.title",
        description = "example.compat.connectionStatus.labels.description",
        example = "/resources/examples/compat/connectionStatus/connectionStatusStyle.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="connectionStatusStyle.xhtml",
                    resource = "/resources/examples/compat/"+
                               "connectionStatus/connectionStatusStyle.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConnectionStatusStyle.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/connectionStatus/ConnectionStatusStyle.java")
        }
)
@ManagedBean(name= ConnectionStatusStyle.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConnectionStatusStyle extends ComponentExampleImpl<ConnectionStatusStyle> implements Serializable {
	
	public static final String BEAN_NAME = "connectionStatusStyle";
	
	public ConnectionStatusStyle() {
		super(ConnectionStatusStyle.class);
	}
}
