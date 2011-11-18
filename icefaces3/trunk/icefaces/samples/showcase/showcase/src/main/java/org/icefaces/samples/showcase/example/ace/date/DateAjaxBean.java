/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
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

import org.icefaces.ace.event.DateSelectEvent;  

@ComponentExample(
        parent = DateEntryBean.BEAN_NAME,
        title = "example.ace.dateentry.ajax.title",
        description = "example.ace.dateentry.ajax.description",
        example = "/resources/examples/ace/date/dateajax.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dateajax.xhtml",
                    resource = "/resources/examples/ace/date/dateajax.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DateAjaxBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/date/DateAjaxBean.java")
        }
)
@ManagedBean(name= DateAjaxBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DateAjaxBean extends ComponentExampleImpl<DateAjaxBean> implements Serializable {
    public static final String BEAN_NAME = "dateAjax";

    private Date selectedDateIn = new Date(System.currentTimeMillis());
    private Date selectedDatePop = new Date(System.currentTimeMillis());
    
    public DateAjaxBean() {
        super(DateAjaxBean.class);
    }

    public Date getSelectedDateIn() {
        return selectedDateIn;
    }

    public void setSelectedDateIn(Date selectedDateIn) {
        this.selectedDateIn = selectedDateIn;
    }
    
    public Date getSelectedDatePop() {
        return selectedDatePop;
    }

    public void setSelectedDatePop(Date selectedDatePop) {
        this.selectedDatePop = selectedDatePop;
    }
    
    public void dateSelectInListener(DateSelectEvent event) {
        setSelectedDateIn(event.getDate());
    }
    
    public void dateSelectPopListener(DateSelectEvent event) {
        setSelectedDatePop(event.getDate());
    }
}
