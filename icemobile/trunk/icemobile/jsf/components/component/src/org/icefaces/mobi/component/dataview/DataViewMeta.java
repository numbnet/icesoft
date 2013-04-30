package org.icefaces.mobi.component.dataview;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.DefaultValueType;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

/**
 * Copyright 2010-2013 ICEsoft Technologies Canada Corp.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
 * Time: 10:47 AM
 */

@Component(
    tagName = "dataView",
    componentClass = "org.icefaces.mobi.component.dataview.DataView",
    rendererClass = "org.icefaces.mobi.component.dataview.DataViewRenderer",
    generatedClass = "org.icefaces.mobi.component.dataview.DataViewBase",
    componentType = "org.icefaces.DataView",
    rendererType = "org.icefaces.DataViewRenderer",
    extendsClass = "javax.faces.component.UIComponentBase",
    componentFamily = "org.icefaces.DataView",
    tlddoc = ""
)
@ResourceDependencies({
    @ResourceDependency( library = "org.icefaces.component.util", name = "component.js" )
})
public class DataViewMeta extends UIComponentBaseMeta {
    @Property
    String var;

    @Property
    String rowIndexVar;

    @Property(tlddoc = "")
    Object value;

    @Property
    boolean disabled;

    @Property
    boolean rowStripe;

    @Property(defaultValue = "true", defaultValueType = DefaultValueType.EXPRESSION)
    boolean rowStroke;

    @Property
    String style;

    @Property
    String styleClass;

    @Property
    Integer activeRowIndex;

    @Property(defaultValue = "org.icefaces.mobi.component.dataview.ActivationMode.server",
              defaultValueType = DefaultValueType.EXPRESSION)
    ActivationMode activationMode;
}
