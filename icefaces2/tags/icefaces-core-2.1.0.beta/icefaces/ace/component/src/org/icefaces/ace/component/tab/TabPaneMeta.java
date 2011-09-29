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

package org.icefaces.ace.component.tab;

import javax.faces.component.UIComponent;

import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Facet;
import org.icefaces.ace.meta.annotation.Facets;
import org.icefaces.ace.meta.annotation.Property;

@Component(tagName = "tabPane",
        componentClass  = "org.icefaces.ace.component.tab.TabPane",
        generatedClass  = "org.icefaces.ace.component.tab.TabPaneBase",
        extendsClass    = "javax.faces.component.UIComponentBase", 
        componentType   = "org.icefaces.ace.component.TabPane",
        componentFamily = "org.icefaces.ace.TabPane",
        tlddoc = "The TabPane component belongs inside of a TabSet " +
            "component, and encapsulates both the clickabled tab, and the " +
            "content pane that is shown when the TabPane is selected. The " +
            "clickable tab part may be specified by the label property, " +
            "or by the label facet, allowing for any components to be " +
            "placed within the clickable tab. The content pane is specified " +
            "through a combination of the header, body and footer facets.")
public class TabPaneMeta extends UIComponentBaseMeta {
    @Property(tlddoc="This attribute represents Label of the tab")
    private String label;
    
    @Property (tlddoc="If true then this tab will be disabled and can not be selected.")
    private boolean disabled;

    @Property(tlddoc="If true, then when the tabSet has clientSide=false, " +
            "this tabPane will be lazily loaded and cached on the client. " +
            "That is, once this tabPane has been visited, and rendered into " +
            "the client, it will remain rendered onto the client. As long " +
            "as its contents do not change on the server, they will remain " +
            "intact on the client. But, if its contents do change on the " +
            "server, then they will be reflected in the client via standard " +
            "ICEfaces mechanisms. When clientSide=true, all tabPane " +
            "components are always cached on the client.")
    private boolean cacheOnClient;

    
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
