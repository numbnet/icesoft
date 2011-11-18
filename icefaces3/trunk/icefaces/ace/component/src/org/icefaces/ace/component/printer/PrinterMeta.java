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

package org.icefaces.ace.component.printer;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName         = "printer",
        componentClass  = "org.icefaces.ace.component.printer.Printer",
        rendererClass   = "org.icefaces.ace.component.printer.PrinterRenderer",
        generatedClass  = "org.icefaces.ace.component.printer.PrinterBase",
        extendsClass    = "javax.faces.component.UIComponentBase",
        componentType   = "org.icefaces.ace.component.Printer",
        rendererType    = "org.icefaces.ace.component.PrinterRenderer",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "Printer allows sending a specific JSF component to the printer, not the whole page. It needs to be nested inside an h:commandButton or h:outputLink component."
        )
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="printer/printer.js")
})

public class PrinterMeta extends UIComponentBaseMeta {

	@Property(name="for", tlddoc="Specifies the id of the component to print.")
	private String forValue;
}
