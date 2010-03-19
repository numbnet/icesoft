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

package com.icesoft.faces.component.ext;


import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class OutputHead extends javax.faces.component.UIComponentBase{
    private String dir;
    private String lang;
    private String profile;

    public OutputHead() {
        super();
        setRendererType("com.icesoft.faces.OutputHead");
    }
    /**
     * <p>Return the family for this component.</p>
     */
    public String getFamily() {
        return "com.icesoft.faces.OutputHead";
    }

    public String getDir() {
        return (String) getAttribute("dir", dir, null);
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLang() {
        return (String) getAttribute("lang", lang, null);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getProfile() {
        return (String) getAttribute("profile", profile, null);
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    private Object getAttribute(String name, Object localValue, Object defaultValue) {
        if (localValue != null) return localValue;
        ValueBinding vb = getValueBinding(name);
        if (vb == null) return defaultValue;
        Object value = vb.getValue(getFacesContext());
        if (value == null) return defaultValue;
        return value;
    }
    
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                dir,
                lang,
                profile,
        };
    }

    public void restoreState(FacesContext context, Object state) {
        String[] attrNames = {
                "dir",
                "lang",
                "profile",
        };
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        for (int i = 0; i < attrNames.length; i++) {
            getAttributes().put(attrNames[i], values[i + 1]);
        }
    }
}