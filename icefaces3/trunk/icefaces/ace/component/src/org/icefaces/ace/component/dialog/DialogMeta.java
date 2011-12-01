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

package org.icefaces.ace.component.dialog;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;
import org.icefaces.ace.meta.annotation.ClientBehaviorHolder;
import org.icefaces.ace.meta.annotation.ClientEvent;
import org.icefaces.ace.api.IceClientBehaviorHolder;

@Component(
        tagName         = "dialog",
        componentClass  = "org.icefaces.ace.component.dialog.Dialog",
        rendererClass   = "org.icefaces.ace.component.dialog.DialogRenderer",
        generatedClass  = "org.icefaces.ace.component.dialog.DialogBase",
        extendsClass    = "javax.faces.component.UIPanel",
        componentType   = "org.icefaces.ace.component.Dialog",
        rendererType    = "org.icefaces.ace.component.DialogRenderer",
		componentFamily = "org.icefaces.ace.Dialog",
		tlddoc = "Dialog is a container component that can overlay other elements on page. Dialog has several customization options such as modal, resize, width, height, position."
        )
@ResourceDependencies({
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.css"),
	@ResourceDependency(library="icefaces.ace", name="jquery/jquery.js"),
	@ResourceDependency(library="icefaces.ace", name="jquery/ui/jquery-ui.js"),
	@ResourceDependency(library="icefaces.ace", name="core/core.js"),
	@ResourceDependency(library="icefaces.ace", name="dialog/dialog.js")
})
@ClientBehaviorHolder(events = {
	@ClientEvent(name="close", javadoc="...", tlddoc="...", defaultRender="@all", defaultExecute="@all")
}, defaultEvent="close")

public class DialogMeta extends UIPanelMeta {

	@Property(tlddoc="Name of the widget to access client side api")
	private String widgetVar;
	
	@Property(tlddoc="Header text")
	private String header;
	
	@Property(tlddoc="Boolean value whether to allow the user to drag the Panel using its header", defaultValue="true")
	private boolean draggable;
	
	@Property(tlddoc="Makes the dialog resizable. Should be false if width or height is auto, or resizing may hehave erratically.", defaultValue="true")
	private boolean resizable;
	
	@Property(tlddoc="Boolean value that specifies whether the document should be shielded with a partially transparent mask to require the user to close the Panel before being able to activate any elements in the document", defaultValue="false")
	private boolean modal;
	
	@Property(tlddoc="visible can be used to toggle visibility on the server, rendered should not be used that way, setting rendered=false on a visible modal dialog will not remove the modality layer, visible=false must be set first (or client-side JS function called)", defaultValue="false")
	private boolean visible;

	@Property(tlddoc="Width of the element in pixels. Default is auto. If auto, resizable should be false, or resizing may hehave erratically. If auto, IE7 may not size or position properly.", defaultValue="Integer.MIN_VALUE")
	private int width;
	
	@Property(tlddoc="Height of the element in pixels. Default is auto. If auto, resizable should be false, or resizing may hehave erratically. If auto, IE7 may not size or position properly.", defaultValue="Integer.MIN_VALUE")
	private int height;

	@Property(tlddoc="zindex property to control overlapping with other elements", defaultValue="1000")
	private int zindex;
	
	@Property(tlddoc="Minimum width of a resizable dialog", defaultValue="150")
	private int minWidth;
	
	@Property(tlddoc="Minimum height of resizable dialog", defaultValue="0")
	private int minHeight;
	
	@Property(tlddoc="Style class of the main container of dialog")
	private String styleClass;
	
	@Property(tlddoc="Effect to be displayed when showing the dialog")
	private String showEffect;
	
	@Property(tlddoc="Effect to be displayed when hiding the dialog")
	private String hideEffect;
	
	@Property(tlddoc="Specifies where the dialog should be displayed. Possible values: \n1) a single string representing position within viewport: 'center', 'left', 'right', 'top', 'bottom'.\n2) an array containing an x,y coordinate pair in pixel offset from left, top corner of viewport (e.g. [350,100])\n3) an array containing x,y position string values (e.g. ['right','top'] for top right corner).")
	private String position;
	
	@Property(tlddoc="Boolean value that Specifies whether the dialog should close when it has focus and the user presses the escape (ESC) key (default true).", defaultValue="true")
	private boolean closeOnEscape;
	
	@Property(tlddoc="Boolean value that specifies whether the dialog should have a close button in the header (default true)", defaultValue="true")
	private boolean closable;
	
	@Property(tlddoc="Javascript code to be executed when showing the dialog")
	private String onShow;
	
	@Property(tlddoc="Javascript code to be executed when hiding the dialog")
	private String onHide;
	
	@Property(tlddoc="Boolean value that specifies whether the dialog should be appended to the page body (default false)", defaultValue="false")
	private boolean appendToBody;
	
	@Property(tlddoc="Boolean value that specifies whether the dialog should have a header (default true)", defaultValue="true")
	private boolean showHeader;
}
