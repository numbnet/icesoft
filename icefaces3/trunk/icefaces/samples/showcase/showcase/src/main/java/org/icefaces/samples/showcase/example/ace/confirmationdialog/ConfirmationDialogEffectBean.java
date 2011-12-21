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

package org.icefaces.samples.showcase.example.ace.confirmationdialog;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.LinkedHashMap;

@ComponentExample(
        parent = ConfirmationDialogBean.BEAN_NAME,
        title = "example.ace.confirmationdialog.effect.title",
        description = "example.ace.confirmationdialog.effect.description",
        example = "/resources/examples/ace/confirmationdialog/confirmationDialogEffect.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="confirmationDialogEffect.xhtml",
                    resource = "/resources/examples/ace/confirmationdialog/confirmationDialogEffect.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ConfirmationDialogEffect.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/confirmationdialog/ConfirmationDialogEffectBean.java")
        }
)
@ManagedBean(name= ConfirmationDialogEffectBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ConfirmationDialogEffectBean extends ComponentExampleImpl<ConfirmationDialogEffectBean> implements Serializable {
    public static final String BEAN_NAME = "confirmationDialogEffectBean";
    private String showEffect;
    private String hideEffect;
    private String outcome;
    private LinkedHashMap<String, String> effects;

    public ConfirmationDialogEffectBean() {
        super(ConfirmationDialogEffectBean.class);
        outcome = null;
        
        effects = initializeDialogEffects();
        showEffect = effects.get("Fade");
        hideEffect = effects.get("Fade");
    }

        private LinkedHashMap<String, String> initializeDialogEffects() 
        {
            LinkedHashMap<String, String> list = new LinkedHashMap<String, String>();
            list.put("Fade", "fade");
            list.put("Highlight", "highlight");
            list.put("Blind", "blind");
            list.put("Bounce", "bounce");
            list.put("Clip", "clip");
            list.put("Explode", "explode");
            list.put("Puff", "puff");
            list.put("Pulsate", "pulsate");
            list.put("Scale", "scale");
            list.put("Slide", "slide");
            return list;
    }

    public void yes(ActionEvent actionEvent) { 
            outcome = "You clicked 'yes'";
    }

    public void no(ActionEvent actionEvent) { 
            outcome = "You clicked 'no'";
    }

    public String getOutcome() {
            return outcome;
    }

    public String getHideEffect() {
        return hideEffect;
    }

    public void setHideEffect(String hideEffect) {
        this.hideEffect = hideEffect;
    }

    public String getShowEffect() {
        return showEffect;
    }

    public void setShowEffect(String showEffect) {
        this.showEffect = showEffect;
    }

    public LinkedHashMap<String, String> getEffects() {
        return effects;
    }

    public void setEffects(LinkedHashMap<String, String> effects) {
        this.effects = effects;
    }
}