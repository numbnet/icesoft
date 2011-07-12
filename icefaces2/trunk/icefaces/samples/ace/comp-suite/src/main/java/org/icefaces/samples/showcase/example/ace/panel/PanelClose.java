package org.icefaces.samples.showcase.example.ace.panel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

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

@ComponentExample(
        parent = PanelBean.BEAN_NAME,
        title = "example.ace.panel.close.title",
        description = "example.ace.panel.close.description",
        example = "/resources/examples/ace/panel/panelClose.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelClose.xhtml",
                    resource = "/resources/examples/ace/panel/panelClose.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelClose.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panel/PanelClose.java")
        }
)
@ManagedBean(name= PanelClose.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelClose extends ComponentExampleImpl<PanelClose> implements Serializable {

    public static final String BEAN_NAME = "panelClose";
    
    private boolean closable = true;
    private int speed = 700;
    
    public PanelClose() {
        super(PanelClose.class);
    }
    
    public boolean getClosable() { return closable; }
    public int getSpeed() { return speed; }
    
    public void setClosable(boolean closable) { this.closable = closable; }
    public void setSpeed(int speed) { this.speed = speed; }
}
