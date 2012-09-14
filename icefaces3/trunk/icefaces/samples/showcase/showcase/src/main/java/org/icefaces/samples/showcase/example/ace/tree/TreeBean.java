package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.ace.model.tree.LazyNodeDataModel;
import org.icefaces.ace.model.tree.NodeState;
import org.icefaces.ace.model.tree.NodeStateMap;
import org.icefaces.ace.model.tree.StateCreationCallback;
import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.util.JavaScriptRunner;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils
 * Date: 2012-08-14
 * Time: 3:19 PM
 */
@ComponentExample(
        title = "example.ace.tree.title",
        description = "example.ace.tree.description",
        example = "/resources/examples/ace/tree/tree.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="tree.xhtml",
                        resource = "/resources/examples/ace/tree/tree.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="TreeBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/tree/TreeBean.java")
        }
)
@Menu(
        title = "menu.ace.tree.subMenu.title",
        menuLinks = {
            @MenuLink(title = "menu.ace.tree.subMenu.main", isDefault = true, exampleBeanName = TreeBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.lazy", exampleBeanName = TreeLazyBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.client", exampleBeanName = TreeClientBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.tree.subMenu.nested", exampleBeanName = TreeNestedBean.BEAN_NAME)
        }
)
@ManagedBean(name= TreeBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeBean extends ComponentExampleImpl<TreeBean> implements Serializable {
    public static final String BEAN_NAME = "treeBean";
    private List<LocationNodeImpl> treeRoots = Arrays.asList(TreeDataFactory.getTreeRoots().clone());
    private NodeStateMap stateMap;


    public TreeBean() {
        super(TreeBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }

    public void print(String text) {
        JavaScriptRunner.runScript(FacesContext.getCurrentInstance(),
                "alert('"+text+"');");
    }

    public NodeStateMap getStateMap() {
        return stateMap;
    }

    public void setStateMap(NodeStateMap stateMap) {
        this.stateMap = stateMap;
    }
}
