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

package org.icefaces.samples.showcase.example.ace.resizable;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ComponentExample(
        title = "example.ace.resizable.title",
        description = "example.ace.resizable.description",
        example = "/resources/examples/ace/resizable/resizable.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="resizable.xhtml",
                    resource = "/resources/examples/ace/resizable/resizable.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ResizableBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/resizable/ResizableBean.java")
        }
)
@Menu(
	title = "menu.ace.maskedEntry.subMenu.main",
	menuLinks = {
	        @MenuLink(title = "menu.ace.resizable.subMenu.main",
	                isDefault = true,
                    exampleBeanName = ResizableBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.resizable.subMenu.resizeListener",
                    exampleBeanName = ResizeListenerBean.BEAN_NAME)
    }
)
@ManagedBean(name= ResizableBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ResizableBean extends ComponentExampleImpl<ResizableBean> implements Serializable
{
    public static final String BEAN_NAME = "resizableBean";
    
    public ResizableBean()
    {
        super(ResizableBean.class);
    }

}
