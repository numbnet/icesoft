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

package org.icefaces.component.pushbutton;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(
        tagName ="pushButton",
        componentClass ="org.icefaces.component.pushbutton.PushButton",
        rendererClass ="org.icefaces.component.pushbutton.PushButtonRenderer",
        componentType = "org.icefaces.PushButton", 
        rendererType = "org.icefaces.PushButtonRenderer",            
        extendsClass = "javax.faces.component.UICommand", 
        generatedClass = "org.icefaces.component.pushbutton.PushButtonBase",
		componentFamily="com.icesoft.faces.PushButton",
	    tlddoc = "This component allows entry of a complete form or just itself. " +
	         "It has athe same functionality of a regular jsf command button " +
	         "but without having to add extra attributes other than determining singleSubmit " +
	         "to be true or false"
        )
	
/*
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="oop/oop-min.js",library="yui/3_1_1"),
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
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),    
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
	
    @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),   
	
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="pushbutton.js",library="org.icefaces.component.pushbutton"),
	@ResourceDependency(name="pushbutton.css",library="org.icefaces.component.pushbutton")
})
        
public class PushButtonMeta extends UICommandMeta {
    
    @Property(tlddoc=" A localized user presentable name for this component. Used by aria.") 
    private String label;

	@Property(defaultValue="false",
			tlddoc="When singleSubmit is true, triggering an action on this component will submit" +
			" and execute this component only. Equivalent to <f:ajax execute='@this' render='@all'>." +
			" When singleSubmit is false, triggering an action on this component will submit and execute " +
			" the full form that this component is contained within." +
			" The default value is false.")
    private boolean singleSubmit;
	
    @Property (defaultValue="false",
            tlddoc="disabled property. If true no input may be submitted via this" +
    				"component.  Is required by aria specs")
    private boolean disabled;
    
    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;
  
    @Property(tlddoc="style class of the component, rendered on the div root of the component")
    private String styleClass;  

    @Property(tlddoc="style of the component, rendered on the div root of the component")
    private String style;
}
