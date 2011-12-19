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

package org.icefaces.demo.basic;

import javax.faces.bean.ViewScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ResponseWriter;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 *
 */
@ManagedBean(name = "ajaxecho")
@ViewScoped
public class AjaxEcho {

    private String str;

    private static int count;

    private String filepath;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void reset() {
        this.str = "";
    }

    /**
     * Just a faux method to get post working. 
     */
    public void sendIt() {
    }

    public void toggleInclude() {

         ResponseWriter wr = FacesContext.getCurrentInstance().getResponseWriter();
        if (filepath == null) {
            filepath = "snippet.xhtml";
        } else {
            filepath = null;
        } 
    } 

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
