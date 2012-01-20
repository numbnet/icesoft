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

package org.icefaces.ace.component.rowpanelexpander;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIColumnMeta;

import javax.el.MethodExpression;

@Component(
        tagName = "panelExpansion",
        componentClass = "org.icefaces.ace.component.rowpanelexpander.RowPanelExpander",
        generatedClass = "org.icefaces.ace.component.rowpanelexpander.RowPanelExpanderBase",
        extendsClass = "javax.faces.component.UIColumn",
        componentType = "org.icefaces.ace.component.RowPanelExpander",
        componentFamily = "org.icefaces.ace.RowPanelExpander",
        tlddoc = "<p>Renders a table-width panel filled with its children components, located underneath the row where ace:expansionToggler was activated.</p>" +
                 "<p>For more information, see the <a href=\"http://wiki.icefaces.org/display/ICE/RowPanelExpander\">Row Panel Expander Wiki Documentation</a>.</p>"
)
public class RowPanelExpanderMeta extends UIColumnMeta {}
