package org.icefaces.samples.showcase.example.ace.list;

import org.icefaces.samples.showcase.dataGenerators.utilityClasses.DataTableData;
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
    title = "example.ace.list.multi.title",
    description = "example.ace.list.multi.description",
    example = "/resources/examples/ace/list/listMulti.xhtml"
)
@ExampleResources(
    resources ={
        // xhtml
        @ExampleResource(type = ResourceType.xhtml,
                title="ListMutli.xhtml",
                resource = "/resources/examples/ace/"+
                        "list/listMulti.xhtml"),
        // Java Source
        @ExampleResource(type = ResourceType.java,
                title="ListMultiBean.java",
                resource = "/WEB-INF/classes/org/icefaces/samples/"+
                        "showcase/example/ace/list/ListMultiBean.java")
    }
)
@ManagedBean(name= ListMultiBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ListMultiBean extends ComponentExampleImpl<ListMultiBean> implements Serializable {
    public static final String BEAN_NAME = "listMultiBean";

    public ListMultiBean() {
        super(ListMultiBean.class);
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

    List<SelectItem> fstDestStringList = new ArrayList<SelectItem>() {{
        add(new SelectItem(DataTableData.CHASSIS_ALL[DataTableData.CHASSIS_ALL.length-1]));
    }};

    List<SelectItem> sndDestStringList = new ArrayList<SelectItem>();


    public List<SelectItem> getStringList() {
        return stringList;
    }

    public void setStringList(List<SelectItem> stringList) {
        this.stringList = stringList;
    }

    public List<SelectItem> getFstDestStringList() {
        return fstDestStringList;
    }

    public void setFstDestStringList(List<SelectItem> fstDestStringList) {
        this.fstDestStringList = fstDestStringList;
    }

    public List<SelectItem> getSndDestStringList() {
        return sndDestStringList;
    }

    public void setSndDestStringList(List<SelectItem> sndDestStringList) {
        this.sndDestStringList = sndDestStringList;
    }
}