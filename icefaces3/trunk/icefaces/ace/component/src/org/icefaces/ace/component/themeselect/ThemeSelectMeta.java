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
package org.icefaces.ace.component.themeselect;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIInputMeta;
import org.icefaces.ace.resources.ACEResourceNames;
import org.icefaces.resources.ICEResourceDependencies;
import org.icefaces.resources.ICEResourceDependency;
import org.icefaces.resources.ICEResourceLibrary;

@Component(
        tagName = "themeSelect",
        componentClass = "org.icefaces.ace.component.themeselect.ThemeSelect",
        rendererClass = "org.icefaces.ace.component.themeselect.ThemeSelectRenderer",
        generatedClass = "org.icefaces.ace.component.themeselect.ThemeSelectBase",
        extendsClass = "javax.faces.component.UIInput",
        componentType = "org.icefaces.ace.component.ThemeSelect",
        rendererType = "org.icefaces.ace.component.ThemeSelectRenderer",
        componentFamily = "org.icefaces.ace.ThemeSelect",
        tlddoc = "The themeSelect component can be used to dynamically change the current ACE ThemeRoller theme in the application. " +
                "See <a href=\"http://www.icesoft.org/wiki/display/ICE/ace.theme\">Wiki Documentation</a> for more details on themes."
)
@ICEResourceLibrary(ACEResourceNames.ACE_LIBRARY)
@ICEResourceDependencies({
        @ICEResourceDependency(name = ACEResourceNames.COMBINED_CSS),
        @ICEResourceDependency(name = ACEResourceNames.JQUERY_JS),
        @ICEResourceDependency(name = ACEResourceNames.COMPONENTS_JS)
})
public class ThemeSelectMeta extends UIInputMeta {
}
