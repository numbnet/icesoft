package org.icefaces.mobi.component.dataview;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Expression;
import org.icefaces.ace.meta.annotation.Property;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License atA
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * <p/>
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p/>
 * User: Nils Lundquist
 * Date: 2013-04-01
 * Time: 10:49 AM
 */
@Component(
        tagName = "dataViewColumn",
        componentClass = "org.icefaces.mobi.component.dataview.DataViewColumn",
        generatedClass = "org.icefaces.mobi.component.dataview.DataViewColumnBase",
        componentType = "org.icefaces.DataViewColumn",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.DataViewColumn",
        tlddoc = ""
)
public class DataViewColumnMeta {
    @Property
    String headerText;

    @Property
    String footerText;

    @Property(expression = Expression.VALUE_EXPRESSION)
    Object value;
}
