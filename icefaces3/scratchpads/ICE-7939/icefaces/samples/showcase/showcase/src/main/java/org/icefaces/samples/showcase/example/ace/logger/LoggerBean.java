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

package org.icefaces.samples.showcase.example.ace.logger;

import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;                                                                       
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.logger.title",
        description = "example.ace.logger.description",
        example = "/resources/examples/ace/logger/logger.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="logger.xhtml",
                    resource = "/resources/examples/ace/logger/logger.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="LoggerBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                               "/example/ace/logger/LoggerBean.java")
        }
)
@Menu(
	title = "menu.ace.logger.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.logger.subMenu.main",
	                exampleBeanName = LoggerBean.BEAN_NAME)
})
@ManagedBean(name= LoggerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LoggerBean extends ComponentExampleImpl<LoggerBean> implements Serializable {

    public static final String BEAN_NAME = "logger";

    private boolean rendered = false;
    private String category = "logBugz";

    public LoggerBean() {
        super(LoggerBean.class);
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }

    public void enableRendered() {
        this.rendered = true;
    }
}
