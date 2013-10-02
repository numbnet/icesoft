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
package org.icefaces.mobi.component.contentstack;
import org.icefaces.ace.meta.annotation.Component;
import org.icefaces.ace.meta.annotation.Property;
import org.icefaces.ace.meta.baseMeta.UIPanelMeta;


@Component(
        tagName = "contentStack",
        componentClass = "org.icefaces.mobi.component.contentstack.ContentStack",
        rendererClass = "org.icefaces.mobi.component.contentstack.ContentStackRenderer",
        generatedClass = "org.icefaces.mobi.component.contentstack.ContentStackBase",
        componentType = "org.icefaces.ContentStack",
        rendererType = "org.icefaces.ContentStackRenderer",
        extendsClass = "javax.faces.component.UIPanel",
        componentFamily = "org.icefaces.ContentStack",
        tlddoc = "contentStack manages child contentPanes, controlling which child is visible.")
public class ContentStackMeta extends UIPanelMeta {

     @Property( tlddoc="The id of the panel that is visible.")
     private String currentId;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLE)
     private String style;

     @Property(tlddoc = org.icefaces.mobi.utils.TLDConstants.STYLECLASS)
     private String styleClass;

     @Property(tlddoc="The id of contentStackMenu used with this stack.")
     private String contentMenuId;

}
