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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.examples.input.dateSpinner;


import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;
import java.util.Date;

@Destination(
        title = "example.input.dateSpinner.destination.title.short",
        titleExt = "example.input.dateSpinner.destination.title.long",
        titleBack = "example.input.dateSpinner.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/input/dateSpinner.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/dateSpinner-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/dateSpinner-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "dateSpinner-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/dateSpinner-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "DateSpinnerBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/dateSpinner/DateSpinnerBean.java")
        }
)

@ManagedBean(name = DateSpinnerBean.BEAN_NAME)
@SessionScoped
public class DateSpinnerBean extends ExampleImpl<DateSpinnerBean> implements
        Serializable {

    public static final String BEAN_NAME = "dateSpinnerBean";

    // simple bean input value bindings
    private Date dateInput;

    public DateSpinnerBean() {
        super(DateSpinnerBean.class);
    }

    public Date getDateInput() {
        return dateInput;
    }

    public void setDateInput(Date dateInput) {
        this.dateInput = dateInput;
    }

}