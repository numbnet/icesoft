/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
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
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.ace.component.linkbutton;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;

import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName         = "linkButton",
        componentClass  = "org.icefaces.ace.component.linkbutton.LinkButton",
        rendererClass   = "org.icefaces.ace.component.linkbutton.LinkButtonRenderer",
        generatedClass  = "org.icefaces.ace.component.linkbutton.LinkButtonBase",
        extendsClass    = "javax.faces.component.UICommand",
        componentType   = "org.icefaces.ace.component.LinkButton",
        rendererType    = "org.icefaces.ace.component.LinkButtonRenderer",
        componentFamily = "org.icefaces.ace.LinkButton"
)
@ResourceDependencies({
		@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
        @ResourceDependency(name="util/ace-yui.js",library="icefaces.ace"),
        @ResourceDependency(library="icefaces.ace", name="linkbutton/linkbutton.css"),
		@ResourceDependency(name="button/assets/button-core.css",library="yui/2_8_2")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="activate", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="activate")
public class LinkButtonMeta extends UICommandMeta {

    @Property(tlddoc = "href of link. If specified and actionListener is absent, linkButton works " +
                       "as normal anchor. If specified and actionListener is present, linkButton works " +
                       "as AJAX event source, but href may be opened in a new tab or window")
    private String href;

    @Property(tlddoc ="standard HTML href language attribute")
    private String hrefLang;

    @Property (defaultValue="false", tlddoc="If true, disables the YUI component on the page.")
    private boolean disabled;

    @Property (tlddoc="This property defines the link text visible in the component", implementation= Implementation.GENERATE,
    defaultValue="Default Anchor Label")
    private Object value; 

    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;

    @Property(tlddoc="The CSS style class of the component")
    private String styleClass;

    @Property(tlddoc="the inline style of the component")
    private String style;

    @Property(tlddoc="If the link is a traditional anchor, this is the traditional target attribute")
    private String target; 
}
