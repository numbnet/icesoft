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
    title = "example.ace.list.reorderAjax.title",
    description = "example.ace.list.reorderAjax.description",
    example = "/resources/examples/ace/list/listReorderAjax.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="ListReorderAjax.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/listReorderAjax.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListReorderAjaxBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListReorderAjaxBean.java")
    }
)
@ManagedBean(name= ListReorderAjaxBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListReorderAjaxBean extends ComponentExampleImpl<ListReorderAjaxBean> implements Serializable {
    public static final String BEAN_NAME = "listReorderAjaxBean";

    public ListReorderAjaxBean() {
        super(ListReorderAjaxBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    List<SelectItem> ajaxStringList = new ArrayList<SelectItem>() {{
        for (String s : DataTableData.CHASSIS_ALL) {
            add(new SelectItem(s));
        }
    }};

    public List<SelectItem> getAjaxStringList() {
        return ajaxStringList;
    }

    public void setAjaxStringList(List<SelectItem> ajaxStringList) {
        this.ajaxStringList = ajaxStringList;
    }
}
