/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icefaces.mobi.component.contentstack;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.baseMeta.UIComponentBaseMeta;


@Component(
        tagName = "contentStackFormProxy",
        componentClass = "org.icefaces.mobi.component.contentstack.ContentStackFormProxy",
        rendererClass = "org.icefaces.mobi.component.contentstack.ContentStackFormProxyRenderer",
        generatedClass = "org.icefaces.mobi.component.contentstack.ContentStackFormProxyBase",
        componentType = "org.icefaces.ContentStackFormProxy",
        rendererType = "org.icefaces.ContentStackFormProxyRenderer",
        extendsClass = "javax.faces.component.UIComponentBase",
        componentFamily = "org.icefaces.ContentStackFormProxy",
        tlddoc = "contentStackFormProxy allows contentPanes to nest forms and still maintain server-side current pane state")
public class ContentStackFormProxyMeta extends UIComponentBaseMeta {

     
}
