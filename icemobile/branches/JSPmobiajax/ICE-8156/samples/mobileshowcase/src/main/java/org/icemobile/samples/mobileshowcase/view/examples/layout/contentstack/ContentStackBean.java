package org.icemobile.samples.mobileshowcase.view.examples.layout.contentstack;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.context.FacesContext;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Map;

/**
 * Content stack bean stores the id of panels that can be selected.
 */
@Destination(
        title = "example.layout.contentstack.destination.title.short",
        titleExt = "example.layout.contentstack.destination.title.long",
        titleBack = "example.layout.contentstack.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/contentstack-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/contentstack-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "contentstack-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/contentstack-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ContentStackBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/contentstack/ContentStackBean.java")
        }
)
@ManagedBean(name = ContentStackBean.BEAN_NAME)
@SessionScoped
public class ContentStackBean  extends ExampleImpl<ContentStackBean> implements
        Serializable {

    public static final String BEAN_NAME = "contentStackBean";

    private String selectedPaneId = "panel1";

    public ContentStackBean() {
        super(ContentStackBean.class);
    }

    public String nextPaneParam()  {
        Map<String,String> params = 
            FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
     
        String paneName = params.get("paneName");
        this.selectedPaneId = paneName;
        return null;
    }


    public String nextPane(String selectedPaneId){
        this.selectedPaneId = selectedPaneId;
        return null;
    }

    public String getSelectedPaneId() {
        return selectedPaneId;
    }

    public void setSelectedPaneId(String selectedPaneId) {
        this.selectedPaneId = selectedPaneId;
    }
}
