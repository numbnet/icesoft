/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.compat.tree;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;

import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

@ComponentExample(
        parent = TreeBean.BEAN_NAME,
        title = "example.compat.tree.navigation.title",
        description = "example.compat.tree.navigation.description",
        example = "/resources/examples/compat/tree/treeNavigation.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="treeNavigation.xhtml",
                    resource = "/resources/examples/compat/"+
                               "tree/treeNavigation.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="TreeNavigation.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/"+
                               "showcase/example/compat/tree/TreeNavigation.java")
        }
)
@ManagedBean(name= TreeNavigation.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeNavigation extends ComponentExampleImpl<TreeNavigation> implements Serializable {
	
    public static final String BEAN_NAME = "treeNavigation";

    private boolean hideNavigation = true;

    public TreeNavigation() {
            super(TreeNavigation.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public boolean getHideNavigation() { return hideNavigation; }

    public void setHideNavigation(boolean hideNavigation) { this.hideNavigation = hideNavigation; }

    public void toggleNavigation(ActionEvent event) {
        hideNavigation = !hideNavigation;
    }
}
