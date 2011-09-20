/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icefaces.component.thumbnail;


import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import org.icefaces.component.annotation.Required;
import org.icefaces.component.baseMeta.UIComponentBaseMeta;

import javax.faces.application.ResourceDependencies;

@Component(
        tagName = "thumbnail",
        componentClass = "org.icefaces.component.thumbnail.Thumbnail",
        rendererClass = "org.icefaces.component.thumbnail.ThumbnailRenderer",
        generatedClass = "org.icefaces.component.thumbnail.ThumbnailBase",
        componentType = "org.icefaces.Thumbnail",
        rendererType = "org.icefaces.ThumbnailRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Thumbnail",
        tlddoc = "This mobility component supports a thumbnail of an image taken by an " +
                "ICEfaces camera component. "
)

@ResourceDependencies({
})
public class ThumbnailMeta extends UIComponentBaseMeta {

    @Property(defaultValue = "0", tlddoc = "tabindex of the component")
    private int tabindex;

    @Property(tlddoc = "style will be rendered on the root element of this " +
            "component.")
    private String style;

    @Property(tlddoc = "style class will be rendered on the root element of " +
            "this component.")
    private String styleClass;

    @Property( name = "for",
               required = Required.yes,
               tlddoc = "id of camera component the thumbnail uses to find the image for")
    private String For;

}
