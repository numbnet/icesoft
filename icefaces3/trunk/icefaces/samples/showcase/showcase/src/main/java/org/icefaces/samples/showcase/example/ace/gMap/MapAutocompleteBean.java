package org.icefaces.samples.showcase.example.ace.gMap;

import javax.el.MethodExpression;
import javax.faces.application.*;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.*;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import javax.faces.bean.CustomScoped;
import javax.annotation.PostConstruct;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import java.io.Serializable;

@ComponentExample(
        parent = MapBean.BEAN_NAME,
        title = "example.ace.gMap.autocomplete.title",
        description = "example.ace.gMap.autocomplete.description",
        example = "/resources/examples/ace/gMap/gMapAutocomplete.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="gMapAutocomplete.xhtml",
                    resource = "/resources/examples/ace/gMap/gMapAutocomplete.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MapAutocompleteBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/gMap/MapAutocompleteBean.java")
        }
)
@ManagedBean(name= MapAutocompleteBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MapAutocompleteBean extends ComponentExampleImpl<MapAutocompleteBean> implements Serializable{
	public static final String BEAN_NAME = "autocompleteBean";
	private boolean showWindow = false;
    private String address,types,url;

    public boolean isShowWindow() {
        return showWindow;
    }

    public void setShowWindow(boolean showWindow) {
        this.showWindow = showWindow;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTypes() {
        return types;
    }

    public void setTypes(String types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
	
	public MapAutocompleteBean() {
        super(MapAutocompleteBean.class);
    }
	@PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
