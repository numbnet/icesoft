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
    title = "example.ace.list.reorder.title",
    description = "example.ace.list.reorder.description",
    example = "/resources/examples/ace/list/listReorder.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="ListReorder.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/listReorder.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListReorderBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListReorderBean.java")
    }
)
@ManagedBean(name= ListReorderBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListReorderBean extends ComponentExampleImpl<ListReorderBean> implements Serializable {
    public static final String BEAN_NAME = "listReorderBean";

    public ListReorderBean() {
        super(ListReorderBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    List<SelectItem> stringList = new ArrayList<SelectItem>() {{
        for (String s : DataTableData.CHASSIS_ALL) {
            add(new SelectItem(s));
        }
    }};

    List<SelectItem> ajaxStringList = new ArrayList<SelectItem>() {{
        for (String s : DataTableData.CHASSIS_ALL) {
            add(new SelectItem(s));
        }
    }};

    public List<SelectItem> getStringList() {
        return stringList;
    }

    public void setStringList(List<SelectItem> stringList) {
        this.stringList = stringList;
    }

    public List<SelectItem> getAjaxStringList() {
        return ajaxStringList;
    }

    public void setAjaxStringList(List<SelectItem> ajaxStringList) {
        this.ajaxStringList = ajaxStringList;
    }
}
