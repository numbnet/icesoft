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

package org.icefaces.samples.showcase.example.ace.accordionpanel;

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
        parent = AccordionPanelBean.BEAN_NAME,
        title = "example.ace.accordionpanel.effect.title",
        description = "example.ace.accordionpanel.effect.description",
        example = "/resources/examples/ace/accordionpanel/accordionPanelEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="accordionPanelEffect.xhtml",
                    resource = "/resources/examples/ace/accordionpanel/accordionPanelEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AccordionPanelEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/accordionpanel/AccordionPanelEffectBean.java")
        }
)
@ManagedBean(name= AccordionPanelEffectBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelEffectBean extends ComponentExampleImpl<AccordionPanelEffectBean> implements Serializable {
    public static final String BEAN_NAME = "accordionPanelEffectBean";

    public AccordionPanelEffectBean() {
        super(AccordionPanelEffectBean.class);
    }
}