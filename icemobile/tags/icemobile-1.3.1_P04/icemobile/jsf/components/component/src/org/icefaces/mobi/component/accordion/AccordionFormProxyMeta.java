/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.accordion;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "accordionFormProxy",
        componentClass = "org.icefaces.mobi.component.accordion.AccordionFormProxy",
        rendererClass = "org.icefaces.mobi.component.accordion.AccordionFormProxyRenderer",
        generatedClass = "org.icefaces.mobi.component.accordion.AccordionFormProxyBase",
        componentType = "org.icefaces.AccordionFormProxy",
        rendererType = "org.icefaces.AccordionFormProxyRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.AccordionFormProxy",
        tlddoc = "accordionFormProxy allows accordions to nest forms and still maintain server-side current pane state")
public class AccordionFormProxyMeta extends UIComponentBaseMeta {
    
    @Property(tlddoc="clientId of the accordion component, only required if the accordionFormProxy is not nested inside an accordion", name="for", required=Required.no)
    private String For;

}
