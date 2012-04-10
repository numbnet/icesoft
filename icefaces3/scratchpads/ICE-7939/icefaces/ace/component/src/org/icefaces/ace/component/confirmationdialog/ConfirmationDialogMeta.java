/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.ace.component.confirmationdialog;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;

@Component(
        tagName         = "confirmationDialog",
        componentClass  = "org.icefaces.ace.component.confirmationdialog.ConfirmationDialog",
        rendererClass   = "org.icefaces.ace.component.confirmationdialog.ConfirmationDialogRenderer",
        generatedClass  = "org.icefaces.ace.component.confirmationdialog.ConfirmationDialogBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.ConfirmationDialog",
        rendererType    = "org.icefaces.ace.component.ConfirmationDialogRenderer",
		componentFamily = "org.icefaces.ace.component",
		tlddoc = "The Confirmation Dialog is a component that displays a dialog that asks users to confirm or cancel their actions." +
                " If position of dialog is out of place, try putting it as last child of body." +
                "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/ConfirmationDialog\">ConfirmationDialog Wiki Documentation</a>."
        )

@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="util/combined.css"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="util/ace-components.js")
})

public class ConfirmationDialogMeta extends UIPanelMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;
	
	@Property(tlddoc="Text to be displayed in body.")
	private String message;
	
	@Property(tlddoc="Text for the header.")
	private String header;
	
	@Property(tlddoc="Message severity for the dislayed icon. Value can be whatever is appended to \".ui-icon-\" in the theme stylesheet.", defaultValue="alert")
	private String severity;
	
	@Property(tlddoc="Controls draggability.", defaultValue="true")
	private boolean draggable;
	
	@Property(tlddoc="Boolean value that specifies whether the document should be shielded with a partially transparent mask to require the user to close the Panel before being able to activate any elements in the document.", defaultValue="false")
	private boolean modal;
	
	@Property(tlddoc="Width of the dialog in pixels.", defaultValue="300")
	private int width;
	
	@Property(tlddoc="Height of the dialog in pixels.", defaultValue="Integer.MIN_VALUE")
	private int height;
	
	@Property(tlddoc="zindex property to control overlapping with other elements.", defaultValue="1000")
	private int zindex;
	
	@Property(tlddoc="Style class of the dialog container.")
	private String styleClass;
	
	@Property(tlddoc="Effect to use when showing the dialog. Standard jQuery effects are supported (see wiki page for exceptions).")
	private String showEffect;
	
	@Property(tlddoc="Effect to use when hiding the dialog. Standard jQuery effects are supported (see wiki page for exceptions).")
	private String hideEffect;
	
	@Property(tlddoc="Specifies where the dialog should be displayed. Possible values: \n1) a single string representing position within viewport: 'center', 'left', 'right', 'top', 'bottom'.\n2) an array containing an x,y coordinate pair in pixel offset from left, top corner of viewport (e.g. [350,100])\n3) an array containing x,y position string values (e.g. ['right','top'] for top right corner).")
	private String position;
	
	@Property(tlddoc="Specifies if dialog should be closed when escape key is pressed.", defaultValue="true")
	private boolean closeOnEscape;
	
	@Property(tlddoc="Specifies if close button should be displayed or not.", defaultValue="true")
	private boolean closable;
	
//	@Property(tlddoc="Appends dialog as a child of document body.", defaultValue="false")
//	private boolean appendToBody;
}
