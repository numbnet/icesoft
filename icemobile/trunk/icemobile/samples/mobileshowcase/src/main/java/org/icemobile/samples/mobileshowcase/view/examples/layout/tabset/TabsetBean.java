package org.icemobile.samples.mobileshowcase.view.examples.layout.tabset;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;


/**
 * Content stack bean stores the id of panels that can be selected.
 */
@Destination(
        title = "example.layout.tabset.destination.title.short",
        titleExt = "example.layout.tabset.destination.title.long",
        titleBack = "example.layout.tabset.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/tabset-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/tabset-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "tabset-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/tabset-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "TabsetBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/tabset/TabsetBean.java")
        }
)
@ManagedBean(name = TabsetBean.BEAN_NAME)
@SessionScoped
public class TabsetBean extends ExampleImpl<TabsetBean> implements
        Serializable {

    public static final String BEAN_NAME = "tabsetBean";

    private int selectedTabId = 0;

    public TabsetBean() {
        super(TabsetBean.class);
    }
}
