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

package org.icefaces.mobi.component.uploadhelper;


import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;



@Component(
        tagName = "uploadhelper",
        componentClass = "org.icefaces.mobi.component.uploadhelper.UploadHelper",
        rendererClass = "org.icefaces.mobi.component.uploadhelper.UploadHelperRenderer",
        generatedClass = "org.icefaces.mobi.component.uploadhelper.UploadHelperBase",
        componentType = "org.icefaces.UploadHelper",
        rendererType = "org.icefaces.UploadHelperRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.UploadHelper",
        tlddoc = "uploadhelper " +
                " detects mobile and desktop cases to support upload. "
)


@ResourceDependencies({
        @ResourceDependency(library = "org.icefaces.component.util", name = "component.js")
})
public class UploadHelperMeta extends UIComponentBaseMeta {


}
