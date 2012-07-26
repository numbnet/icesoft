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
package org.icefaces.mobi.component.augmentedreality;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.annotation.Required;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "augmentedReality",
        componentClass = "org.icefaces.mobi.component.augmentedreality.AugmentedReality",
        rendererClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityRenderer",
        generatedClass = "org.icefaces.mobi.component.augmentedreality.AugmentedRealityBase",
        componentType = "org.icefaces.AugmentedReality",
        rendererType = "org.icefaces.AugmentedRealityRenderer",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.AugmentedReality",
        tlddoc = "This mobility component implements an augmentedReality for a selected item" +
                " within the locationItems list that is it's child."
)

@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class AugmentedRealityMeta extends UIComponentBaseMeta  {
    @Property(required=Required.no, tlddoc="required formatted params for augmentedReality component")
    private String params;

    @Property(tlddoc="URL base for loading resources in AR view")
    private String urlBase;

    @Property(tlddoc = "style class of the component, rendered on the select root of the component")
    private String styleClass;

    @Property(tlddoc = "style of the component, rendered on the div root of the component")
    private String style;

    @Property(tlddoc = "input value, name of the location selected by the user")
    private String value;

}
