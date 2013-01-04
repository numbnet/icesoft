package org.icefaces.samples.showcase.example.ace.tree;

import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.metadata.context.ResourceRootPath;

import javax.annotation.PostConstruct;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

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
 * Date: 2012-09-26
 * Time: 1:10 PM
 */

@ExampleResources(
        resources ={
                @ExampleResource(type = ResourceType.wiki,
                        title="ace:tree",
                        resource = ResourceRootPath.FOR_WIKI +"Tree"),

                @ExampleResource(type = ResourceType.tld,
                        title="ace:tree",
                        resource = ResourceRootPath.FOR_ACE_TLD + "tree.html"),

                @ExampleResource(type = ResourceType.tld,
                        title="ace:node",
                        resource = ResourceRootPath.FOR_ACE_TLD + "node.html")
        }
)
@ManagedBean(name= TreeResources.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class TreeResources extends ComponentExampleImpl< TreeResources > implements Serializable {
    public static final String BEAN_NAME = "treeResources";
    public TreeResources()
    {
        super(TreeResources.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

}
