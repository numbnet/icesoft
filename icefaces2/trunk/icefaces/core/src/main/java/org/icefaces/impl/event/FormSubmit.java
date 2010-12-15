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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIForm;
import javax.faces.component.UINamingContainer;
import javax.faces.component.html.HtmlForm;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class FormSubmit implements SystemEventListener {
    public static final String DISABLE_CAPTURE_SUBMIT = "DISABLE_CAPTURE_SUBMIT";
    private static final String CAPTURE_SUBMIT_SUFFIX = "_captureSubmit";
    private boolean deltaSubmit;
    private boolean partialStateSaving;

    public FormSubmit() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        deltaSubmit = EnvUtils.isDeltaSubmit(facesContext);
        partialStateSaving = EnvUtils.isPartialStateSaving(facesContext);
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(context)) {
            return;
        }

        //using PostAddToViewEvent ensures that the component resource is added to the view only once
        final HtmlForm form = (HtmlForm) ((PostAddToViewEvent) event).getComponent();
        if (form.getAttributes().get(DISABLE_CAPTURE_SUBMIT) != null) {
            return;
        }
        String componentId = form.getId() + CAPTURE_SUBMIT_SUFFIX;

        if (!partialStateSaving)  {
            for (UIComponent child : form.getChildren())  {
                String id = child.getId();
                if ((null != id) && id.endsWith(CAPTURE_SUBMIT_SUFFIX))  {
                    return;
                }
            }
        }

        UIOutput scriptWriter = new UIOutputWriter() {
            public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                if (form.getAttributes().get(DISABLE_CAPTURE_SUBMIT) != null) {
                    return;
                }
                String formId = form.getClientId(context);
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", "type");
                writer.writeAttribute("id", getClientId(context), "id");
                writer.write("ice.captureSubmit('");
                writer.write(formId);
                writer.write("',");
                writer.write(Boolean.toString(deltaSubmit));
                writer.write(");");
                writer.write("ice.captureEnterKey('");
                writer.write(formId);
                writer.write("');");
                writer.endElement("script");
            }
        };

        scriptWriter.setId(componentId);
        scriptWriter.setTransient(true);
        form.getChildren().add(0, scriptWriter);

        AjaxDisabledWriter disabledWriter = new AjaxDisabledWriter();
        disabledWriter.setTransient(true);
        //add to end of list
        form.getChildren().add(disabledWriter);

    }

    public boolean isListenerForSource(Object source) {
        return source instanceof HtmlForm;
    }
}

class AjaxDisabledWriter extends UIOutputWriter {
    public void encode(ResponseWriter writer, FacesContext context)
            throws IOException {
        UIForm form =  AjaxDisabledList.getContainingForm(this);
        //consume with remove to reset the list each time
        String value = (String) form.getAttributes()
                .remove(AjaxDisabledList.DISABLED_LIST);
        if (null == value) {
            return;
        }
        writer.startElement("input", this);
        writer.writeAttribute("type", "hidden", "type");
        writer.writeAttribute("id", getClientId(context), "id");
        writer.writeAttribute("disabled", "true", "disabled");
        writer.writeAttribute("value", value, "value");
        writer.endElement("input");
    }

    public String getClientId(FacesContext context)  {
        UIForm form =  AjaxDisabledList.getContainingForm(this);
        return (form.getClientId() + UINamingContainer
                    .getSeparatorChar(context)+ "ajaxDisabled");
    }
}
