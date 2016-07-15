/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.contentnavbar;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.mobi.utils.TLDConstants;

@Component(
        tagName = "contentNavBar",
        componentClass = "org.icefaces.mobi.component.contentnavbar.ContentNavBar",
        rendererClass = "org.icefaces.mobi.component.contentnavbar.ContentNavBarRenderer",
        generatedClass = "org.icefaces.mobi.component.contentnavbar.ContentNavBarBase",

        componentType = "org.icefaces.ContentNavBar",
        rendererType = "org.icefaces.ContentNavBarRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentNavBar",
        tlddoc = "Renders a navigation tool bar for the contentPane component. " +
        		"Allowable children are contentMenuItems and HTML content. " +
        		"The contentNavBar must used inside of a contentPane. "
)

public class ContentNavBarMeta {
    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;

}
