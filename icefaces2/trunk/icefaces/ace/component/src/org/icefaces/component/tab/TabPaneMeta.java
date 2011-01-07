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

package org.icefaces.component.tab;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

import org.icefaces.component.baseMeta.UIComponentBaseMeta;

@Component(tagName = "tabPane",
        componentClass  = "org.icefaces.component.tab.TabPane",
        generatedClass  = "org.icefaces.component.tab.TabPaneBase",
        extendsClass    = "javax.faces.component.UIComponentBase", 
        componentType   = "org.icefaces.TabPane",
        componentFamily = "org.icefaces.TabPane",
        tlddoc = "The TabPane component belongs inside of a TabSet " +
            "component, and encapsulates both the clickabled tab, and the " +
            "content pane that is shown when the TabPane is selected. The " +
            "clickable tab part may be specified by the label property, " +
            "or by the label facet, allowing for any components to be " +
            "placed within the clickable tab. The content pane is specified " +
            "through a combination of the header, body and footer facets.")
public class TabPaneMeta extends UIComponentBaseMeta {
    @Property (tlddoc="This attribute represents Label of the tab")  
    private String label;
    
    @Property (tlddoc="If true then this tab will be disabled and can not be selected. Currently not supported.") 
    private boolean disabled;    

    
    @Facets
    class FacetsMeta{
        @Facet
        UIComponent header;
        @Facet
        UIComponent body;
        @Facet
        UIComponent footer;    
        @Facet
        UIComponent label;          
    }    
}