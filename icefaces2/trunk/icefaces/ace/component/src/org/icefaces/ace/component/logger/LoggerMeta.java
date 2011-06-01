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

package org.icefaces.ace.component.logger;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.Property;

@Component(
        tagName         = "logger",
        componentClass  = "org.icefaces.ace.component.logger.Logger",
        rendererClass   = "org.icefaces.ace.component.logger.LoggerRenderer",
        generatedClass  = "org.icefaces.ace.component.logger.LoggerBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.Logger",
        rendererType    = "org.icefaces.ace.component.LoggerRenderer",
		componentFamily = "org.icefaces.ace.Logger"
        )
@ResourceDependencies({
    @ResourceDependency(name="util/combined.js",library="icefaces.ace")
})

public class LoggerMeta extends UIComponentBaseMeta {
//    
//    @Property   
//    private String label;

	@Property
    private String debugElement;
	
//	@Property
//	private int width;
//	
//	@Property
//	private int height;
//	
//	@Property
//	private boolean footerEnabled;
//	
//	@Property
//	private boolean logReaderEnabled;
	
//other properties not included here are thresholdMax, thresholdMin,
//draggable, outputBuffer, newestOnTop, verboseOutput, entryFormat
	
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;  
  
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property(tlddoc="style of the component")
    private String style;
    
}
