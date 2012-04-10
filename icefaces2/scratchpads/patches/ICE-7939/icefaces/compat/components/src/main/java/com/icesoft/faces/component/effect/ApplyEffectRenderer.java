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

package com.icesoft.faces.component.effect;

import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.context.effects.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import java.io.IOException;

public class ApplyEffectRenderer extends DomBasicRenderer {

    private static Log log = LogFactory.getLog(ApplyEffectRenderer.class);


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {

        try {
            String parentId = uiComponent.getParent().getClientId(facesContext);
            ApplyEffect af = (ApplyEffect) uiComponent;
            Effect fx = EffectBuilder.build(af.getEffectType());
            if (fx == null) {
                log.error("No Effect for effectType [" + af.getEffectType() + "]");
            } else {
                fx.setSequence(af.getSequence());
                fx.setSequenceId(af.getSequenceNumber().intValue());
                fx.setSubmit(af.getSubmit().booleanValue());
                fx.setTransitory(af.getTransitory().booleanValue());
                fx.setOptions(af.getOptions());

                if (af.getFire().booleanValue()) {
                    JavascriptContext.fireEffect(fx, uiComponent.getParent(), facesContext);
                    if (af.getAutoReset().booleanValue())
                        af.setFire(Boolean.FALSE);
                }
                if (af.getEvent() != null) {
                    String event = af.getEvent();
                    LocalEffectEncoder.encodeLocalEffect(parentId, fx, event, facesContext);
                }
            }
        } catch (Exception e) {
            log.error("Unexpected Exception in ApplyEffectRenderer",e);
        }
    }


}

