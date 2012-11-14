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


    private String selectedId1 = "accordionPane1";
    private String selectedId2 = "accordionPane4";
    private String selectedId3 = "accordionPane7";

    public AccordionBean() {
        super(AccordionBean.class);
    }

    public String getSelectedId1() {
        return selectedId1;
    }

    public void setSelectedId1(String selectedId1) {
        this.selectedId1 = selectedId1;
    }

    public String getSelectedId2() {
        return selectedId2;
    }

    public void setSelectedId2(String selectedId2) {
        this.selectedId2 = selectedId2;
    }

    public String getSelectedId3() {
        return selectedId3;
    }

    public void setSelectedId3(String selectedId3) {
        this.selectedId3 = selectedId3;
    }

    
}
