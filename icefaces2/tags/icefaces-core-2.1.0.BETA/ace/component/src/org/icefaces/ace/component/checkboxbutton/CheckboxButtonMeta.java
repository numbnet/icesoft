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

package org.icefaces.ace.component.checkboxbutton;


import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.baseMeta.UISelectBooleanMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName        = "checkboxButton",
        componentClass = "org.icefaces.ace.component.checkboxbutton.CheckboxButton",
        rendererClass  = "org.icefaces.ace.component.checkboxbutton.CheckboxButtonRenderer",
        generatedClass = "org.icefaces.ace.component.checkboxbutton.CheckboxButtonBase",
        extendsClass   = "javax.faces.component.UISelectBoolean",
        componentType  = "org.icefaces.ace.component.CheckboxButton",
        rendererType   = "org.icefaces.ace.component.CheckboxButtonRenderer",
		componentFamily= "org.icefaces.ace.CheckboxButton",
		tlddoc="This component allows entry of a button which "+
		       "supports browsers that see checkbox as true or false, "+
		       "yes or no, on or off.  LabelPosition property allows label "+
		       "to be placed on the button-in case of sam style, or to the left "+
		       "of the button - in the case of rime style."
        )
@ResourceDependencies({
    	@ResourceDependency(name="yui/yui-min.js",library="yui/3_3_0"),
	    @ResourceDependency(name="loader/loader-min.js",library="yui/3_3_0"),
        @ResourceDependency(name="util/combined.js",library="icefaces.ace"),
        @ResourceDependency(name="yui2-skin-sam-button/assets/button-core.css",library="yui/2in3")
})

public class CheckboxButtonMeta extends UISelectBooleanMeta {
    
    @Property(tlddoc="A label to be printed either on the buttton or to the left of it "+
    		" according to labelPosition parameter")
    private String label;
    
/*    @Property(defaultValue="left",
    		tlddoc="Default is left for rime theme. Other possibility is \"on\" " +
    				"for sam skin.")
    private String labelPosition; */

	@Property(defaultValue="false",
			tlddoc= "When singleSubmit is true, changing the value of this component" +
					" will submit and execute this component only. Equivalent to " +
					" execute=\"@this\" render=\"@all\" of the f ajax tag. " +
					"When singleSubmit is false, no submit occurs. " +
					"The default value is false.")
    private boolean singleSubmit;
	    
    @Property(tlddoc="style of the component, rendered on the root div of the component")
	private String style;
	    
    @Property(tlddoc="style class of the component, rendered on the root div of the component.")
	private String styleClass;     
 
    @Property (tlddoc="tabindex of the component")
    private Integer tabindex;
    
    @Property (defaultValue="false",
    		tlddoc="disabled property. If true no input may be submitted via this" +
    				" component.  Is required by aria")
    private boolean disabled;
}
