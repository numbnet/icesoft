/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.datatable;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "column",
        componentClass = "org.icefaces.mobi.component.datatable.Column",
        rendererClass = "org.icefaces.mobi.component.datatable.ColumnRenderer",
        generatedClass = "org.icefaces.mobi.component.datatable.ColumnBase",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentType = "org.icefaces.Column",
        rendererType = "org.icefaces.ColumnRenderer",
        componentFamily = "org.icefaces.Column",
        tlddoc = "mobile data table column "
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ColumnMeta extends UIComponentBaseMeta{
    
    @Property(tlddoc="Defines a plain text header.")
    private String headerText;
    
    @Property(tlddoc="The value to render to the column cell. ")
    private String value;
    
    @Property(tlddoc="The minimum device width for which the column will display.")
    private String minDeviceWidth;
    
    @Property(tlddoc="optimizeExpression, when true, will cause" +
     " the DataTable to cache the ValueExpression Method bindings for" +
     " higher performance.", defaultValue = "false")
    private boolean optimizeExpression;
    
    @Property(tlddoc="The property name of the column to be used in the detail template. ")
    private String property;
}
