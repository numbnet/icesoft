package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
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
 * Date: 2012-09-14
 * Time: 1:30 PM
 */

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.ace.tree.client.title",
        description = "example.ace.tree.client.description",
        example = "/resources/examples/ace/tree/treeClient.xhtml"
)
@ExampleResources(
        resources ={
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title="treeClient.xhtml",
                        resource = "/resources/examples/ace/tree/treeClient.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title="TreeClientBean.java",
                        resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                                "/example/ace/tree/TreeClientBean.java")
        }
)
@ManagedBean(name= TreeClientBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeClientBean extends ComponentExampleImpl<TreeClientBean> implements Serializable {
    public static final String BEAN_NAME = "treeClientBean";
    private List<LocationNodeImpl> treeRoots = Arrays.asList(TreeDataFactory.getTreeRoots().clone());

    public TreeClientBean() {
        super(TreeClientBean.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public List<LocationNodeImpl> getTreeRoots() {
        return treeRoots;
    }
}
