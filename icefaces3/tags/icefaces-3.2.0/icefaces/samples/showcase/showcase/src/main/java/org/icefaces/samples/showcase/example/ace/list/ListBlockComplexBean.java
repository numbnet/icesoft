package org.icefaces.samples.showcase.example.ace.list;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
import org.icefaces.samples.showcase.example.compat.dataTable.Car;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ComponentExample(
    parent = ListBean.BEAN_NAME,
    title = "example.ace.list.blockComplex.title",
    description = "example.ace.list.blockComplex.description",
    example = "/resources/examples/ace/list/listBlockComplex.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="ListBlockComplex.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/listBlockComplex.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListBlockComplexBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListBlockComplexBean.java")
    }
)

@ManagedBean(name= ListBlockComplexBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListBlockComplexBean extends ComponentExampleImpl<ListBlockComplexBean> implements Serializable {
    public static final String BEAN_NAME = "listBlockComplexBean";

    public ListBlockComplexBean() {
        super(ListBlockComplexBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }
}
