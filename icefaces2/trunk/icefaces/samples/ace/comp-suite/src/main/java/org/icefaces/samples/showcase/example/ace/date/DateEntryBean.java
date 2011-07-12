/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 */
package org.icefaces.samples.showcase.example.ace.date;

import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;                                                                       
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.Date;

@ComponentExample(
        title = "example.ace.dateentry.title",
        description = "example.ace.dateentry.description",
        example = "/resources/examples/ace/date/dateentry.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dateentry.xhtml",
                    resource = "/resources/examples/ace/date/dateentry.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DateEntryBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DateEntryBean.java")
        }
)
@Menu(
	title = "menu.ace.dateentry.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.dateentry.subMenu.main",
	                exampleBeanName = DateEntryBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.dateentry.subMenu.popup",
                exampleBeanName = DatePopupBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.dateentry.subMenu.ajax",
                exampleBeanName = DateAjaxBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.dateentry.subMenu.pages",
                exampleBeanName = DatePagesBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.dateentry.subMenu.navigator",
                exampleBeanName = DateNavigatorBean.BEAN_NAME)
    }
)
@ManagedBean(name= DateEntryBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateEntryBean extends ComponentExampleImpl<DateEntryBean> implements Serializable {
    public static final String BEAN_NAME = "dateEntry";

    private Date selectedDate = new Date(System.currentTimeMillis());

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public DateEntryBean() {
        super(DateEntryBean.class);
    }
}
