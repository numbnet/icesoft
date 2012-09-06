package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.ace.tree.nested.title",
        description = "example.ace.tree.nested.description",
        example = "/resources/examples/ace/tree/treeNested.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="treeNested.xhtml",
                        resource = "/resources/examples/ace/tree/treeNested.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="TreeNestedBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/tree/TreeNestedBean.java")
        }
)
@ManagedBean(name= TreeNestedBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeNestedBean extends ComponentExampleImpl<TreeNestedBean> implements Serializable {
    public static final String BEAN_NAME = "treeNestedBean";
    private List<LocationNodeImpl> treeRoots;

    public TreeNestedBean() {
        super(TreeNestedBean.class);
        treeRoots = Arrays.asList(TreeDataFactory.getTreeRoots().clone());
    }

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }
}
