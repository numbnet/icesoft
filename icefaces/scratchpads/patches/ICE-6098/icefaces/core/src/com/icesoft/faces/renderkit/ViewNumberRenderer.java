/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.renderkit;

import java.util.Map;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.render.Renderer;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class ViewNumberRenderer extends Renderer {
    private static final Log log = LogFactory.getLog(ViewNumberRenderer.class);
    public static String VIEW_NUMBER = "com.icesoft.faces.viewNumber";
    public static String CONCURRENT_DOM_VIEWS = 
            "com.icesoft.faces.concurrentDOMViews";
    private static boolean concurrentDOMViews = false;

    public ViewNumberRenderer()  {
        String concurrentDOMViewsStr = FacesContext.getCurrentInstance()
                .getExternalContext().getInitParameter(CONCURRENT_DOM_VIEWS);
        concurrentDOMViews = "true".equalsIgnoreCase(concurrentDOMViewsStr);
        System.out.println("concurrentDOMViews config: " + concurrentDOMViews);
    }

    public void encodeBegin(FacesContext context, UIComponent component) 
        throws IOException  {

        if (context == null || component == null) {
            throw new NullPointerException(
                    "Null Faces context or component parameter");
        }

        if (!component.isRendered()) {
            return;
        }

        String page = (String) component.getAttributes().get("page");

        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("input", component);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", VIEW_NUMBER, null);
        writer.writeAttribute("value", getViewNumber(context), null);
        writer.endElement("input");

    }


    public static String getViewNumber(FacesContext facesContext)  {
        if (!concurrentDOMViews)  {
            return "1";
        }
        String viewNumber = null;
        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (null != viewRoot)  {
            viewNumber = (String) viewRoot.getAttributes().get(VIEW_NUMBER);
        }
        if (null != viewNumber) {
            return viewNumber;
        }

        Map sessionMap = facesContext.getExternalContext().getSessionMap();
        Integer currentViewNumber = (Integer) sessionMap.get(VIEW_NUMBER);
        if (null == currentViewNumber)  {
            currentViewNumber = new Integer(0);
        }
        currentViewNumber = new Integer(currentViewNumber.intValue() + 1);
        sessionMap.put(VIEW_NUMBER, currentViewNumber);
        viewNumber = currentViewNumber.toString();
        if (null != viewRoot)  {
            viewRoot.getAttributes().put(VIEW_NUMBER, viewNumber);
        }
        
        return viewNumber;
    }
}
