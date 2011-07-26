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

package org.icefaces.ace.component.pushbutton;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UICommandMeta;
import org.icefaces.ace.meta.annotation.Property;

  @Component(
        tagName         = "pushButton",
        componentClass  = "org.icefaces.ace.component.pushbutton.PushButton",
        rendererClass   = "org.icefaces.ace.component.pushbutton.PushButtonRenderer",
        generatedClass  = "org.icefaces.ace.component.pushbutton.PushButtonBase",
        extendsClass    = "javax.faces.component.UICommand",
        componentType   = "org.icefaces.ace.component.PushButton",
        rendererType    = "org.icefaces.ace.component.PushButtonRenderer",
		componentFamily = "org.icefaces.ace.PushButton",
	    tlddoc = "This component allows entry of a complete form or just itself. " +
	         "It has athe same functionality of a regular jsf command button " +
	         "but without having to add extra attributes other than determining singleSubmit " +
	         "to be true or false"
        )
@ResourceDependencies({
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_3_0"),    
	@ResourceDependency(name="loader/loader-min.js",library="yui/3_3_0"),
    @ResourceDependency(name="util/combined.js",library="icefaces.ace"),
	@ResourceDependency(name="yui2-skin-sam-button/assets/button-core.css",library="yui/2in3")
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
