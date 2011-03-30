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

package org.icefaces.component.linkbutton;

import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(
        tagName ="linkButton",
        componentClass ="org.icefaces.component.linkbutton.LinkButton",
        rendererClass ="org.icefaces.component.linkbutton.LinkButtonRenderer",
        componentType = "org.icefaces.LinkButton",
        rendererType = "org.icefaces.LinkButtonRenderer",
        extendsClass = "javax.faces.component.UICommand",
        generatedClass = "org.icefaces.component.linkbutton.LinkButtonBase",
        componentFamily="com.icesoft.faces.LinkButton"
)

/*
        @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
		
	@ResourceDependency(name="event-custom/event-custom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="attribute/attribute-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="base/base-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-base-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="dom/dom-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="node/node-min.js",library="yui/3_1_1"),
	@ResourceDependency(name="event/event-delegate-min.js",library="yui/3_1_1"),
    @ResourceDependency(name ="plugin/plugin-min.js",library = "yui/3_1_1"),    
    @ResourceDependency(name ="pluginhost/pluginhost-min.js",library = "yui/3_1_1"),      
*/

@ResourceDependencies({
        @ResourceDependency(name="yui/yui-min.js",library="yui/3_3_0"),

	@ResourceDependency(name="loader/loader-min.js",library="yui/3_3_0"),
	@ResourceDependency(name="oop/oop-min.js",library="yui/3_3_0"),
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),   
		
        @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
        @ResourceDependency(name="component.js",library="org.icefaces.component.util"),
        @ResourceDependency(name="linkbutton.js",library="org.icefaces.component.linkbutton"),
		@ResourceDependency(name="linkbutton.css",library="org.icefaces.component.linkbutton")
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

    @Property (tlddoc="This property defines the link text visible in the component", implementation=Implementation.GENERATE,
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
