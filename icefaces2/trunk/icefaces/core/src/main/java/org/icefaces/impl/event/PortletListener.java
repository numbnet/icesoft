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

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class PortletListener implements SystemEventListener {

    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
        final FacesContext fc = FacesContext.getCurrentInstance();

        if (!EnvUtils.isICEfacesView(fc)) {
            //If ICEfaces is not configured for this view, we don't need to process this event.
            return;
        }

        if (!EnvUtils.instanceofPortletRequest(fc.getExternalContext().getRequest())) {
            //If we're not running in a portlet, we don't need to process this event.
            return;
        }

        if (EnvUtils.isLiferay()) {
            //We currently only have a special script snippet for Liferay
            UIViewRoot root = fc.getViewRoot();
            root.addComponentResource(fc, getLiferayScriptComponent(), "head");
        }

    }

    public boolean isListenerForSource(Object o) {
        return true;
    }

    private UIComponent getLiferayScriptComponent() {

        // The way that Liferay recommends to extend the client session timer is to call
        // Liferay.Session.setCookie() during any request to the server. However, due to a bug,
        // this call does not have the desired effect after the banner has been displayed for
        // the first time. The only way to prevent the banner from showing even when there is
        // client activity is to call extend() so that the setTimeout call is cleared and restarted.
        // Unfortunately, calling extend() makes an extra call to the server but it's unavoidable
        // until Liferay changes their code.
        UIOutput liferayScript = new UIOutputWriter() {

            public void encode(ResponseWriter writer, FacesContext context) throws IOException {

                writer.startElement("script", this);
                writer.write("if(!extendLiferayClientTimer){");
                writer.write("    var extendLiferayClientTimer = function() {");
                writer.write("        if(Liferay.Session){");
//                writer.write("            Liferay.Session.setCookie();");
                writer.write("            Liferay.Session.extend();");
                writer.write("        }");
                writer.write("    };");
                writer.write("    if (Liferay) {");
                writer.write("        ice.onSubmitSend(extendLiferayClientTimer);");
                writer.write("    }");
                writer.write("}");
                writer.endElement("script");
            }
        };

        liferayScript.setTransient(true);
        return liferayScript;
    }
}
