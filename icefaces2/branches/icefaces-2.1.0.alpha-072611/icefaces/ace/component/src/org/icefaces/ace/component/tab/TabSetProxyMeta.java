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

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;

import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(tagName ="tabSetProxy",
        componentClass  = "org.icefaces.ace.component.tab.TabSetProxy",
        generatedClass  = "org.icefaces.ace.component.tab.TabSetProxyBase",
        extendsClass    = "javax.faces.component.UIPanel", 
        componentType   = "org.icefaces.ace.component.TabSetProxy",
        componentFamily = "org.icefaces.ace.component",
        tlddoc = "The TabSetProxy component is used in conjunction with a " +
            "server-side TabSet component that is not inside of a form. " +
            "The TabSetProxy will then instead be placed inside of a form, " +
            "to handle the server communication on behalf of the TabSet.")
public class TabSetProxyMeta extends UIPanelMeta {
    @Property(tlddoc="id of the tabSet component")
    private String For;
}
