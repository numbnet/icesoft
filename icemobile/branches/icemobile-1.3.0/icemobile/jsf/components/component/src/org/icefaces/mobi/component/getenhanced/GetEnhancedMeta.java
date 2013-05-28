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

package org.icefaces.mobi.component.getenhanced;


import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import org.icefaces.mobi.utils.TLDConstants;

@Component(
        tagName = "getEnhanced",
        componentClass = "org.icefaces.mobi.component.getenhanced.GetEnhanced",
        rendererClass = "org.icefaces.mobi.component.getenhanced.GetEnhancedRenderer",
        generatedClass = "org.icefaces.mobi.component.getenhanced.GetEnhancedBase",
        componentType = "org.icefaces.GetEnhanced",
        rendererType = "org.icefaces.GetEnhancedRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.GetEnhanced",
        tlddoc = "This mobility component renders a warning when the ICEmobile container" +
        		" is not detected, and will render a device-dependent link to the ICEmobile container download. This component should" +
        		" be used in association with other ICEmobile container-dependent components, such as the camera or camcorder, which" +
        		" require the ICEmobile container for optimum use."
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class GetEnhancedMeta extends UIComponentBaseMeta {

	@Property(defaultValue = "false", 
            tlddoc = TLDConstants.DISABLED)
    private boolean disabled;

    @Property(tlddoc = TLDConstants.STYLE)
    private String style;

    @Property(tlddoc = TLDConstants.STYLECLASS)
    private String styleClass;
    
    @Property(defaultValue = "true",
    		tlddoc = "When set to false, the ICEmobile download link will not be displayed.")
    private boolean includeLink;
    
    @Property(tlddoc="The custom download message link for an Android device.")
    private String androidMsg;
    
    @Property(tlddoc="The custom download message link for an iOS device.")
    private String iosMsg;
    
    @Property(tlddoc="The custom download message link for an BlackBerry device.")
    private String blackberryMsg;
    
    
    
    

}
