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

package org.icefaces.ace.component.linkbutton;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.Implementation;
import org.icefaces.ace.meta.annotation.Property;

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
        @ResourceDependency(name="yui/yui-min.js",library="yui/3_3_0"),
    	@ResourceDependency(name="loader/loader-min.js",library="yui/3_3_0"),
        @ResourceDependency(name="util/combined.js",library="icefaces.ace"),
		@ResourceDependency(name="yui2-skin-sam-button/assets/button-core.css",library="yui/2in3")
})

public class LinkButtonMeta extends UICommandMeta {

    @Property(tlddoc = "href of link. If specified and actionListener is absent, linkButton works " +
                       "as normal anchor. If specified and actionListener is present, linkButton works " +
                       "as AJAX event source, but href may be opened in a new tab or window")
    private String href;

    @Property(tlddoc ="standard HTML href language attribute")
    private String hrefLang;

    @Property(defaultValue="false",
              tlddoc="When singleSubmit is true, triggering an action on this component " +
                     "will submit and execute this component only (equivalent to " +
                     "<f:ajax execute=\"@this\" render=\"@all\">). When singleSubmit is false, " +
                     "triggering an action on this component will submit and execute the full " +
                     "form that this component is contained within. The default value is false.")
    private boolean singleSubmit;

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
