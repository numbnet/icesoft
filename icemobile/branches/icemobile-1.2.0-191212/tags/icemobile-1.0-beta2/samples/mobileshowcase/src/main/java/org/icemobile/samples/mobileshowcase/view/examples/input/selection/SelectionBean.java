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

package org.icemobile.samples.mobileshowcase.view.examples.input.selection;


import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;

@Destination(
        title = "example.input.selection.destination.title.short",
        titleExt = "example.input.selection.destination.title.long",
        titleBack = "example.input.selection.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/input/selection.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/selection-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/selection-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "selection-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/selection-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "SelectionBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/selection/SelectionBean.java")
        }
)

@ManagedBean(name = SelectionBean.BEAN_NAME)
@SessionScoped
public class SelectionBean extends ExampleImpl<SelectionBean> implements
        Serializable {

    public static final String BEAN_NAME = "selectionBean";

    private String[] array1 = new String[]{"test1", "test2", "test3"};
    private String[] array2 = new String[]{"test1", "test2", "test3"};
    private String[] array3 = new String[]{"test1", "test2", "test3"};

    private boolean boolean1;
    private boolean boolean2;
    private boolean boolean3;

    private String string1 = "test1";
    private String string2 = "Pepsi";
    private String string3 = "test3";

    public SelectionBean() {
        super(SelectionBean.class);
    }

    public void filterChanged(ValueChangeEvent event) {

    }

    public String[] getArray1() {
        return array1;
    }

    public void setArray1(String[] array1) {
        this.array1 = array1;
    }

    public boolean isBoolean1() {
        return boolean1;
    }

    public void setBoolean1(boolean boolean1) {
        this.boolean1 = boolean1;
    }

    public boolean isBoolean2() {
        return boolean2;
    }

    public void setBoolean2(boolean boolean2) {
        this.boolean2 = boolean2;
    }

    public boolean isBoolean3() {
        return boolean3;
    }

    public void setBoolean3(boolean boolean3) {
        this.boolean3 = boolean3;
    }

    public String getString1() {
        return string1;
    }

    public void setString1(String string1) {
        this.string1 = string1;
    }

    public String getString2() {
        return string2;
    }

    public void setString2(String string2) {
        this.string2 = string2;
    }

    public String getString3() {
        return string3;
    }

    public void setString3(String string3) {
        this.string3 = string3;
    }

    public String[] getArray2() {
        return array2;
    }

    public void setArray2(String[] array2) {
        this.array2 = array2;
    }

    public String[] getArray3() {
        return array3;
    }

    public void setArray3(String[] array3) {
        this.array3 = array3;
    }
}