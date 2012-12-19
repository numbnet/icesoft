package org.icemobile.samples.mobileshowcase.view.examples.layout.accordion;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Accordion  bean stores meta data for the example  as well as the
 * state for the example.
 */
@Destination(
        title = "example.layout.accordion.destination.title.short",
        titleExt = "example.layout.accordion.destination.title.long",
        titleBack = "example.layout.accordion.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/accordion-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/accordion-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "accordion-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/accordion-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "AccordionBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/accordion/AccordionBean.java")
        }
)
@ManagedBean(name = AccordionBean.BEAN_NAME)
@SessionScoped
public class AccordionBean extends ExampleImpl<AccordionBean> implements
        Serializable {

    public static final String BEAN_NAME = "accordionBean";


    private String currentId = "tab1";

    public AccordionBean() {
        super(AccordionBean.class);
    }

    public String getCurrentId() {
        return currentId;
    }

    public void setCurrentId(String currentId) {
        this.currentId = currentId;
    }
}
