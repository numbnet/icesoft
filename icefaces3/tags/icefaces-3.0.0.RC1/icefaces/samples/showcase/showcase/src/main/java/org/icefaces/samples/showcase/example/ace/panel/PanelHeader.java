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

@ComponentExample(
        parent = PanelBean.BEAN_NAME,
        title = "example.ace.panel.header.title",
        description = "example.ace.panel.header.description",
        example = "/resources/examples/ace/panel/panelHeader.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panelHeader.xhtml",
                    resource = "/resources/examples/ace/panel/panelHeader.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelHeader.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panel/PanelHeader.java")
        }
)
@ManagedBean(name= PanelHeader.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelHeader extends ComponentExampleImpl<PanelHeader> implements Serializable {

    public static final String BEAN_NAME = "panelHeader";
    
    private boolean headerEnable = true;
    private boolean footerEnable = true;
    private String headerText = "Our Header";
    private String footerText = "Our Footer";

    public PanelHeader() {
        super(PanelHeader.class);
    }
    
    public boolean getHeaderEnable() { return headerEnable; }
    public boolean getFooterEnable() { return footerEnable; }
    public String getHeaderText() { return headerText; }
    public String getFooterText() { return footerText; }
    
    public void setHeaderEnable(boolean headerEnable) { this.headerEnable = headerEnable; }
    public void setFooterEnable(boolean footerEnable) { this.footerEnable = footerEnable; }
    public void setHeaderText(String headerText) { this.headerText = headerText; }
    public void setFooterText(String footerText) { this.footerText = footerText; }
}
