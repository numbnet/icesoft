/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.samples.mobileshowcase.view.examples.layout.viewmanager;

/**
 *
 */

import java.io.Serializable;
import java.util.Stack;
import java.util.logging.Logger;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Destination;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.Example;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResource;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ExampleResources;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.ResourceType;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

@Destination(
        title = "example.layout.viewmanager.destination.title.short",
        titleExt = "example.layout.viewmanager.destination.title.long",
        titleBack = "example.layout.viewmanager.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/layout/viewmanager-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/layout/viewmanager-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "viewmanager-example.xhtml",
                        resource = "/WEB-INF/includes/examples/layout/viewmanager-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ViewManagerBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/layout/viewmanager/ViewManagerBean.java")
        }
)

@ManagedBean(name = ViewManagerBean.BEAN_NAME)
@SessionScoped
public class ViewManagerBean extends ExampleImpl<ViewManagerBean> implements
        Serializable {

    public static final String BEAN_NAME = "viewManagerBean";

    private static final Logger logger =
            Logger.getLogger(ViewManagerBean.class.toString());
            
    private String transitionType = "horizontal";
    private String barStyle;
    private String view = null;
    private Stack<String> history;
    private boolean clientSide = false;

    public ViewManagerBean() {
        super(ViewManagerBean.class);
        history = new Stack<String>();
    }

    public String getTransitionType() {
        return transitionType;
    }

    public void setTransitionType(String transitionType) {
        this.transitionType = transitionType;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public Stack<String> getHistory() {
        return history;
    }

    public void setHistory(Stack<String> history) {
        this.history = history;
    }

    public boolean isClientSide() {
        return clientSide;
    }

    public void setClientSide(boolean clientSide) {
        this.clientSide = clientSide;
    }

    public String getBarStyle() {
        return barStyle;
    }

    public void setBarStyle(String barStyle) {
        this.barStyle = barStyle;
    }

 }

