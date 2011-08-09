/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.examples.layout.list;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 *
 */
@Destination(
        title = "example.layout.list.destination.title.short",
        titleExt = "example.layout.list.destination.title.long",
        titleBack = "example.layout.list.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/layout/list.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/list-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/list-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "list-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/list-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ListBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/list/ListBean.java")
        }
)
@ManagedBean(name = ListBean.BEAN_NAME)
@SessionScoped
public class ListBean extends ExampleImpl<ListBean> implements
        Serializable {

    public static final String BEAN_NAME = "listBean";

    public ListBean() {
        super(ListBean.class);
    }

}