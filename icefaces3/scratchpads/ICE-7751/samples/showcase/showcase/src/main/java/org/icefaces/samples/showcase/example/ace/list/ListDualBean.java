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
    title = "example.ace.list.dual.title",
    description = "example.ace.list.dual.description",
    example = "/resources/examples/ace/list/listDual.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="ListDual.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/listDual.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListDualBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListDualBean.java")
    }
)
@ManagedBean(name= ListDualBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListDualBean extends ComponentExampleImpl<ListDualBean> implements Serializable {
    public static final String BEAN_NAME = "listDualBean";

    public ListDualBean() {
        super(ListDualBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    List<SelectItem> stringList = new ArrayList<SelectItem>() {{
        for (String s : DataTableData.CHASSIS_ALL) {
            add(new SelectItem(s));
        }
        remove(DataTableData.CHASSIS_ALL.length-1);
    }};

    List<SelectItem> destStringList = new ArrayList<SelectItem>() {{
        add(new SelectItem(DataTableData.CHASSIS_ALL[DataTableData.CHASSIS_ALL.length-1]));
    }};

    public List<SelectItem> getStringList() {
        return stringList;
    }

    public void setStringList(List<SelectItem> stringList) {
        this.stringList = stringList;
    }

    public List<SelectItem> getDestStringList() {
        return destStringList;
    }

    public void setDestStringList(List<SelectItem> destStringList) {
        this.destStringList = destStringList;
    }
}
