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

package com.icesoft.faces.component;

/**
 * All style class names are extracted from JSR-168 spec 
 *
 */
public class PORTLET_CSS_DEFAULT {
						//PLT.C.6 Menus
	public static final String PORTLET_MENU = "portlet-menu"; // (menuBar)
    public static final String PORTLET_MENU_ITEM = "portlet-menu-item"; // (root level menuItem) 
    public static final String PORTLET_MENU_ITEM_HOVER = "portlet-menu-item-hover"; // (root level menuItem:hover)
    public static final String PORTLET_MENU_CASCADE_ITEM = "portlet-menu-cascade-item"; // (submenu menuItem)

    					//PLT.C.5 Forms
    public static final String PORTLET_FORM_LABEL = "portlet-form-label"; //       (outputText, outputLabel)
    public static final String PORTLET_FORM_INPUT_FIELD = "portlet-form-input-field"; // (inputText, inputTextArea, inputSecret)
    public static final String PORTLET_FORM_BUTTON = "portlet-form-button"; //     (commandButton)
    public static final String PORTLET_FORM_FIELD = "portlet-form-field"; // (selectBooleanCheckbox,selectManyCheckbox,selectManyListbox, selectManyMenu, selectOneListbox, selectOneMenu, selectOneRadio )

    		       		//PLT.C.4 Sections (Table)	
    public static final String PORTLET_SECTION_HEADER =  "portlet-section-header"; //   (thead)
    public static final String PORTLET_SECTION_BODY = "portlet-section-body"; //     (Normal text in a table cell (TD))
    public static final String PORTLET_SECTION_ALTERNATE = "portlet-section-alternate"; // (text in every other row in the cell (alternate tr))
    public static final String PORTLET_SECTION_FOOTER = "portlet-section-footer"; //   (tfoot)   

    					//PLT.C.3 Messages (ice:message/ice:messages)
    public static final String PORTLET_MSG_ERROR = "portlet-msg-error"; //	errorClass     
    public static final String PORTLET_MSG_INFO = "portlet-msg-info"; //	infoClass      
    public static final String PORTLET_MSG_ALERT = "portlet-msg-alert"; //	warnClass      

}
