/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.menu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import javax.faces.model.SelectItem;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@ComponentExample(
        parent = MenuBean.BEAN_NAME,
        title = "example.ace.menu.effect.title",
        description = "example.ace.menu.effect.description",
        example = "/resources/examples/ace/menu/menuEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuEffect.xhtml",
                    resource = "/resources/examples/ace/menu/menuEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/menu/MenuEffect.java")
        }
)
@ManagedBean(name= MenuEffect.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuEffect extends ComponentExampleImpl<MenuEffect> implements Serializable {
    public static final String BEAN_NAME = "menuEffect";
    
    private SelectItem[] availableEffects = { new SelectItem("fade", "Fade"),
                                              new SelectItem("slide", "Slide") };
    
    private String effectName = availableEffects[0].getValue().toString();
    private int effectDuration = 400;
    
    public MenuEffect() {
        super(MenuEffect.class);
    }
    
    public SelectItem[] getAvailableEffects() { return availableEffects; }
    public String getEffectName() { return effectName; }
    public int getEffectDuration() { return effectDuration; }
    
    public void setEffectName(String effectName) { this.effectName = effectName; }
    public void setEffectDuration(int effectDuration) { this.effectDuration = effectDuration; }
}
