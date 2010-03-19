/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
 
 package com.icesoft.faces.component.facelets;

import com.sun.facelets.tag.jsf.ComponentHandler;
import com.sun.facelets.tag.jsf.ComponentConfig;
import com.sun.facelets.tag.MetaRuleset;
import com.sun.facelets.tag.MethodRule;
import com.icesoft.faces.component.dragdrop.DragEvent;
import com.icesoft.faces.component.dragdrop.DropEvent;
import com.icesoft.faces.component.ext.RowSelectorEvent;
import com.icesoft.faces.component.ext.ClickActionEvent;
import com.icesoft.faces.component.panelpositioned.PanelPositionedEvent;
import com.icesoft.faces.component.paneltabset.TabChangeEvent;
import com.icesoft.faces.component.outputchart.OutputChart;
import com.icesoft.faces.component.DisplayEvent;
import com.icesoft.faces.component.selectinputtext.TextChangeEvent;

import java.util.EventObject;

/**
 * @since 1.6
 */
public class IceComponentHandler extends ComponentHandler {
    public IceComponentHandler(ComponentConfig componentConfig) {
        super(componentConfig);
    }
    
    protected MetaRuleset createMetaRuleset(Class type) {
        MetaRuleset m = super.createMetaRuleset(type);
        if( tag.getNamespace() != null &&
            tag.getNamespace().equals("http://www.icesoft.com/icefaces/component") )
        {
            if( tag.getLocalName().equals("inputFile") ) {
                m.addRule( new MethodRule("progressListener", null, new Class[] {EventObject.class}) );
            }
            else if( tag.getLocalName().equals("outputChart") ) {
                m.addRule( new MethodRule("renderOnSubmit", Boolean.TYPE, new Class[] {OutputChart.class}) );
            }
            else if( tag.getLocalName().equals("panelGroup") ) {
                m.addRule( new MethodRule("dragListener", null, new Class[] {DragEvent.class}) );
                m.addRule( new MethodRule("dropListener", null, new Class[] {DropEvent.class}) );
            }
            else if( tag.getLocalName().equals("panelPositioned") ) {
                m.addRule( new MethodRule("listener", null, new Class[] {PanelPositionedEvent.class}) );
            }
            else if( tag.getLocalName().equals("panelTabSet") ) {
                m.addRule( new MethodRule("tabChangeListener", null, new Class[] {TabChangeEvent.class}) );
            }
            else if( tag.getLocalName().equals("rowSelector") ) {
                m.addRule( new MethodRule("selectionListener", null, new Class[] {RowSelectorEvent.class}) );
                m.addRule( new MethodRule("selectionAction", null, new Class[0]) );
                m.addRule( new MethodRule("clickListener", null, new Class[] {ClickActionEvent.class}) );
                m.addRule( new MethodRule("clickAction", null, new Class[0]) );
            }
            else if( tag.getLocalName().equals("panelTooltip") ) {
                m.addRule( new MethodRule("displayListener", null, new Class[] {DisplayEvent.class}) );
            }
            else if( tag.getLocalName().equals("menuPopup") ) {
                m.addRule( new MethodRule("displayListener", null, new Class[] {DisplayEvent.class}) );
            }
            else if( tag.getLocalName().equals("selectInputText") ) {
                m.addRule( new MethodRule("textChangeListener", null, new Class[] {TextChangeEvent.class}) );
            }
        }
        return m;
    }
}
