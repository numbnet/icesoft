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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.component.panelpositioned;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to track changes to positioned panel instances
 */
public class PanelPositionedModel implements Serializable {

    private List column;
    private Map idIndex = new HashMap();
    private int max = 0;

    public static PanelPositionedModel build() {

        PanelPositionedModel result = new PanelPositionedModel();

        return result;
    }


    public static PanelPositionedModel resetInstance(FacesContext context,
                                                     UIComponent component) {
        Map sessionMap = context.getExternalContext().getSessionMap();

        PanelPositionedModel result = build();
        sessionMap.put(getName(context, component), result);

        return result;
    }

    private static String getName(FacesContext context, UIComponent component) {
        return PanelPositionedModel.class.getName() + ":" +
                component.getClientId(context);
    }

    public static PanelPositionedModel getInstance(FacesContext context,
                                                   UIComponent component) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        PanelPositionedModel result = (PanelPositionedModel) sessionMap
                .get(getName(context, component));
        return result;
    }


    private PanelPositionedModel() {
        this.column = new ArrayList();
    }

    public void setIndex(String id, int index) {
        idIndex.put(id, new Integer(index));
    }

    public int getIndex(String id) {
        Integer I = (Integer) idIndex.get(id);
        if (I != null) {
            return I.intValue();
        }
        return -1;

    }

    public int size() {
        return this.column.size();
    }

}
