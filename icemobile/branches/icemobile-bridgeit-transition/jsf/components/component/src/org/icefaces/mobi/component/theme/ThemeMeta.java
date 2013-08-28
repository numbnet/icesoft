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
package org.icefaces.mobi.component.theme;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;

@Component(
        tagName = "theme",
        componentClass = "org.icefaces.mobi.component.theme.Theme",
        rendererClass = "org.icefaces.mobi.component.theme.ThemeRenderer",
        generatedClass = "org.icefaces.mobi.component.theme.ThemeBase",
        componentType = "org.icefaces.Theme",
        rendererType = "org.icefaces.ThemeRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.Theme",
        tlddoc = "The theme component renders an ICEmobile theme"
)
@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})

public class ThemeMeta extends UIComponentBaseMeta {


    @Property(tlddoc = "The name of the theme to render, options include 'auto', 'iphone', 'ipad', 'bberry', 'bb10', 'android_light', 'android_dark', and 'jqm'.")
    private String name;
 }

