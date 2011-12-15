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

package org.icefaces.samples.showcase.example.ace.resizable;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import org.icefaces.ace.event.ResizeEvent;

@ComponentExample(
        parent = ResizableBean.BEAN_NAME,
        title = "example.ace.resizableListener.title",
        description = "example.ace.resizableListener.description",
        example = "/resources/examples/ace/resizable/resizeListener.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="resizeListener.xhtml",
                    resource = "/resources/examples/ace/resizable/resizeListener.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ResizeListener.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/resizable/ResizeListenerBean.java")
        }
)
@ManagedBean(name= ResizeListenerBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ResizeListenerBean extends ComponentExampleImpl<ResizeListenerBean> implements Serializable
{
    public static final String BEAN_NAME = "resizeListenerBean";
    private String resizeParameters;
    
    public ResizeListenerBean() 
    {
        super(ResizeListenerBean.class);
        resizeParameters = "Resize panel to call its listener !";
    }
    
    
    public void handleResizeEvent(ResizeEvent event)
    {
        System.out.println("Hello world!");
        resizeParameters = "My size changed to: "+event.getWidth() +" x "+ event.getHeight();
    }

    public String getResizeParameters() {
        return resizeParameters;
    }

    public void setResizeParameters(String resizeParameters) {
        this.resizeParameters = resizeParameters;
    }
}
