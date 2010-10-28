/*
 * Version: MPL 1.1
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.impl.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class that can be used to render markup within forms. The markup is rendered just before 'form' end tag.
 */
public class FormEndRendering {

    /**
     * Register FormEndRenderer to the list of renderers.
     *
     * @param context  current FacesContext
     * @param renderer the renderer
     */
    public static void addRenderer(FacesContext context, FormEndRenderer renderer) {
        Map attributes = context.getAttributes();
        ArrayList list = (ArrayList) attributes.get(FormEndRendering.class.getName());
        if (list == null) {
            list = new ArrayList();
            attributes.put(FormEndRendering.class.getName(), list);
        }
        list.add(renderer);
    }

    /**
     * Method used by form renderers to invoke all the registered renderers before encodeEnd is executed.
     *
     * @param context current FacesContext
     * @param form    the form component
     * @throws IOException
     */
    public static void renderIntoForm(FacesContext context, UIComponent form) throws IOException {
        ArrayList o = (ArrayList) context.getAttributes().get(FormEndRendering.class.getName());
        if (o != null) {
            Iterator i = o.iterator();
            while (i.hasNext()) {
                FormEndRenderer formEndRenderer = (FormEndRenderer) i.next();
                formEndRenderer.encode(context, form);
            }
        }
    }
}
